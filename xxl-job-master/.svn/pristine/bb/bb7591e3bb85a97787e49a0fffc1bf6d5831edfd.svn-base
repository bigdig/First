package com.tfzq.tycrm.entity;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyMessagelog extends BaseModel{
    private String msgType;
    private String msgContent;
    private String receiver;
    private String receiverName;
    private String receiverTel;
    private String receiverEmail;
    private String sendFlag;
    private String sendDate;
    private String title;
    private String msgAuditid;
    private String fileName;
    private String filePath;

    private String senderEmail;
    private String senderPass;
    private String remark;
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setMsgType(String msgType){
        this.msgType = msgType;
    }
    public String getMsgType(){
        return this.msgType;
    }
    public void setMsgContent(String msgContent){
        this.msgContent = msgContent;
    }
    public String getMsgContent(){
        return this.msgContent;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public String getReceiver(){
        return this.receiver;
    }
    public void setReceiverName(String receiverName){
        this.receiverName = receiverName;
    }
    public String getReceiverName(){
        return this.receiverName;
    }
    public void setReceiverTel(String receiverTel){
        this.receiverTel = receiverTel;
    }
    public String getReceiverTel(){
        return this.receiverTel;
    }
    public void setReceiverEmail(String receiverEmail){
        this.receiverEmail = receiverEmail;
    }
    public String getReceiverEmail(){
        return this.receiverEmail;
    }
    public void setSendFlag(String sendFlag){
        this.sendFlag = sendFlag;
    }
    public String getSendFlag(){
        return this.sendFlag;
    }
    public void setSendDate(String sendDate){
        this.sendDate = sendDate;
    }
    public String getSendDate(){
        return this.sendDate;
    }
    
    public String getMsgAuditid() {
		return msgAuditid;
	}
	public void setMsgAuditid(String msgAuditid) {
		this.msgAuditid = msgAuditid;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderPass() {
		return senderPass;
	}
	public void setSenderPass(String senderPass) {
		this.senderPass = senderPass;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}