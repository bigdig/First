package com.xxl.job.executor.service.jobhandler;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.tfzq.tycrm.entity.TyOrgcustomer;
import com.tfzq.tycrm.service.TyOrgcustomerService;
import com.tfzq.utils.HttpClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

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
@JobHandler(value = "pushJmcj")
@Component
public class PushJMCJMeetingJobHandler extends IJobHandler {
	private Logger logger = LoggerFactory.getLogger(PushJMCJMeetingJobHandler.class);
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
//	@Autowired
//	private TyStafflistService tyStafflistService;
//	@Autowired
//	private RedisUtil redisUtil;
	@Value("${jmcj.url}")
	private String url;
	@Value("${jmcj.channelsignpwd}")
	private String channelsignpwd;
	@Value("${jmcj.app}")
	private String app;
	@Value("${jmcj.mod}")
	private String mod;
	@Value("${jmcj.act}")
	private String act;

	@Override
	public ReturnT<String> execute(String param) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("platFlag", "jmcj");
		List<TyOrgcustomer> results = tyOrgcustomerService.findByCondition(params);
		XxlJobLogger.log("推送客户数据到进门财经，条数：" + results.size());
		boolean succ = true;
		for (TyOrgcustomer lc : results) {
			// 白名单客户
			String remark = "成功";
			String dealFlag = "1";
//			if ("3".equals(lc.getCustStatus())) {
				try {
					if ("1".equals(lc.getDeleteFlag())) {
						String result = deleteContact(getContactStr(lc));
						JSONObject r = JSON.parseObject(result);
						String errorcode = r.getString("errorcode");
						if(!errorcode.equals("0")){
							dealFlag = "2";
							remark = r.getString("errordesc");
						}
					} else {
						String result = deleteContact(getContactStr(lc));
						result = addContact(getContactStr(lc));
						JSONObject r = JSON.parseObject(result);
						String errorcode = r.getString("errorcode");
						if(!errorcode.equals("0")){
							dealFlag = "2";
							remark = r.getString("errordesc");
						}
					}
				} catch (Exception e) {
					succ = false;
					e.printStackTrace();
					remark = e.getMessage();
					dealFlag = "2";
					XxlJobLogger.log("推送客户[" + lc.toString() + "]到进门财经失败：" + e.getMessage());
				}
//			}else{
//				remark="非白名单客户，不同步进门财经";
//			}
			TyOrgcustomer result = new TyOrgcustomer();
			result.setPushId(lc.getPushId());
			result.setRemark(remark);
			result.setDealFlag(dealFlag);
			tyOrgcustomerService.update(result);
		}
		if (succ) {
			return SUCCESS;
		} else {
			return FAIL;
		}
	}
	public String getContactStr(TyOrgcustomer customer){
		JSONObject c = new JSONObject();
		c.put("name", " ");
		String phone = customer.getCustMobile();
		String areacode = "+86";
		String[] prefixArr = {"00852-","852-","（00852）","00852","852","0065-","65-","0065","65"};
		for (String f:prefixArr){
			if(phone.startsWith(f)){
				phone = StringUtils.removeStart(phone, f);
				if(f.contains("852")){
					areacode = "+852";
				}
				if(f.contains("65")){
					areacode = "+65";
				}
				break;
			}
		}
		c.put("phone", phone);
		c.put("areacode", areacode);
		c.put("company", " ");
		c.put("occupation", " ");

		JSONArray results = new JSONArray();
		results.add(c);
		return results.toJSONString();
	}

	public String addContact(String whitelist) throws Exception {
		return sendReq("0", whitelist);
	}

	public String deleteContact(String whitelist) throws Exception {
		return sendReq("1", whitelist);
	}

	public String sendReq(String opt, String whitelist) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String channelsigntime = String.valueOf(new Date().getTime());
		String channelsign = getMD5("channelsigntime=" + channelsigntime + "&channelsignpwd=" + channelsignpwd);
		params.put("app", app);
		params.put("mod", mod);
		params.put("act", act);
		params.put("channelsigntime", channelsigntime);
		params.put("channelsign", channelsign);
		params.put("opt", opt);
		params.put("whitelist", whitelist);
//		logger.info("channelsigntime"+channelsigntime+",channelsign"+channelsign+",whitelist"+whitelist);
		String results = HttpClient.post(url, params);
		logger.info(results);
		return results;
	}

	/*
	 * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
		
}