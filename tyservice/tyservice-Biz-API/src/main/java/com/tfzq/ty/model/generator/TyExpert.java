package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyExpert extends BaseModel{
    private String expertName;
    private String orgName;
    private String orgId;
    private String title;
    private String expertType;
    private String expertTel;
    private String industry;
    private String brokerId;
    private String brokerName;
    private String brokerTel;
    private String closeLevel;
    private String source;
    private String expertProjectRole;
    
    
    public void setExpertName(String expertName){
        this.expertName = expertName;
    }
    public String getExpertName(){
        return this.expertName;
    }
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgName(){
        return this.orgName;
    }
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setExpertType(String expertType){
        this.expertType = expertType;
    }
    public String getExpertType(){
        return this.expertType;
    }
    public void setExpertTel(String expertTel){
        this.expertTel = expertTel;
    }
    public String getExpertTel(){
        return this.expertTel;
    }
    public void setIndustry(String industry){
        this.industry = industry;
    }
    public String getIndustry(){
        return this.industry;
    }
    public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public void setBrokerName(String brokerName){
        this.brokerName = brokerName;
    }
    public String getBrokerName(){
        return this.brokerName;
    }
    public void setBrokerTel(String brokerTel){
        this.brokerTel = brokerTel;
    }
    public String getBrokerTel(){
        return this.brokerTel;
    }
    public void setCloseLevel(String closeLevel){
        this.closeLevel = closeLevel;
    }
    public String getCloseLevel(){
        return this.closeLevel;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
    public String getExpertProjectRole() {
		return expertProjectRole;
	}
	public void setExpertProjectRole(String expertProjectRole) {
		this.expertProjectRole = expertProjectRole;
	}
	
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}