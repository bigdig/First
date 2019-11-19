package com.hanvoncloud;

import org.ibase4j.core.support.security.coder.MDCoder;

import com.hanvoncloud.common.LanguageEnum;

public class HwCloudBcardDemo {

	public static void main(String[] args)throws Exception {
		//testBcard();
		System.out.println(MDCoder.encodeMD5("中".getBytes()).toString());
		System.out.println(MDCoder.encodeMD5("中国".getBytes()).toString());
		System.out.println(MDCoder.encodeMD5("中国人".getBytes()).toString());
		System.out.println(MDCoder.encodeMD5("中国人民".getBytes()).toString());
		System.out.println(MDCoder.encodeMD5("这是虾米回事啊".getBytes()).toString());
		System.out.println(MDCoder.encodeMD5("这是什么回事啊".getBytes()).toString());
	}
	
	private static void testBcard(){
		//您在汉王云开发者中心获取的key
		String key = "9f7bec55-2272-4eb6-aff5-cb12631a64f1";  
		
		//语言code，AUTO:自动识别语言且带坐标 CHNS:中文简体  CHNT：中文繁体  EN:英文  
		String language = LanguageEnum.CHNS.getCode();
		
		//待识别图片绝对路径
		String path = "C:\\Users\\Administrator\\Desktop\\card1.jpg";
		
		String result = HwCloud.getInstance().recgBcard(key, language, path);
		System.out.println(result);
	}
}
