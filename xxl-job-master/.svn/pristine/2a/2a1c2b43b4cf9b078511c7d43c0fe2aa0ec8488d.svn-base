package com.tfzq.tycrm.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyMessagelog;
import com.tfzq.tycrm.service.SmsService;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
public class SmsServiceImpl extends BaseServiceImpl<TyMessagelog> implements SmsService{
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Value("${sms.debug}")
	private String isDebug ;//= PropertiesUtil.getString("sms.debug");
	@Value("${sms.url}")
	private String url ;//= PropertiesUtil.getString("sms.url");
	@Value("${sms.sn}")
	private String sn ;//= PropertiesUtil.getString("sms.sn");
	@Value("${sms.ext}")
	private String ext ;//= PropertiesUtil.getString("sms.ext");
	@Value("${sms.md5pwd}")
	private String md5pwd ;//= PropertiesUtil.getString("sms.md5pwd");
	@Value("${sms.msg.suffix}")
	private String suffix ;//= PropertiesUtil.getString("sms.msg.suffix");
    @Value("${sms.msg.head}")
    private String head ;//= PropertiesUtil.getString("sms.msg.head");
	
	public boolean sendValidSms(String phone,String msgContent) {
		// TODO Auto-generated method stub
		
//		logger.info(phone+":"+msgContent);
		boolean code = true;;
		if(StringUtils.equals("1", isDebug)){
			//code = true;
			return code;
		}
		
		SmsClient client;
		try {
			client = new SmsClient(url,sn,md5pwd);
			System.out.println(msgContent);
			System.out.println(suffix);
			String content = java.net.URLEncoder.encode(head+" "+msgContent+" "+suffix,  "utf-8");  
			String result_mt = client.mdsmssend(phone, content, ext, "", "", "");
			System.out.println(result_mt);
			logger.debug(result_mt);
			if(result_mt.startsWith("-")){
				code =false;
			}
		} catch (UnsupportedEncodingException e) {
			code =false;
			e.printStackTrace();
		} catch (IOException e) {
			code = false;
			e.printStackTrace();
		}
		
		return code;
	}

}
