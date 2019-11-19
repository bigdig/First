package org.ibase4j.scheduler.task.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.model.generator.TyMessagelog;
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
public class SendMsgJobImpl implements TaskScheduler {
	@Autowired
	private TyMessagelogProviderImpl messagelogProviderImpl;	
	@Autowired
	private SmsProviderImpl smsProviderImpl;
	private static Logger logger = LoggerFactory.getLogger(SendMsgJobImpl.class);
	private static boolean running = false;

	public void execute() {
		if (running) {
			logger.info("发送短信任务运行中, 本次将退出! ");
			return;
		} else {
			running = true;
		}
		
		// 定时任务开关的状态为开时才执行定时任务
		sendSms();

		running = false;
	}

	/**
	 * 发送短信
	 * 
	 * @param date
	 */
	private void sendSms() {
		Map<String,Object> params = new HashMap<String,Object>();
		String batchSize = PropertiesUtil.getString("sms.batchsize");
		int pageSize = StringUtils.isEmpty(batchSize)?1000:Integer.parseInt(batchSize);
		params.put("msgType", "sms");
		params.put("sendFlag", "0");
		params.put("pageSize", pageSize);
		params.put("auditFlag", "1");
		PageInfo<TyMessagelog> messages = messagelogProviderImpl.query(params);
		if(!messages.getList().isEmpty()){
			for(TyMessagelog msg:messages.getList()){
				//System.out.println(msg.getReceiverTel());
				boolean succ = smsProviderImpl.sendValidSms(msg.getReceiverTel(),msg.getMsgContent());
				if(succ){
					msg.setSendFlag("1");
					msg.setRemark("发送成功");
				}else{
					msg.setSendFlag("0");
					msg.setRemark("发送失败");
				}
				messagelogProviderImpl.update(msg);
			}
			//sendSms();
		}
	}

	
	
	public static void main(String[] args) {
	}

}