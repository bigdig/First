package org.ibase4j.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
/*
 * 验证用户身份
 */
public class ValidateUserUtil {
	
	public static String TOKEN="sharevalidatetoken";

//	public static boolean validateUser(EncryptParam encryptParam){
//		boolean isValid=true;
//		String signature=encryptParam.getSignature();
//		String timestamp=encryptParam.getTimestamp();
//		String nonce=encryptParam.getNonce();
//		String generateSignature=generateSignature(timestamp,nonce);
//		if(!signature.equals(generateSignature)){
//			isValid=false;
//		}
//		return isValid;
//	}
	
//	public static boolean validateUser(EncryptParam encryptParam){
//		boolean isValid=true;
//		String signature=encryptParam.getSignature();
//		String timestamp=encryptParam.getTimestamp();
//		String nonce=encryptParam.getNonce();
//		String token=encryptParam.getToken();
//		String generateSignature=generateSignature(timestamp,nonce,token);
//		if(!signature.equals(generateSignature)){
//			isValid=false;
//		}
//		return isValid;
//	}
	
	public static boolean validateUser(EncryptParam encryptParam){
		boolean isValid=true;
		String signature=encryptParam.getSignature();
		String timestamp=encryptParam.getTimestamp();
		String nonce=encryptParam.getNonce();
		String token=TOKEN;
		String generateSignature=generateSignature(timestamp,nonce,token);
		if(!signature.equals(generateSignature)){
			isValid=false;
		}
		return isValid;
	}	
	
	/*
	 * 将token、timestamp、nonce三个参数进行字典序排序
	 */
//	private static String constructorStr(String timestamp,String nonce){
//		List<String> list=new ArrayList<String>();
//		list.add(timestamp);
//		list.add(nonce);
//		list.add(TOKEN);
//		Collections.sort(list);
//		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<list.size();i++){
//			sb.append(list.get(i));
//		}
//		return sb.toString();
//	}
	
	private static String constructorStr(String timestamp,String nonce,String TOKEN){
		List<String> list=new ArrayList<String>();
		list.add(timestamp);
		list.add(nonce);
		list.add(TOKEN);
		Collections.sort(list);
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i));
		}
		return sb.toString();
	}	
	
	private static String constructorStr2(String noncestr,String jsapi_ticket,String timestamp,String url){
		List<String> list=new ArrayList<String>();
		list.add("noncestr="+noncestr);
		list.add("jsapi_ticket="+jsapi_ticket);
		list.add("timestamp="+timestamp);
		list.add("url="+url);
		Collections.sort(list);
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i)+"&");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}	
	
	/*
	 * 生成时间戳
	 */
	private static String generateTimeStamp(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp=sdf.format(new Date());
		return timeStamp;
	}
	
	/*
	 * 生成随机数
	 */
	private static String generateNonce(){
		String str="0123456789abcdefghigklmnopqrstuvwxyz";
		Random random=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<6;i++){
			int index=random.nextInt(str.length());
			sb.append(str.charAt(index));
		}
		return sb.toString();
	}
	
	/*
	 * 生成数字签名
	 */
//	private static String generateSignature(String timestamp,String nonce){
//		String sbStr=constructorStr(timestamp,nonce);
//		String generateSignature=EncryptUtil.encode("SHA1",sbStr);
//		return generateSignature;
//	}
	
	private static String generateSignature(String timestamp,String nonce,String token){
		String sbStr=constructorStr(timestamp,nonce,token);
		String generateSignature=EncryptUtil.encode("SHA1",sbStr);
		return generateSignature;
	}
	
	private static String generateSignature2(String noncestr,String jsapi_ticket,String timestamp,String url){
		String sbStr=constructorStr2(noncestr,jsapi_ticket,timestamp,url);
		String generateSignature=EncryptUtil.encode("SHA1",sbStr);
		return generateSignature;
	}
	
	/*
	 * 生成调用方需要传递的数字签名json参数
	 */
//	public static String generateSenderSignatureParam(){
//		JSONObject json=new JSONObject();
//		String timestamp=generateTimeStamp();
//		String nonce=generateNonce();
//		String signature=generateSignature(timestamp,nonce);
//		json.put("timestamp",timestamp);
//		json.put("nonce",nonce);
//		json.put("signature",signature);
//		return json.toJSONString();
//	}
	
	public static String generateSenderSignatureParam(String token){
		JSONObject json=new JSONObject();
		String timestamp=generateTimeStamp();
		String nonce=generateNonce();
		String signature=generateSignature(timestamp,nonce,token);
		json.put("timestamp",timestamp);
		json.put("nonce",nonce);
		json.put("signature",signature);
		return json.toJSONString();
	}	
	
	public static JSONObject generateSenderSignatureParam2(String jsapi_ticket,String url){
		JSONObject json=new JSONObject();
		String timestamp=generateTimeStamp();
		String noncestr=generateNonce();
		
		String signature=generateSignature2(noncestr,jsapi_ticket,timestamp,url);
		json.put("timestamp",timestamp);
		json.put("noncestr",noncestr);
		json.put("url",url);
		json.put("signature",signature);
		return json;
	}
	
	
	public static void main(String[] args) {
		
		
		
		String timeStamp="20151017143737";
		String nonce="123abc";
		String result=constructorStr(timeStamp,nonce,"reallypointservicevalidatetoken");
		String generateSignature=EncryptUtil.encode("SHA1",result);
		System.out.println(generateSignature);
		
		
		
		//ValidateUserUtil.generateSenderSignatureParam("");
		
		
		
		
		
	}
	
}
