package org.ibase4j.core.support.login;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private Boolean adlogin;
	private Boolean existWx;
	private String captcha;
	private String dptoken;
	private String wxId;
	private String avatar;
	

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Boolean getAdlogin() {
		return adlogin;
	}

	public void setAdlogin(Boolean adlogin) {
		this.adlogin = adlogin;
	}

	public String getDptoken() {
		return dptoken;
	}

	public void setDptoken(String dptoken) {
		this.dptoken = dptoken;
	}

	public Boolean getExistWx() {
		return existWx;
	}

	public void setExistWx(Boolean existWx) {
		this.existWx = existWx;
	}

	public UsernamePasswordCaptchaToken() {
		super();

	}

	public UsernamePasswordCaptchaToken(String username, String password,
			 String captcha) {
		super(username, password);
		this.captcha = captcha;
	}
	
	public UsernamePasswordCaptchaToken(String username, String password,
			Boolean adlogin) {
		super(username, password);
		this.adlogin = adlogin;
	}
	public UsernamePasswordCaptchaToken(String dptoken) {
		super(" ", " ");
		this.dptoken = dptoken;
		this.adlogin = false;
	}
	public UsernamePasswordCaptchaToken(String username,String password,String wxId,boolean adlogin,String avatar,boolean existWx){
		super(username,password);
		this.adlogin = adlogin;
		this.existWx = existWx;
		this.wxId  = wxId;
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UsernamePasswordCaptchaToken [adlogin=" + adlogin + ", existWx=" + existWx + ", captcha=" + captcha
				+ ", dptoken=" + dptoken + ", wxId=" + wxId + ", avatar=" + avatar + "]";
	}
	
}