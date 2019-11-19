package org.ibase4j.provider.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
public class SmsProviderImpl {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	
	public boolean sendValidSms(String phone,String msgContent) {
		// TODO Auto-generated method stub
		String isDebug = PropertiesUtil.getString("sms.debug");
		String url = PropertiesUtil.getString("sms.url");
		String sn = PropertiesUtil.getString("sms.sn");
		String ext = PropertiesUtil.getString("sms.ext");
		String md5pwd = PropertiesUtil.getString("sms.md5pwd");
		String suffix = PropertiesUtil.getString("sms.msg.suffix");
		String head = PropertiesUtil.getString("sms.msg.head");
		
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
