package org.ibase4j.core.support.ftp;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.exception.FtpException;
import org.ibase4j.core.util.PropertiesUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Java Secure Channel
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
public class SftpClient {
	private Logger logger = LogManager.getLogger();
	private String host;
	private int port;
	private String userName;
	private String password;
	private int timeout;
	private int aliveMax;

	private Session session = null;
	private ChannelSftp channel = null;

	public SftpClient(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		init();
	}

	public void connect() throws JSchException {
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(userName, host, port);
		// 根据用户名，主机ip，端口获取一个Session对象
		if (password != null) {
			session.setPassword(password); // 设置密码
		}
		Properties config = new Properties();
		config.put("userauth.gssapi-with-mic", "no");
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.setServerAliveCountMax(aliveMax);
		session.connect(); // 通过Session建立链接
		channel = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		logger.info("SSH Channel connected.");
	}

	public SftpClient init() {
		timeout = PropertiesUtil.getInt("sftp.timeout");
		aliveMax = PropertiesUtil.getInt("sftp.aliveMax");
		try {
			connect();
			logger.info("用户[" + userName + "]登陆[" + host + "]成功.");
		} catch (JSchException e) {
			logger.error("用户[" + userName + "]登陆[" + host + "]失败.");
			e.printStackTrace();
		}
		return this;
	}

	public void disconnect() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
			logger.info("SSH Channel disconnected.");
		}
	}

	/** 发送文件 */
	public void put(String src, String dst) {
		try {
			channel.put(src, dst, new FileProgressMonitor());
		} catch (SftpException e) {
			logger.error("上传文件失败." + src + "," + dst);
			throw new FtpException("", e);
		}
	}

	/** 获取文件 */
	public void get(String src, String dst) {
		try {
			channel.get(src, dst, new FileProgressMonitor());
		} catch (SftpException e) {
			logger.error("下载文件失败.");
			throw new FtpException("", e);
		}
	}
}
