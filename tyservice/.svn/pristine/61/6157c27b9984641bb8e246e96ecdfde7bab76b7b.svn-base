package com.tfzq.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import com.tfzq.pb.provider.SmsProvider;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
//@DubboService(interfaceClass = SmsProvider.class)
@Service
public class SmsProviderImpl implements SmsProvider {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public String sendValidSms(String phone) {
		// TODO Auto-generated method stub
		String isDebug = PropertiesUtil.getString("sms.debug");
		String url = PropertiesUtil.getString("sms.url");
		String sn = PropertiesUtil.getString("sms.sn");
		String ext = PropertiesUtil.getString("sms.ext");
		String md5pwd = PropertiesUtil.getString("sms.md5pwd");
		String validMsg = PropertiesUtil.getString("sms.msg.validcode");
		String code = String.valueOf(RandomUtils.nextInt(1000, 9999));
		if(StringUtils.equals("1", isDebug)){
			code = "1111";
			return code;
		}
		validMsg = StringUtils.replace(validMsg, "[s]", code);
		logger.info(validMsg);
		SmsClient client;
		try {
			client = new SmsClient(url,sn,md5pwd);
			String content = java.net.URLEncoder.encode(validMsg,  "utf-8");  
			String result_mt = client.mdsmssend(phone, content, ext, "", "", "");
			if(result_mt.startsWith("-")){
				code =null;
			}
		} catch (UnsupportedEncodingException e) {
			code =null;
			e.printStackTrace();
		} catch (IOException e) {
			code =null;
			e.printStackTrace();
		}
		return code;
	}

}
