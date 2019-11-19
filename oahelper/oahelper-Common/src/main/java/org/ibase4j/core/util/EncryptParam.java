package org.ibase4j.core.util;

public class EncryptParam {

	//数字加密签名
	private String signature;
	
	//时间戳
	private String timestamp;
	
	//随机数
	private String nonce;
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

}
