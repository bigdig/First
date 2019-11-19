package com.xxl.job.executor.service.jobhandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ddfc.service.RedisUtil;
import com.tfzq.tycrm.entity.TyActivity;
import com.tfzq.tycrm.entity.TyActivityStaff;
import com.tfzq.tycrm.entity.TyStafflist;
import com.tfzq.tycrm.service.TyActivityService;
import com.tfzq.tycrm.service.TyActivityStaffService;
import com.tfzq.tycrm.service.TyStafflistService;
import com.tfzq.wechatmsg.TemplateData;
import com.tfzq.wechatmsg.WxTemplateMsgUtil;
import com.tfzq.wechatmsg.WxTokenUtil;
import com.tfzq.wechatmsg.WxUserUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="sendWxMsgJobHandler")
@Component
public class SendWxMsgJobHandler extends IJobHandler {	
    private Logger logger = LoggerFactory.getLogger(SendWxMsgJobHandler.class);
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private TyActivityStaffService tyActivityStaffService;
	@Autowired
	private TyStafflistService tyStafflistService;
    @Autowired
    private RedisUtil redisService;

    @Value("${tfty.wx.appId}")
    private String appId;
    @Value("${tfty.wx.appSecret}")
    private String appSecret;
    @Value("${tfty.wx.regTempId}")
    private String regTempId;

    @Value("${tfty.miniprogram.appid}")
    private String miniprogramAppid;
    @Value("${tfty.miniprogram.pagepath}")
    private String miniprogramPagepath;
    
    private WxTokenUtil tokenUtil;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		// 定时任务开关的状态为开时才执行定时任务
		boolean result = sendWxmsg();
		if(result){			
			return SUCCESS;
		}else{
			return FAIL;
		}
	}


	/**
	 * 发送短信
	 * 
	 * @param date
	 */
	private boolean sendWxmsg() {

		Map<String, Object> params = new HashMap<String, Object>();		
		List<TyActivity> results = tyActivityService.findNotifyRecords(params);
		XxlJobLogger.log("发送Activity活动通知 ，条数：" + results.size());
		tokenUtil = new WxTokenUtil();
		tokenUtil.setAppId(appId);
		tokenUtil.setAppSecret(appSecret);
		tokenUtil.setRedisService(redisService);

		Set<String> pushedUser = new HashSet<String>();
		boolean result = true;
		for(TyActivity act:results){
			//发送给联系人
			sendSingMsg(act,act.getContactId(),pushedUser);
			
			Map<String, Object> params2 = new HashMap<String, Object>();		
			params2.put("activityId", act.getId());
			List<TyActivityStaff> staffs = tyActivityStaffService.findByCondition(params2);
			//发送给参与人
			for(TyActivityStaff staff:staffs){
				boolean b = sendSingMsg(act,staff.getId(),pushedUser);
				if(!b){
					result=false;
				}
			}
			//更改状态
			TyActivity param = new TyActivity();
			param.setId(act.getId());
			param.setNotifyFlag("1");
			tyActivityService.update(param);
			//
			String key ="ty:tyActivity:"+act.getId();
			redisService.delete(key);
		}
		return result;
	}
	
	private boolean sendSingMsg(TyActivity act,String staffId,Set<String> pushedUser){
		if(pushedUser.contains(staffId)){
			return true;
		}else{
			pushedUser.add(staffId);
		}
		boolean result = true;
		TyStafflist staff =tyStafflistService.selectByPrimaryKey(staffId);
		if(StringUtils.isNotBlank(staff.getWxOpenid())){
			String openId=staff.getWxOpenid();
			
			WxUserUtil userUtil = new WxUserUtil();
			userUtil.setTokenUtil(tokenUtil);
			// 用户是否订阅该公众号标识 (0代表此用户没有关注该公众号 1表示关注了该公众号)
			Integer state = userUtil.subscribeState(openId);
			// 绑定了微信并且关注了服务号的用户 , 注册成功-推送注册短信
			if (state == 1) {
				Map<String, TemplateData> param = new HashMap<>();
				param.put("first", new TemplateData("您有协作活动需要处理！", "#696969"));
				param.put("keyword1", new TemplateData(act.getCreateName(), "#696969"));
				param.put("keyword2", new TemplateData(String.valueOf(act.getBeginDate()), "#696969"));
				param.put("remark", new TemplateData(act.getTitle(), "#696969"));
				// 调用发送微信消息给用户的接口
				JSONObject miniprogram = new JSONObject();
				if(StringUtils.isNotBlank(miniprogramAppid)&&StringUtils.isNotBlank(miniprogramPagepath)){					
					miniprogram.put("appid", miniprogramAppid);
					miniprogram.put("pagepath", miniprogramPagepath);
				}

				WxTemplateMsgUtil templateMsgUtil = new WxTemplateMsgUtil();
				templateMsgUtil.setTokenUtil(tokenUtil);
				String code = templateMsgUtil.sendWechatMsgToUser(openId, regTempId, "",miniprogram, "#000000", templateMsgUtil.packJsonmsg(param));
				if("success".equals(code)){
					result = true;
				}else{
					XxlJobLogger.log("员工["+staff.getStaffName()+"]推送微信消息失败");
					result = false;
				}
			}
		}else{
			XxlJobLogger.log("员工微信账号未配置["+staff.getStaffName()+"]");
		}
		return result;
	}
}