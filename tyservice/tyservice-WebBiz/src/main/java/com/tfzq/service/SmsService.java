package com.tfzq.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.tfzq.pb.provider.SmsProvider;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:16:07
 */
@Service
public class SmsService {
    protected Logger logger = LogManager.getLogger();

	@DubboReference
	private SmsProvider smsProvider;

	public String sendShortMsg(String phone){
		String sms = smsProvider.sendValidSms(phone);
		return sms;
	}

	public boolean validSms(String sms,String captcha){
		return sms!=null&&captcha!=null&&sms.equals(captcha);
	}

}
