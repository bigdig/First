package com.tfzq.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.ibase4j.core.util.UploadUtil;
/**
 * 静态化工具类
 * @author Administrator
 *
 */
public class PageFileUtil {

	private static Map<String, String> locks = new HashMap<String, String>();

	public static void checkAndCreateDir(String dir) {
		File modelDir = new File(dir);
		if (!modelDir.isDirectory()) {
			modelDir.mkdirs();
		}
	}

	public static boolean isFileExists(String filePath) {
		boolean htmlExits = false;
		File htmlFile = new File(filePath);
		if (htmlFile.exists()) {
			htmlExits = true;
		}
		return htmlExits;
	}

	public static boolean isLatest(String dir, long updateTimeLong) {
		long nowTimeLong = getCurrentVersion(dir);
		if (updateTimeLong <= nowTimeLong) {
			return true;
		}
		return false;
	}

	public static long getCurrentVersion(String dir) {
		File modeldir = new File(dir);
		File files[] = modeldir.listFiles(new CustomeFilenameFilter());
		String txtFileName = "";
		String txtPreName = "0";// 文件名 去掉了后缀
		if (files != null && files.length > 0) {
			txtFileName = files[0].getName();
			int num = UploadUtil.getSuffix(txtFileName).length();
			txtPreName = txtFileName.substring(0, txtFileName.length() - num);
		}
		long nowTimeLong = Long.parseLong(txtPreName);

		return nowTimeLong;
	}

	public static void updatePage(String dir, String fileName, String fileContent, long updateTime) {

		String lock = getLock(dir);
		synchronized (lock) {
			long currentVersion = getCurrentVersion(dir);
			if (currentVersion != updateTime) {
				// 清除目录下所有文件
				File modelDir = new File(dir);
				if (modelDir.exists()) {
					String[] tempList = modelDir.list();
					File temp = null;
					for (int i = 0; i < tempList.length; i++) {
						temp = new File(dir + File.separator + tempList[i]);
						if (temp.isFile()) {
							temp.delete();
						}
					}
				}
				// 重新生成文件
				String everyHtmlPath = dir + File.separator + fileName + ".html";
				File htmlFile = new File(everyHtmlPath);
//				FileWriter fw;
				try {
//					fw = new FileWriter(htmlFile.getAbsoluteFile());
//					BufferedWriter bw = new BufferedWriter(fw);
//					bw.write(fileContent);
//					bw.close();
					FileUtils.write(htmlFile.getAbsoluteFile(), fileContent, Charset.forName("utf-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 添加版本文件
				File timeFile = new File(dir + File.separator + updateTime + ".txt");
				if (!timeFile.exists()) {
					try {
						timeFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

	public static void updatePages(String dir, Map<String, String> files, long updateTime) {
		String lock = getLock(dir);
		synchronized (lock) {
			long currentVersion = getCurrentVersion(dir);
			if (currentVersion != updateTime) {
				// 清除目录下所有文件
				File modelDir = new File(dir);
				if (modelDir.exists()) {
					String[] tempList = modelDir.list();
					File temp = null;
					for (int i = 0; i < tempList.length; i++) {
						temp = new File(dir + File.separator + tempList[i]);
						if (temp.isFile()) {
							temp.delete();
						}
					}
				}
				// 重新生成文件
				for (Entry<String, String> entry : files.entrySet()) {
					String i = entry.getKey();
					String everyHtmlPath = dir + File.separator + i + ".html";
					File htmlFile = new File(everyHtmlPath);
//					FileWriter fw;
					try {
						
//						fw = new FileWriter(htmlFile.getAbsoluteFile());
//						BufferedWriter bw = new BufferedWriter(fw);
//						bw.write(entry.getValue());
//						bw.close();
						FileUtils.write(htmlFile.getAbsoluteFile(), entry.getValue(), Charset.forName("utf-8"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 添加版本文件
				File timeFile = new File(dir + File.separator + updateTime + ".txt");
				if (!timeFile.exists()) {
					try {
						timeFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	public static synchronized String getLock(String lock) {
		if (locks.containsKey(lock)) {
			return locks.get(lock);
		} else {
			locks.put(lock, lock);
			return lock;
		}
	}

}