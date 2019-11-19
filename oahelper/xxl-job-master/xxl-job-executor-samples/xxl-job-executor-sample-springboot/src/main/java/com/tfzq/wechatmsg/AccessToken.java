package com.tfzq.wechatmsg;

import java.io.Serializable;

public class AccessToken implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1145235765698491058L;
	private String accessToken;
	private Integer expiresin;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(Integer expiresin) {
		this.expiresin = expiresin;
	}

}
