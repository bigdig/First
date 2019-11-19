package com.tfzq.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.ibase4j.core.exception.IllegalParameterException;
import org.jeewx.api.wxbase.wxmedia.JwMediaAPI;
import org.jeewx.api.wxbase.wxmedia.model.WxMediaForMaterialResponse;
import org.jeewx.api.wxbase.wxtoken.JwTokenAPI;
import org.jeewx.api.wxsendmsg.JwSendMessageAPI;
import org.jeewx.api.wxsendmsg.model.WxArticle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WxUtils {
	private String appid;
	private String appscret;
	private String accesstoken = "";

	// private static String dir =
	// "E:/svn/itwh/trunk/Code/17_infopub/trunk/infopub";

	public WxUtils(String appid, String appscret) {
		super();
		this.appid = appid;
		this.appscret = appscret;
	}

	/**
	 * 获取访问token
	 */
	public String getAccessToken() throws Exception {
		if (StringUtils.isEmpty(accesstoken)) {
			accesstoken = JwTokenAPI.getAccessToken(appid, appscret);
		}
		System.out.println("accesstoken:" + accesstoken);
		return accesstoken;
	}

	/**
	 * 上传封面，必须作为素材存储
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String uploadThumbMedia(String dir, String filename) throws Exception {
		System.out.println("uploadThumbMedia accesstoken:" + accesstoken);
		WxMediaForMaterialResponse upload = JwMediaAPI.addMediaFileByMaterialNews(accesstoken, "thumb", dir, filename);
		System.out.println(upload.getMedia_id());
		return upload.getMedia_id();
	}

	/**
	 * 上传文章中的图片
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String uploadNewsImage(String dir, String filename) throws Exception {
		String url = JwSendMessageAPI.uploadImg(accesstoken, dir + "/" + filename);
		System.out.println(url);
		return url;
	}

	/**
	 * 上传图文
	 * 
	 * @param wxArticles
	 * @throws Exception
	 */
	public void uploadNews(List<WxArticle> wxArticles) throws Exception {
		JwMediaAPI.uploadArticlesByMaterialNews(accesstoken, wxArticles);
	}
	
	
	/**
	 * 微信登录静默授权获取用户基本信息
	 * @param code
	 * @param appid
	 * @param secretCode
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getWeixinUserBaseInfo(String code, String appid, String secretCode) throws Exception {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secretCode + "&code=" + code
				+ "&grant_type=authorization_code";
		JSONObject userBaseInfo = doGetJson(url);
		if(userBaseInfo == null || userBaseInfo.getInteger("errcode") != null || userBaseInfo.getString("openid") == null) {  
			throw new IllegalParameterException("无法获取到openid");//如果返回值没有 或者 出现错误返回errcode 或者   
		} 
		return userBaseInfo;
	}

	/**
	 * 微信登录授权获取用户信息
	 * @param code
	 * @param appid
	 * @param secretCode
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getWeixinUserInfo(String code, String appid, String secretCode) throws Exception {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secretCode + "&code=" + code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = doGetJson(url);
		if(jsonObject == null || jsonObject.getInteger("errcode") != null || jsonObject.getString("openid") == null) {  
			throw new IllegalParameterException("无法获取到openid");//如果返回值没有 或者 出现错误返回errcode 或者   
		} 
		String openid = jsonObject.getString("openid");
		String token = jsonObject.getString("access_token");
		String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid + "&lang=zh_CN";
		JSONObject userInfo = doGetJson(infoUrl);
		if(userInfo == null || userInfo.getInteger("errcode") != null || userInfo.getString("openid") == null) {  
			throw new IllegalParameterException("无法获取到用户信息");//如果返回值没有 或者 出现错误返回errcode 或者   
		} 
		return userInfo;
	}

	public static JSONObject doGetJson(String url) throws IOException {
		JSONObject jsonObject = null;
		CloseableHttpClient defaultHttpClient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			jsonObject = JSON.parseObject(result);
		}
		httpGet.releaseConnection();
		return jsonObject;
	}

	public static void main(String[] args) throws Exception {
		// getAccessToken();
		// uploadMedia();
		// uploadNews();

	}

	//获取微信小程序用户的openid和session
	public static JSONObject getWxprogramUserInfo(String code, String appid, String appsecret) throws IOException {
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+appsecret+"&js_code="+code+"&grant_type=authorization_code";
		JSONObject userInfo = doGetJson(url);
		if(userInfo == null || userInfo.getInteger("errcode") != null || userInfo.getString("openid") == null) {  
			throw new IllegalParameterException("无法获取到openid");//如果返回值没有 或者 出现错误返回errcode 或者   
		} 
		return userInfo;
	}
}
