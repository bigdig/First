package com.xxl.job.executor.service.jobhandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.tfzq.tycrm.entity.TyMessagelog;
import com.tfzq.tycrm.service.SmsService;
import com.tfzq.tycrm.service.impl.SmsServiceImpl;
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
@JobHandler(value="sendSmsMsgJobHandler")
@Component
public class SendSmsMsgJobHandler extends IJobHandler {	
	@Autowired
	private SmsServiceImpl smsServiceImpl;
	@Autowired
	private SmsService smsService;
	private static boolean running = false;
    @Value("${sms.batchsize}")
    private String batchSize ;//= PropertiesUtil.getString("sms.msg.head");

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		
		if (running) {
			XxlJobLogger.log("发送短信任务运行中, 本次将退出! ");
			return SUCCESS;
		} else {
			running = true;
		}
		
		// 定时任务开关的状态为开时才执行定时任务
		try{
			sendSms();			
		}catch(Exception e){
			running = false;
			throw e;
		}
		return SUCCESS;
	}

	

	/**
	 * 发送短信
	 * 
	 * @param date
	 */
	private void sendSms() {
		Map<String,Object> params = new HashMap<String,Object>();
		//String batchSize = PropertiesUtil.getString("sms.batchsize");
		int pageSize = StringUtils.isEmpty(batchSize)?1000:Integer.parseInt(batchSize);
		params.put("msgType", "sms");
		params.put("sendFlag", "0");
		params.put("pageSize", pageSize);
//		params.put("auditFlag", "1");
		PageInfo<TyMessagelog> messages = smsService.findByPage(params);
		if(!messages.getList().isEmpty()){
			for(TyMessagelog msg:messages.getList()){
				boolean succ = smsServiceImpl.sendValidSms(msg.getReceiverTel(),msg.getMsgContent());
				if(succ){
					msg.setSendFlag("1");
					msg.setRemark("发送成功");
				}else{
					msg.setSendFlag("0");
					msg.setRemark("发送失败");
				}
				smsService.update(msg);
			}
			//sendSms();
		}
		XxlJobLogger.log("发送短信任务条数："+messages.getList().size()+"! ");

	}

}