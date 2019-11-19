package com.tfzq.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpApche {
    private  FTPClient ftpClient = new FTPClient();
    private  String encoding = System.getProperty("file.encoding");
    private  String url = null;
    private  Integer port = null;
    private  String username = null;
    private  String password = null;
    private  String path = null;
    public FtpApche(String url,int port,String username,String password,String path){
    	this.url=url;
    	this.port=port;
    	this.username=username;
    	this.password=password;
    	this.path=path;
    }
    /**
     * Description: 向FTP服务器上传文件
     * @param url  FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录,如果是根目录则为“/”
     * @param filename 上传到FTP服务器上的文件名
     * @param input 本地文件输入流
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String filename, InputStream input) {
        boolean result = false;
        try {
            int reply;
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
            	ftpClient.connect(url, port);
                ftpClient.login(username, password);
                ftpClient.setControlEncoding(encoding);
                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                  System.out.println("连接失败");
                  ftpClient.disconnect();
                  return result;
                }
            }            
            ftpClient.makeDirectory(path);
            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                if (!result) {
                    System.out.println("上传失败!");
                }
            }
            input.close();           
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return result;
    }
    public void disconnect(){  	
    	if (ftpClient.isConnected()) {
    		try {
    			ftpClient.logout();
    			ftpClient.disconnect();
        	} catch (IOException ioe) {
            }
        }
    }
    @SuppressWarnings("resource")
	public static void main(String[] args) {
        new FtpApche("",0,"","","");
        try {
        	new FileInputStream(new File("E:/1.rar"));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
    }
}
