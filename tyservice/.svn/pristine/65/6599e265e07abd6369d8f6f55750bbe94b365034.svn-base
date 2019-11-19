package org.ibase4j.core.support.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.exception.FtpException;

/**
 * FTP上传下载
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
public class FtpClient {
	private Logger logger = LogManager.getLogger(getClass());
	private static final byte[] LOCK = { 0 };
	private static FTPClient ftpClient = null;
	private Properties properties = null;
	private static final String FILELOCK = "Token.lock";
	private String encode = "GBK";//默认GBK

	public static void main(String[] args) throws Exception {
/*		System.getProperties().list(System.out);
		String encoding = System.getProperty("file.encoding");  
        System.out.println("Encoding:" + encoding); */ 
		String host = "10.105.70.25";
		int port = 21;
		String username = "yjq";
		String password = "123456";
		String localDnPath = "e:/attachData2/";
//		String remotePath = "";
		String remotePath = "/download/新建文件夹";
		FtpClient ftpClient = new FtpClient(host, port, username, password);

		// FTP上传文件
//		ftpClient.uploadFile("send", localUpPath);
		// FTP下载文件
//		ftpClient.downLoadFile(remotePath, localDnPath);
		String encode = "GBK";
//		String encode = "utf-8";
//		String encode = "ISO-8859-1";
		
		String remoteFileName = new String("彭涛1.txt".getBytes(encode),encode); 
		String localDnPath_gbk = new String(localDnPath.getBytes(encode),encode);
		String remotePath_iso = new String(remotePath.getBytes(encode),"ISO-8859-1");
		ftpClient.downloadFile(remoteFileName, localDnPath_gbk ,remotePath_iso);

		ftpClient.close();
	}

	public FtpClient() {
	}

	/**
	 * 初始化
	 * 
	 * @param host IP
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @throws FtpException
	 */
	public FtpClient(String host, int port, String username, String password) throws FtpException {
		Map<String, String> properties = new HashMap<String,String>();
		properties.put("FTPHOSTNAME", host);
		properties.put("FTPPORT", String.valueOf(port));
		properties.put("FTPUSERNAME", username);
		properties.put("FTPPASSWORD", password);
		setProperties(properties);
		init(host, port, username, password);
	}

	/**
	 * FPT登录
	 */
	public void open() {
		init(properties.getProperty("FTPHOSTNAME"), Integer.valueOf(properties.getProperty("FTPPORT")),
				properties.getProperty("FTPUSERNAME"), properties.getProperty("FTPPASSWORD"));
	}

	/**
	 * 获取FTP连接
	 * 
	 * @param server
	 * @param user
	 * @param password
	 * @return
	 * @throws FtpException
	 */
	private void init(String host, int port, String username, String password) throws FtpException {
		synchronized (LOCK) {
			if (ftpClient == null) {
				ftpClient = new FTPClient();
			}
			try {
				ftpClient.connect(host, port);// 连接FTP服务器
			} catch (Exception e) {
				throw new FtpException("FTP[" + host + ":" + port + "]连接失败!", e);
			}
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				try {
					ftpClient.login(username, password);
				} catch (Exception e) {
					throw new FtpException("FTP用户[" + username + "]登陆失败!", e);
				}
			} else {
				throw new FtpException("FTP连接出错!");
			}
			logger.info("用户[" + username + "]登陆[" + host + "]成功.");
			properties.setProperty("userName", username);
			properties.setProperty("hostName", host);
			try {
				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			} catch (Exception e) {
				logger.error("", e);
				throw new FtpException("FTP初始化出错!", e);
			}
		}
	}

	/**
	 * 关闭FTP客户端
	 * 
	 * @throws Exception
	 */
	public void close() throws FtpException {
		synchronized (LOCK) {
			try {
				ftpClient.logout();
			} catch (IOException e) {
				logger.error("", e);
				ftpClient = null;
				throw new FtpException("FTP退出登录出错!", e);
			}
			logger.info(
					"用户[" + properties.getProperty("userName") + "]退出登录[" + properties.getProperty("hostName") + "].");
		}
	}

	/**
	 * 上传
	 * 
	 * @param remotePath 上传目录
	 * @param localPath 本地目录
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(String remotePath, String localPath) throws FtpException {
		synchronized (LOCK) {
			File file = new File(localPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (!uploadFiles(files[i], remotePath)) {
					return false;
				}
			}
			return files.length > 0;
		}
	}

	/**
	 * 递归上传文件
	 * 
	 * @param localeFile 本地文件/目录 
	 * @param remotePath 上传目录
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFiles(File localeFile, String remotePath) throws FtpException {
		synchronized (LOCK) {
			FileInputStream fis = null;
			try {
				if (localeFile.isDirectory()) {
					boolean flag = false;
					ftpClient.makeDirectory(localeFile.getName());
					ftpClient.changeWorkingDirectory(localeFile.getName());
					logger.info("[" + localeFile.getAbsolutePath() + "]目录");
					File[] files = localeFile.listFiles();
					for (int i = 0; i < files.length; i++) {
						if (uploadFiles(files[i], remotePath + "/" + localeFile.getName())) {
							flag = true;
						} else {
							return false;
						}
					}
					ftpClient.changeToParentDirectory();// return parent
														// directory
					return flag;
				} else if (!FILELOCK.equals(localeFile.getName())) {
					fis = new FileInputStream(localeFile);
					ftpClient.storeFile(localeFile.getName(), fis);
					logger.info("[" + localeFile.getAbsolutePath() + "]上传成功!");
					return true;
				}
				return true;
			} catch (IOException e) {
				logger.error("", e);
				throw new FtpException("FTP上传[" + localeFile.getAbsolutePath() + "]出错!", e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * 下载
	 * 
	 * @param remotePath 下载目录  	GBK编码
	 * @param localPath 本地目录 		ISO-8859-1编码
	 * @return
	 * @throws Exception
	 */
	public boolean downLoadFile(String remotePath, String localPath) throws FtpException {
		synchronized (LOCK) {
			try {
				if (ftpClient.changeWorkingDirectory(remotePath)) {// 转移到FTP服务器目录
					FTPFile[] files = ftpClient.listFiles();
					if (files.length > 0) {
						File localdir = new File(localPath);
						if (!localdir.exists()) {
							localdir.mkdirs();
						}
					}
					for (FTPFile ff : files) {
						if (!downLoadFile(ff, localPath)) {
							return false;
						}
					}
					return files.length > 0;
				}
			} catch (IOException e) {
				logger.error("", e);
				throw new FtpException("FTP下载[" + localPath + "]出错!", e);
			}
			return false;
		}
	}

	/**
	 * 递归下载文件
	 * 
	 * @param ftpFile 下载文件/目录
	 * @param localPath 本地目录
	 * @return
	 */
	public boolean downLoadFile(FTPFile ftpFile, String localPath) {
		String localfilename="";
		try {
			localfilename= new String(ftpFile.getName().getBytes("ISO-8859-1"),encode);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 当前处理文件本地路径
		String fileLocalPath = localPath + "/" + localfilename;
		if (ftpFile.isFile()) {// down file
			if (localfilename.indexOf("?") == -1) {
				OutputStream outputStream = null;
				try {
					File localFile = new File(fileLocalPath);
					if (!localFile.getParentFile().exists()) {
						localFile.getParentFile().mkdirs();
					}
					outputStream = new FileOutputStream(localFile);
					ftpClient.retrieveFile(ftpFile.getName(), outputStream);
					outputStream.flush();
					outputStream.close();
					logger.info("[" + localFile.getAbsolutePath() + "]下载成功!");
					return true;
				} catch (Exception e) {
					logger.error("", e);
					throw new FtpException("FTP下载[" + fileLocalPath + "]出错!", e);
				} finally {
					try {
						if (outputStream != null)
							outputStream.close();
					} catch (IOException e) {
					}
				}
			}
		} else { // deal dirctory
			File file = new File(fileLocalPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				// enter relative workdirectory
				if (ftpClient.changeWorkingDirectory(ftpFile.getName())) {
					logger.info("[" + file.getAbsolutePath() + "]目录");
					FTPFile[] files = null;
					files = ftpClient.listFiles();
					for (int i = 0; i < files.length; i++) {
						downLoadFile(files[i], fileLocalPath);
					}
					ftpClient.changeToParentDirectory();// return parent
														// directory
					return true;
				}
			} catch (Exception e) {
				logger.error("", e);
				throw new FtpException("FTP下载[" + fileLocalPath + "]出错!", e);
			}
		}
		return false;
	}
	
	/*** 
	  * 下载文件 
	  * @param remoteFileName 待下载文件名称 GBK编码
	  * @param localDires 下载到当地那个路径下 GBK编码
	  * @param remoteDownLoadPath remoteFileName所在的路径 需要ISO-8859-1编码
	  * */ 
	 public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
		 synchronized (LOCK) {
			  String strFilePath = localDires + remoteFileName;
			  File localdir = new File(localDires);
			  if (!localdir.exists()) {
				  localdir.mkdirs();
			  }
			  BufferedOutputStream outStream = null;
			  boolean success = false;
			  try {
				  
				   if(ftpClient.changeWorkingDirectory(remoteDownLoadPath)){
					   outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
					   logger.info(remoteFileName + "开始下载....");
					   success = ftpClient.retrieveFile(new String(remoteFileName.getBytes(encode),"ISO-8859-1"), outStream);
					   if (success == true) {
						   logger.info(remoteFileName + "成功下载到" + strFilePath);
						   return success;
					   }
				   }else{
					   throw new FtpException("文件不存在!");
				   }
			   }catch (Exception e) {
				   e.printStackTrace();
				   logger.error(remoteFileName + "下载失败");
			   }finally {
				   if (null != outStream) {
					    try {
					    	outStream.flush();
					    	outStream.close();
					    }catch (IOException e) {
					    	e.printStackTrace();
					    }
				   }
			  }
			  if (success == false) {
				  logger.error(remoteFileName + "下载失败!!!");
			  }
			  return success;
		 }
	 }

	/** 获得目录下最大文件名 */
	public String getMaxFileName(String remotePath) {
		try {
			ftpClient.changeWorkingDirectory(remotePath);
			FTPFile[] files = ftpClient.listFiles();
			Arrays.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile o1, FTPFile o2) {
					return o2.getName().compareTo(o1.getName());
				}
			});
			return files[0].getName();
		} catch (IOException e) {
			logger.error("", e);
			throw new FtpException("FTP访问目录[" + remotePath + "]出错!", e);
		}
	}

	/**
	 * 连接参数
	 * 
	 * @param properties <br>
	 *            FTPHOSTNAME:IP; FTPPORT:端口; FTPUSERNAME:用户名; FTPPASSWORD:密码
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 连接参数
	 * 
	 * @param properties <br>
	 *            FTPHOSTNAME:IP; FTPPORT:端口; FTPUSERNAME:用户名; FTPPASSWORD:密码
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = new Properties();
		String[] key = { "FTPHOSTNAME", "FTPPORT", "FTPUSERNAME", "FTPPASSWORD" };
		for (int i = 0; i < key.length; i++) {
			this.properties.put(key[i], properties.get(key[i]));
		}
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}
	
}