package com.xxl.job.executor.service.jobhandler;

import java.util.ArrayList;
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
import com.net263.contact.Test;
import com.net263.contact.util.HttpClientUtils;
import com.net263.contact.util.JsonUtils;
import com.net263.contact.vo.Contact;
import com.net263.contact.vo.ContactDelData;
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
@JobHandler(value = "pushNet263")
@Component
public class PushNet263MeetingJobHandler extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(PushNet263MeetingJobHandler.class);
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private RedisUtil redisUtil;
    @Value("${net263.url}")
    private String url;
    @Value("${net263.groupId}")
    private String groupId;
    @Value("${net263.account}")
    private String account;
    @Value("${net263.password}")
    private String password;

    
    @Override
	public ReturnT<String> execute(String param) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("platFlag", "net263");
		List<TyOrgcustomer> results = tyOrgcustomerService.findByCondition(params);
		XxlJobLogger.log("推送客户数据到电话会议，条数：" + results.size());
		boolean succ = true;
		String token="";
		if(results.size()>0){
			token =  getLoginToken();
		}
		for (TyOrgcustomer lc : results) {
			//白名单客户
			//if("3".equals(lc.getCustStatus())){
				String remark ="成功";
				String dealFlag="1";
				try {
					String errcode ="";
					if("1".equals(lc.getDeleteFlag())){
						errcode = deleteContact(token,lc);
						
					}else{
						errcode = addcontact(token,lc);
						XxlJobLogger.log("推送客户数据到电话会议 addBatch ");
					}
					
					XxlJobLogger.log("推送客户数据到电话会议 errcode : " + errcode);
					
					if(StringUtils.equals("0", errcode)){
						remark ="成功";
					}else if(StringUtils.equals("-300002", errcode)){
						//进行一次更新操作
						String updateErrcode = updatecontact(token, lc);
						//更新操作仍然出错
						if(!StringUtils.equals("0", updateErrcode)) {
							succ = false;
							dealFlag="2";
							remark="["+errcode+"]更新客户[" + lc.toString() + "]到Net 263失败：[部分联系人群组不存在同时有电话号码重复]["+updateErrcode+"]";
							XxlJobLogger.log(remark);
						}
					}else{
						//进行一次更新操作
						String updateErrcode = updatecontact(token, lc);
						if(!StringUtils.equals("0", updateErrcode)){
							succ = false;
							dealFlag="2";
							remark ="["+errcode+"]更新客户[" + lc.toString() + "]到Net 263失败：ERRORCODE[" + updateErrcode+"]";
							XxlJobLogger.log(remark);
						}
					}

				} catch (Exception e) {
					succ = false;
					e.printStackTrace();
					remark = e.getMessage();
					dealFlag="2";
					XxlJobLogger.log("推送客户[" + lc.toString() + "]到Net 263失败：" + e.getMessage());
				}
				TyOrgcustomer result = new TyOrgcustomer();
				result.setPushId(lc.getPushId());
				result.setRemark(remark);
				result.setDealFlag(dealFlag);
				tyOrgcustomerService.update(result);
			}
		//}
		if(succ){
			return SUCCESS;
		}else{
			return FAIL;
		}
	}

    public String getLoginToken() throws Exception {
        //TODO: Test goes here...
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tag", "1");
        Map<String, String> data = new HashMap<String, String>();
        data.put("account", account);
        data.put("password", password);
        map.put("data", data);
        String test = HttpClientUtils.sendPostBodyRequest("http://"+ url +"/api/auth", JsonUtils.mapToString(map));
        JSONObject result = JSON.parseObject(test);
        logger.info(result.toJSONString());
        return result.getJSONObject("data").getString("token");
    }

    public String addcontact(String token,TyOrgcustomer customer) throws Exception {
        //TODO: Test goes here...
        List<Contact> datas = new ArrayList<Contact>();
        Map<String, String> heard = new HashMap<>();

        Contact contactData = new Contact();
        contactData.setCustsysid(customer.getId());
        contactData.setCustgrpid(groupId);
        contactData.setName(customer.getCustName());
        // contactData.setEmail(customer.getCustEmail());
        contactData.setEmail("");
        contactData.setTelephone(trimLine(StringUtils.isNotEmpty(customer.getCustMobile())?customer.getCustMobile():customer.getCustTel()));
        contactData.setTelephone1(trimLine(StringUtils.isNotEmpty(customer.getCustTel())?customer.getCustTel():customer.getCustMobile()));
        contactData.setDescription("1");
        contactData.setOrganization(StringUtils.isNotEmpty(customer.getOrgName())?customer.getOrgName():"");

        datas.add(contactData);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tag", "1");
        map.put("data", datas);

        heard.put("token", token);
        
        XxlJobLogger.log("call addBatch :" + JsonUtils.mapToString(map));
        
        String test = HttpClientUtils.sendPostBodyRequest("http://"+ url +"/api/contact/addBatch", JsonUtils.mapToString(map), heard);
        JSONObject result = JSON.parseObject(test);
        logger.info(result.toJSONString());
        
        XxlJobLogger.log("response addBatch :" + result.toJSONString());
        
        return result.getInteger("errcode").toString();
    }
    
    private String trimLine(String tel){
//    	return StringUtils.remove(tel, "-");
    	return StringUtils.replace(tel, "-","",1);    	
    }
    public String deleteContact(String token,TyOrgcustomer customer) throws Exception {
        Map<String, String> heard = new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        heard.put("token", token);

        ContactDelData contactDelData = new ContactDelData();
        //contactDelData.setCustsysid(customer.getId());
        contactDelData.setTelephone(StringUtils.isNotEmpty(customer.getCustMobile())?customer.getCustMobile():customer.getCustTel());
        //contactDelData.setCustgrpid(groupId);
        map.put("data", contactDelData);
        
        XxlJobLogger.log("call delete :" + JsonUtils.mapToString(map));
        
        String test = HttpClientUtils.sendPostBodyRequest("http://"+ url +"/api/contact/del", JsonUtils.mapToString(map), heard);
        JSONObject result = JSON.parseObject(test);
        logger.info(result.toJSONString());
        
        XxlJobLogger.log("response delete :" + result.toJSONString());
        
        return result.getInteger("errcode").toString();
    }
    
    public String updatecontact(String token,TyOrgcustomer customer) throws Exception {
        List<Contact> datas = new ArrayList<Contact>();
        Map<String, String> heard = new HashMap<>();

        Contact contactData = new Contact();
        contactData.setCustsysid(customer.getId());
        contactData.setCustgrpid(groupId);
        contactData.setName(customer.getCustName());
        // contactData.setEmail(customer.getCustEmail());
        contactData.setEmail("");
        contactData.setTelephone(trimLine(StringUtils.isNotEmpty(customer.getCustMobile())?customer.getCustMobile():customer.getCustTel()));
        contactData.setTelephone1(trimLine(StringUtils.isNotEmpty(customer.getCustTel())?customer.getCustTel():customer.getCustMobile()));
        contactData.setDescription("1");
        contactData.setOrganization(StringUtils.isNotEmpty(customer.getOrgName())?customer.getOrgName():"");

        datas.add(contactData);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tag", "1");
        map.put("data", datas);
        heard.put("token", token);
        
        XxlJobLogger.log("call update :" + JsonUtils.mapToString(map));
        String test = HttpClientUtils.sendPostBodyRequest("http://"+ url +"/api/contact/update", JsonUtils.mapToString(map), heard);
        JSONObject result = JSON.parseObject(test);
        logger.info(result.toJSONString());
        
        XxlJobLogger.log("response update :" + result.toJSONString());
        
        return result.getInteger("errcode").toString();
    }

}