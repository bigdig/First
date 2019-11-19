package org.ibase4j.provider.impl;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
public class SendEmailProviderImpl {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	private Properties props = new Properties();
	private Session session;
	String host;
	String from;
	String name;
	String password;
	String authorisation;

	public boolean init() {
		try {
			host = PropertiesUtil.getString("email.smtp.host");
			from = PropertiesUtil.getString("email.send.from");
			name = PropertiesUtil.getString("email.user.name");
			password = PropertiesUtil.getString("email.user.password");
			authorisation = PropertiesUtil.getString("email.authorisation.code");

			// 开启debug调试
			props.setProperty("mail.debug", "true");
			// 发送服务器需要身份验证
			props.setProperty("mail.smtp.auth", "true");
			// 设置邮件服务器主机名
			props.setProperty("mail.host", host);
			// 发送邮件协议名称
			props.setProperty("mail.transport.protocol", "smtp");

			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
			session = Session.getInstance(props);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean sendEmail(String email, String msgContent) {
		try {
	        Message msg = new MimeMessage(session);
	        msg.setSubject("邮件内容");
	        msg.setText(msgContent);
			String[] f = from.split(",");
			if (f.length > 1) {
				from = javax.mail.internet.MimeUtility.encodeText(f[0])+ "<" + f[1] + ">";
			}
			msg.setFrom(new InternetAddress(from)); // 设置发信人

	        //msg.setFrom(new InternetAddress(from));

	        Transport transport = session.getTransport();
	        transport.connect(host, from, authorisation);

	        transport.sendMessage(msg, new Address[] { new InternetAddress(email) });
	        transport.close();

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
