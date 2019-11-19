package org.ibase4j.core.support.fastdfs;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

/**
 * @author ShenHuaJie
 * @version 2016年6月27日 上午9:51:06
 */
@SuppressWarnings("serial")
public class FileManager implements Config {
	private static Logger logger = LogManager.getLogger();
	// private static TrackerClient trackerClient;
	// private static TrackerServer trackerServer;
	// private static StorageServer storageServer;
	// private static StorageClient storageClient;
	// private static FastDHTClient fastDHTClient;
	private static ConnectionPool connectionPool;

	static { // Initialize Fast DFS Client configurations
		try {
			connectionPool = new ConnectionPool(10, 20, 200);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static FastDfsFile upload(FileModel file) throws Exception {
		logger.info("File Name: " + file.getFilename() + ". File Length: " + file.getContent().length);
		/** 获取fastdfs服务器连接 */
		TrackerServer trackerServer = connectionPool.checkout();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		NameValuePair[] meta_list = new NameValuePair[] { new NameValuePair("mime", file.getMime()), new NameValuePair("size", file.getSize()),
				new NameValuePair("filename", file.getFilename()) };

		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		try {
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			logger.error("IO Exception when uploadind the file: " + file.getFilename(), e);
		} catch (Exception e) {
			logger.error("Non IO Exception when uploadind the file: " + file.getFilename(), e);
		}
		logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

		if (uploadResults == null) {
			logger.error("upload file fail, error code: " + storageClient.getErrorCode());
		}

		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];

		String fileAbsolutePath = PROTOCOL + trackerServer.getInetSocketAddress().getHostName() + SEPARATOR + TRACKER_NGNIX_PORT + SEPARATOR + groupName
				+ SEPARATOR + remoteFileName;
		file.setRemotePath(fileAbsolutePath);
		logger.info("upload file successfully!!!  " + "group_name: " + groupName + ", remoteFileName:" + " " + remoteFileName);
		FastDfsFile fastDfsFile = new FastDfsFile();
		fastDfsFile.setGroupName(groupName);
		fastDfsFile.setFileName(remoteFileName);
		/** 上传完毕及时释放连接 */  
		connectionPool.checkin(trackerServer);
		return fastDfsFile;
	}

	public static byte[] downloadFile(String groupName, String fileName) throws Exception {
		/** 获取fastdfs服务器连接 */
		TrackerServer trackerServer = connectionPool.checkout();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		try {
			return storageClient.download_file(groupName, fileName);
		} catch (IOException e) {
			logger.error("IO Exception: Download File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Download File from Fast DFS failed", e);
		}finally{
			/** 及时释放连接 */  
			connectionPool.checkin(trackerServer);
		}
		return null;
	}

	public static FileInfo getFile(String groupName, String fileName) throws Exception {
		/** 获取fastdfs服务器连接 */
		TrackerServer trackerServer = connectionPool.checkout();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		try {
			return storageClient.get_file_info(groupName, fileName);
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}finally{
			/** 及时释放连接 */  
			connectionPool.checkin(trackerServer);
		}
		return null;
	}

	public static void deleteFile(String groupName, String remoteFileName) throws Exception {
		/** 获取fastdfs服务器连接 */
		TrackerServer trackerServer = connectionPool.checkout();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		storageClient.delete_file(groupName, remoteFileName);
		
		/** 及时释放连接 */  
		connectionPool.checkin(trackerServer);
	}
}
