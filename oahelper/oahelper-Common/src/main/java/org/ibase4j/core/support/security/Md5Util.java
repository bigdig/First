package org.ibase4j.core.support.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Md5Util {

	/*
	 * 方法名称：getMD5 
	 * 功    能：字符串MD5加密 
	 * 参    数：待转换字符串 
	 * 返 回 值：加密之后字符串
	 */
	public static String getMD5(String sourceStr)  {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 生成URL访问的加密串
	 * @param url
	 * @param secretCode
	 * @return
	 */
	public static String getMD5(String url,String timestmap,String secretCode)  {
		String md = getMD5(url + timestmap + secretCode);
		return md;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String url = "/openapi/finishAuditMsg";
		Date date = new Date();
		//System.out.println(date.getTime());
		String timestmap = String.valueOf(date.getTime());
		System.out.println(timestmap);
		String secretCode = "tfty$2017";
		String md = Md5Util.getMD5(url + timestmap + secretCode);
		System.out.println(md);
	}
	
}
