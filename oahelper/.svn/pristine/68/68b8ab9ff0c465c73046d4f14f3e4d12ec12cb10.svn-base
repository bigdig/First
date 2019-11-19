package com.tfzq.util;

import java.util.regex.Pattern;

public class InputValidatorUtil {
	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^(((1(3|4|5|7|8))\\d{9})|(00852(-?)\\d{8}))$";
	/**
	 * 正则表达式：验证座机号
	 */
	public static final String REGEX_TEL = "^((\\d{3,4}-\\d{7,8}(-\\d{2,4})?)|(00852-\\d{8}))$";

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println(isMobile("13871066260"));
		System.out.println(isMobile("0085211111111"));
		System.out.println(isMobile("00852-11111111"));
//		System.out.println(isMobile("00852-111111111"));
//		System.out.println(isMobile("19871066260"));
//		System.out.println(isTel("027-86974567"));
//		System.out.println(isTel("0271-86974567"));
//		System.out.println(isTel("027-869745673"));
		System.out.println(isTel("00852-86974567"));
		System.out.println(isTel("0085286974567"));
//		System.out.println(isTel("00852-869745677"));
	}

}
