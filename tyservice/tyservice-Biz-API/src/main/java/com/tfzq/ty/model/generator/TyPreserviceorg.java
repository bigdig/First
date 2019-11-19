package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyPreserviceorg extends BaseModel{
    private String orgName;
    private String serviceSaler;
    private String orgLevel;
    private String salerId;
    private String custStatus;
    private String orgSimpleName;
    private String custCat;
    private String teamCat;
    private String address;
    private String companyMail;
    private String companyTel;
    private String contactorName;
    private String contactorTel;
    private String recSms;
    private String recEmail;
    private Integer whiteDeadline;
    
    private String orgExists;
    private String preporgStatus;
    private String mark;

    public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getPreporgStatus() {
		return preporgStatus;
	}
	public void setPreporgStatus(String preporgStatus) {
		this.preporgStatus = preporgStatus;
	}
	public String getOrgExists() {
		return orgExists;
	}
	public void setOrgExists(String orgExists) {
		this.orgExists = orgExists;
	}
	public Integer getWhiteDeadline() {
		return whiteDeadline;
	}
	public void setWhiteDeadline(Integer whiteDeadline) {
		this.whiteDeadline = whiteDeadline;
	}
    
    public String getRecSms() {
		return recSms;
	}
	public void setRecSms(String recSms) {
		this.recSms = recSms;
	}
	public String getRecEmail() {
		return recEmail;
	}
	public void setRecEmail(String recEmail) {
		this.recEmail = recEmail;
	}
	public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgName(){
        return this.orgName;
    }
    public void setServiceSaler(String serviceSaler){
        this.serviceSaler = serviceSaler;
    }
    public String getServiceSaler(){
        return this.serviceSaler;
    }
    public void setOrgLevel(String orgLevel){
        this.orgLevel = orgLevel;
    }
    public String getOrgLevel(){
        return this.orgLevel;
    }
    public void setSalerId(String salerId){
        this.salerId = salerId;
    }
    public String getSalerId(){
        return this.salerId;
    }
    public void setCustStatus(String custStatus){
        this.custStatus = custStatus;
    }
    public String getCustStatus(){
        return this.custStatus;
    }
    public void setOrgSimpleName(String orgSimpleName){
        this.orgSimpleName = orgSimpleName;
    }
    public String getOrgSimpleName(){
        return this.orgSimpleName;
    }
    public void setCustCat(String custCat){
        this.custCat = custCat;
    }
    public String getCustCat(){
        return this.custCat;
    }
    public void setTeamCat(String teamCat){
        this.teamCat = teamCat;
    }
    public String getTeamCat(){
        return this.teamCat;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setCompanyMail(String companyMail){
        this.companyMail = companyMail;
    }
    public String getCompanyMail(){
        return this.companyMail;
    }
    public void setCompanyTel(String companyTel){
        this.companyTel = companyTel;
    }
    public String getCompanyTel(){
        return this.companyTel;
    }
    public void setContactorName(String contactorName){
        this.contactorName = contactorName;
    }
    public String getContactorName(){
        return this.contactorName;
    }
    public void setContactorTel(String contactorTel){
        this.contactorTel = contactorTel;
    }
    public String getContactorTel(){
        return this.contactorTel;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}

}