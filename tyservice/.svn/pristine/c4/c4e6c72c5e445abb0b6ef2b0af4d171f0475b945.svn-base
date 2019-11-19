package com.tfzq.util;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.PropertiesUtil;

public class HtmlUtils {
	private static Logger logger = LogManager.getLogger(HtmlUtils.class);


	/**
	 * 返回邮件附件本地存储绝对路径
	 * @return
	 */
	public static String getMailAttachFullFilePath() {
		String currDate = DateUtil.getDate();
		return PropertiesUtil.getString("ty.file.rootdir") + File.separator + PropertiesUtil.getString("machine.node") + File.separator
				+ PropertiesUtil.getString("mail.attachment.dir") + File.separator + currDate;
	}
	
	/**
	 * 返回邮件附件URL相对路径
	 * @return
	 */
	public static String getRelativeMailAttachUrlPath() {
		String currDate = DateUtil.getDate();
		return "/" + PropertiesUtil.getString("machine.node") + "/" + PropertiesUtil.getString("mail.attachment.dir") + "/" + currDate;
	}
	
	/**
	 * 返回邮件附件访问URL全路径
	 * @return
	 */
	public static String getMailAttachFullUrlPath() {
		String currDate = DateUtil.getDate();
		return PropertiesUtil.getString("tyservice.host") + "/" + PropertiesUtil.getString("machine.node") + "/" + PropertiesUtil.getString("mail.attachment.dir")
				+ "/" + currDate;
	}
	
	/**
	 * 返回excel服务器临时路径
	 * @return
	 */
	public static String getExcelTmpFilePath() {
		return PropertiesUtil.getString("excel.tmp.dir");
	}

	/**
	 * 本地文件不存在，则尝试从另外的节点去下载
	 * @param url
	 * @return
	 */
	public static File getLocalFile(String url) {
		String rootDir = PropertiesUtil.getString("attachment.dir");
		String http = PropertiesUtil.getString("static.attach.url");
		String localHttp = PropertiesUtil.getString("local.attach.url");
		File thumbFile = new File(StringUtils.replace(url, http, rootDir));
		// 本地文件不存在，则尝试从另外的节点去下载
		if (!thumbFile.exists()) {
			try {
				downloadFile(StringUtils.replace(url, http, localHttp), StringUtils.replace(url, http, rootDir));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return thumbFile;
	}
	
	/**
	 * 从网络上下载图片，并按图片路径规则放在本地
	 * 
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static String downloadFile(String img) throws Exception {
		URL urlImg = new URL(img);
		// 构造连接
		HttpURLConnection conn = (HttpURLConnection) urlImg.openConnection();
		// 模拟浏览器
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		// 打开连接
		conn.connect();
		// 打开这个网站的输入流
		InputStream in = conn.getInputStream();
		logger.info("dowload begin :" + img);
		byte[] buff = IOUtils.toByteArray(in);
		String fileId = UUID.randomUUID().toString();
		String suffix = ".jpg";
		File file = new File(getMailAttachFullFilePath() + File.separator + fileId + suffix);
		img = getMailAttachFullUrlPath() + "/" + fileId + suffix;
		FileUtils.writeByteArrayToFile(file, buff);
		logger.info("dowload finished:" + img + " ,file byte size:" + buff.length);
		return img;
	}
	
	/**
	 * 访问内容图片分布式节点时，将其他结点图片下载到本地节点指定路径
	 * 
	 * @param img
	 * @param path
	 * @throws Exception
	 */
	public static void downloadFile(String img, String path) throws Exception {
		URL urlImg = new URL(img);
		// 构造连接
		HttpURLConnection conn = (HttpURLConnection) urlImg.openConnection();
		// 模拟浏览器
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		// 打开连接
		conn.connect();
		// 打开这个网站的输入流
		InputStream in = conn.getInputStream();
		byte[] buff = IOUtils.toByteArray(in);
		File file = new File(path);
		FileUtils.writeByteArrayToFile(file, buff);
	}
	
	
	/**
	 * 获取活动附件上传的绝对路径
	 */
	public static String getActivityAttachFullFilePath() {
		String currDate = DateUtil.getDate();
		return PropertiesUtil.getString("ty.file.rootdir") + File.separator + PropertiesUtil.getString("machine.node") + File.separator
				+ PropertiesUtil.getString("activity.attachment.dir") + File.separator + currDate;
	}
	
	/**
	 * 返回邮件附件URL相对路径
	 * @return
	 */
	public static String getActivityAttachRelativeFilePath() {
		String currDate = DateUtil.getDate();
		return "/" + PropertiesUtil.getString("machine.node") + "/" + PropertiesUtil.getString("activity.attachment.dir") + "/" + currDate;
	}

}
