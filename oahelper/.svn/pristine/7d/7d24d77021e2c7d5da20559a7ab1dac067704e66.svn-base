package com.tfzq.wechatmsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ddfc.service.RedisUtil;


public class WxTokenUtil {
    private static Logger log = LoggerFactory.getLogger(WxTokenUtil.class);
    
    private RedisUtil redisService;
    private String appId;
    private String appSecret;

    
    
   /**
     *  获得微信 AccessToken
     * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。
     * 开发者需要access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取
     * 的access_token失效。  
     * （此处我是把token存在Redis里面了）   
     */
    
    public AccessToken getWXToken() {
    	
        AccessToken access_token = (AccessToken)redisService.get(WeiXinEnum.CACHE_TYPE.CACHE_WX_ACCESS_TOKEN.name());
        if(access_token == null || access_token.getAccessToken().equals("")){
            String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ appId+"&secret="+ appSecret;
            JSONObject jsonObject = WX_HttpsUtil.httpsRequest(tokenUrl, "GET", null);
            if (null != jsonObject) {
                try {
                    access_token = new AccessToken();
                    access_token.setAccessToken(jsonObject.getString("access_token"));
                    access_token.setExpiresin(jsonObject.getInteger("expires_in"));
                    log.info(jsonObject.toJSONString());
                } catch (JSONException e) {
                    access_token = null;
                    // 获取token失败
                    log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                }
            }
            
            redisService.set(WeiXinEnum.CACHE_TYPE.CACHE_WX_ACCESS_TOKEN.name(), access_token,7100L);
        }
        return access_token;
    }
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public RedisUtil getRedisService() {
		return redisService;
	}
	public void setRedisService(RedisUtil redisService) {
		this.redisService = redisService;
	}

}
