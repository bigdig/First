package com.tfzq.tycrm.entity;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoInformUser extends BaseModel{
	private String informId;

    private String srcType;
	private String informTitle;
    private String informContent;
    private String externalLink;
    
    private String userId;
    private String userName;
    private String isRead;
    private String isReplied;
    private String isSend;
    
    
    public String getExternalLink() {
		return externalLink;
	}
	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}
	public String getSrcType() {
		return srcType;
	}
	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}
	public String getInformId() {
		return informId;
	}
	public void setInformId(String informId) {
		this.informId = informId;
	}
    public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	public String getInformTitle() {
		return informTitle;
	}
	public void setInformTitle(String informTitle) {
		this.informTitle = informTitle;
	}
	public String getInformContent() {
		return informContent;
	}
	public void setInformContent(String informContent) {
		this.informContent = informContent;
	}
	public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setIsRead(String isRead){
        this.isRead = isRead;
    }
    public String getIsRead(){
        return this.isRead;
    }
    public void setIsReplied(String isReplied){
        this.isReplied = isReplied;
    }
    public String getIsReplied(){
        return this.isReplied;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}