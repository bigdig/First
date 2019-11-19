package org.ibase4j.scheduler.task.impl;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.support.email.EmailSender;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.model.generator.TyMessagelog;
import org.ibase4j.provider.impl.SendEmailProviderImpl;
import org.ibase4j.provider.impl.SmsProviderImpl;
import org.ibase4j.provider.impl.TyMessagelogProviderImpl;
import org.ibase4j.scheduler.task.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;

/**
 * 定时任务
 */
@Repository
public class SendEmailJobImpl implements TaskScheduler {
	@Autowired
	private TyMessagelogProviderImpl messagelogProviderImpl;	
//	@Autowired
//	private SmsProviderImpl smsProviderImpl;
//	@Autowired
//	private SendEmailProviderImpl sendEmailProviderImpl;
	private static Logger logger = LoggerFactory.getLogger(SendEmailJobImpl.class);
	private static boolean running = false;

	public void execute() {
		if (running) {
			logger.info("发送邮件任务运行中, 本次将退出! ");
			return;
		} else {
			running = true;
		}
		
		// 定时任务开关的状态为开时才执行定时任务
		try {
			sendEmail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		running = false;
	}

	/**
	 * 发送短信
	 * 
	 * @param date
	 * @throws GeneralSecurityException 
	 * @throws Exception 
	 */
	private void sendEmail() throws  Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		String batchSize = PropertiesUtil.getString("email.batchsize");
		int pageSize = StringUtils.isEmpty(batchSize)?1000:Integer.parseInt(batchSize);
		params.put("msgType", "email");
		params.put("sendFlag", "0");
		params.put("pageSize", pageSize);
		params.put("auditFlag", "1");
		PageInfo<TyMessagelog> messages = messagelogProviderImpl.query(params);
		if(!messages.getList().isEmpty()){

			for(TyMessagelog msg:messages.getList()){
				String isDebug = PropertiesUtil.getString("email.debug");
				boolean succ=true;
				if("0".equals(isDebug)){
					EmailSender emailSender = new EmailSender("");
					emailSender.setNamePass("", "", "");
					emailSender.createMimeMessage();
					emailSender.setFrom("");
					emailSender.setSubject(msg.getTitle());
					emailSender.setBody(msg.getMsgContent());
					emailSender.setTo(msg.getReceiverEmail());
					if(StringUtils.isNotBlank(msg.getFilePath())){
						String filePathStr = msg.getFilePath();
						//TODO
						String[] filePathArray = filePathStr.split(",");
						for (String filePath : filePathArray) {
							emailSender.addFileAffix(PropertiesUtil.getString("ty.file.rootdir")+filePath);
						}
					}
					succ = emailSender.sendout();
				}
				if(succ){
					msg.setSendFlag("1");
					msg.setRemark("发送成功");
				}else{
					msg.setSendFlag("0");
					msg.setRemark("发送失败");
				}
				messagelogProviderImpl.update(msg);
			}
		}
	}
	
	public static void main(String[] args){
		EmailSender emailSender = new EmailSender("");
		emailSender.setNamePass("", "", "");
		emailSender.createMimeMessage();
		emailSender.setFrom("彭涛,pengtao@tfzq.com");
		emailSender.setSubject("Tiel");
		emailSender.setBody("你好");
		emailSender.setTo("peng.tao@dangdaifintech.com");
		
		boolean succ = emailSender.sendout();

	}
}