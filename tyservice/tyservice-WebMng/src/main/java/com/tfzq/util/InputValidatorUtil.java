package com.tfzq.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class InputValidatorUtil {
	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^(((1(3|4|5|6|7|8|9))\\d{9})|(00852(-?)\\d{8}))$";
	/**
	 * 正则表达式：验证座机号
	 */
	public static final String REGEX_TEL = "^((\\d{3,4}-\\d{7,8}(-\\d{2,4})?)|(00852-\\d{8}))$";

	// 邮箱正则表达式
	// public static final String REGEX_EMAIL =
	// "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	public static final String REGEX_EMAIL = "^[A-Za-z0-9_-]+([-_.][A-Za-z0-9_-]+)*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

	public static final String REGEX_263_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public static final String ERROR_263_INFO = "邮箱不能超过50个字符，且不能以 .con ， .cm ， @gmial.com ， @gmial.com ， @gmai.com 结尾";

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isTel(String tel) {
		return Pattern.matches(REGEX_TEL, tel);
	}

	/**
	 * 检验邮箱
	 * 
	 * @param args
	 */
	public static boolean isEmail(String email) {
		Pattern emailer = Pattern.compile(REGEX_263_EMAIL);
		if (StringUtils.isBlank(email)) {
			return false;
		}
		return emailer.matcher(email).matches();
		//return Pattern.matches(REGEX_EMAIL, email);
	}

	public static boolean is263Email(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		email = email.trim().toLowerCase();
		if(email.length() > 50){
			return false;
		}
		
		if (email.endsWith(".con"))
			return false;
		if (email.endsWith(".cm"))
			return false;
		if (email.endsWith("@gmial.com"))
			return false;
		if (email.endsWith("@gamil.com"))
			return false;
		if (email.endsWith("@gmai.com"))
			return false;
		
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(isMobile("13871066260"));
//		System.out.println(isMobile("0085211111111"));
//		System.out.println(isMobile("00852-11111111"));
		System.out.println("emailTest:" + is263Email("LLLi@gmailll.com"));
		// System.out.println(isMobile("00852-111111111"));
		// System.out.println(isMobile("19871066260"));
		// System.out.println(isTel("027-86974567"));
		// System.out.println(isTel("0271-86974567"));
		// System.out.println(isTel("027-869745673"));
//		System.out.println(isTel("00852-86974567"));
//		System.out.println(isTel("0085286974567"));
		// System.out.println(isTel("00852-869745677"));
	}

}
