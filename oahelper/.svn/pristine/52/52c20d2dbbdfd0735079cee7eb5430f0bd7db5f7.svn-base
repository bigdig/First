package com.tfzq.generate;

import org.apache.commons.lang3.StringUtils;

public class Utils {
	public static String calBeanName(String tableName){
		String bean=StringUtils.lowerCase(tableName);
		StringBuffer sb = new StringBuffer();
		boolean delimeter = false;
		int i =0;
		for(char c:bean.toCharArray()){
			i++;
			if(c=='_'){
				delimeter = true;
				continue;
			}else{
				if(delimeter || i==1){
					sb.append(Character.toUpperCase(c));
					delimeter=false;
				}else{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
	public static String calBeanObjName(String beanName){
		return StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
	}

}
