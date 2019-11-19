package com.tfzq.pr.entity;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class InfChannelcontent extends BaseModel{
    private String channelId;
    private String channelName;
    private Integer channelNumber;
    private String contentId;
    private Integer publishCount;
    private String useStatus;
    private String companyId;
    
    public void setChannelId(String channelId){
        this.channelId = channelId;
    }
    public String getChannelId(){
        return this.channelId;
    }
    public void setChannelName(String channelName){
        this.channelName = channelName;
    }
    public String getChannelName(){
        return this.channelName;
    }
    public void setContentId(String contentId){
        this.contentId = contentId;
    }
    public String getContentId(){
        return this.contentId;
    }
    public void setPublishCount(Integer publishCount){
        this.publishCount = publishCount;
    }
    public Integer getPublishCount(){
        return this.publishCount;
    }
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}
	public Integer getChannelNumber() {
		return channelNumber;
	}
	public void setChannelNumber(Integer channelNumber) {
		this.channelNumber = channelNumber;
	}
	public String getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}