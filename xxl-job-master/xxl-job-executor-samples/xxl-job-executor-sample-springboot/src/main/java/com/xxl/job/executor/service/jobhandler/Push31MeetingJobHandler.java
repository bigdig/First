package com.xxl.job.executor.service.jobhandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddfc.service.RedisUtil;
import com.tfzq.tycrm.entity.TyOrgcustomer;
import com.tfzq.tycrm.entity.TyStafflist;
import com.tfzq.tycrm.service.TyOrgcustomerService;
import com.tfzq.tycrm.service.TyStafflistService;
import com.tfzq.utils.Constants;
import com.tfzq.utils.HttpClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.core.config.XxlJobConfig;

/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤： 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，
 * 注解value值对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "push31Meeting")
@Component
public class Push31MeetingJobHandler extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(Push31MeetingJobHandler.class);
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private RedisUtil redisUtil;
    @Value("${31meijia.appid}")
    private String appid;
    @Value("${31meijia.appsecret}")
    private String secret;
    @Value("${31meijia.api.token}")
    private String tokenurl;
    @Value("${31meijia.api.member}")
    private String member;
//	String appid = PropertiesUtil.getString("31meijia.appid");
//	String secret = PropertiesUtil.getString("31meijia.appsecret");
//	String tokenurl = PropertiesUtil.getString("31meijia.api.token");
//    PropertiesUtil.getString("31meijia.api.member")
	@Override
	public ReturnT<String> execute(String param) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("custStatus", "31");
		params.put("platFlag", "31");
		List<TyOrgcustomer> results = tyOrgcustomerService.findByCondition(params);
		XxlJobLogger.log("推送客户数据到31会议，条数：" + results.size());
		boolean succ = true;
		for (TyOrgcustomer lc : results) {
			String remark =null;
			String dealFlag="1";
			try {
				update31Cust(lc);
			} catch (Exception e) {
				succ = false;
				e.printStackTrace();
				remark = e.getMessage();
				dealFlag="2";
				XxlJobLogger.log("推送客户[" + lc.toString() + "]到31会议失败：" + e.getMessage());
			}
			TyOrgcustomer result = new TyOrgcustomer();
			result.setPushId(lc.getPushId());
			result.setRemark(remark);
			result.setDealFlag(dealFlag);
			tyOrgcustomerService.update(result);
		}
		if(succ){
			return SUCCESS;
		}else{
			return FAIL;
		}
	}

	public void update31Cust(TyOrgcustomer custBean) throws Exception {
		if (StringUtils.isNotEmpty(custBean.getSalerId())) {
			TyStafflist staff = tyStafflistService.selectByPrimaryKey(custBean.getSalerId());
			custBean.setServiceSaler(staff == null ? "" : staff.getStaffName());
			custBean.setSalerMobile(staff == null ? "" : staff.getTel());
		}
		custBean.setCustCat(transCustCat(custBean.getCustCat()));
		custBean.setOrgLevel(transOrgLevel(custBean.getOrgLevel()));
		boolean flag = true;
		while (flag) {
			flag = false;
			String access_key = appid + "_access_token";
			String access_token = (String) redisUtil.get(access_key);
			if (!StringUtils.isNotBlank(access_token)) {
				Map<String, String> pa = new HashMap<String, String>();
				pa.put("appid", appid);
				pa.put("secret", secret);
				String result1 = HttpClient.get(tokenurl, pa);
				JSONObject jsonParams1 = JSONObject.parseObject(result1);
				access_token = jsonParams1.getString("access_token");
				redisUtil.set(access_key, access_token);
				redisUtil.expire(access_key, 7200L);
			}
			String url = member + "save-associator";
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", access_token);
			params.put("RealName", custBean.getCustName());
			params.put("Mobile", custBean.getCustMobile());
			params.put("Email", custBean.getCustEmail() == null ? "" : custBean.getCustEmail());
			params.put("PosStatusEx", custBean.getTitle());
			params.put("Company", custBean.getOrgName());
			params.put("LevelId", custBean.getOrgLevel());
			// JSONObject jsonParams = new JSONObject();
			// jsonParams.put("Extra907828220", custBean.getServiceSaler());
			// jsonParams.put("Extra907828221", custBean.getSalerMobile());
			params.put("ExtraFields[Extra907828220]", custBean.getServiceSaler());
			params.put("ExtraFields[Extra907828221]", custBean.getSalerMobile());
			params.put("ExtraFields[Extra1109120496]", custBean.getCustCat());

			logger.info("接口调用入参对象:" + custBean.toString());
			logger.info("接口调用入参map:" + params.toString());
			String retStr = HttpClient.post(url, params);
			logger.info("接口调用返回结果:" + retStr);
			JSONObject json = null;
			try {
				json = JSON.parseObject(retStr);
			} catch (Exception e) {
				// 返回了非json的Long型结果，调用成功
				return;
			}
			String errcode = json.getString("errcode");
			if (StringUtils.isNotBlank(errcode) && (!errcode.equals("0"))) {
				if (errcode.equals("40001")) { // token失效
					flag = true;
					redisUtil.delete(access_key);
				} else {
					throw new BusinessException("接口调用失败:" + json.getString("errmsg"));
				}
			}
		}
	}

	private String transOrgLevel(String orgLevel) {
		 if ("1".equals(orgLevel)) {
			return Constants.CUST_LEVEL_1;
		} else if ("2".equals(orgLevel)) {
			return Constants.CUST_LEVEL_2;
		} else if ("3".equals(orgLevel)) {
			return Constants.CUST_LEVEL_3;
		}else if ("4".equals(orgLevel)) {
			return Constants.CUST_LEVEL_4;
		} else {
			return Constants.CUST_LEVEL_OTHER;
		}
	}

	private String transCustCat(String cust_cat_str) {
		if ("1".equals(cust_cat_str)) {
			return "保险资管";
		} else if ("2".equals(cust_cat_str)) {
			return "公募";
		} else if ("3".equals(cust_cat_str)) {
			return "券商资管";
		} else if ("4".equals(cust_cat_str)) {
			return "券商自营";
		} else if ("5".equals(cust_cat_str)) {
			return "私募";
		} else if ("6".equals(cust_cat_str)) {
			return "信托";
		}
		// else if ("7".equals(cust_cat_str)) {
		// return "其他";
		// }
		return "其他";
	}
}