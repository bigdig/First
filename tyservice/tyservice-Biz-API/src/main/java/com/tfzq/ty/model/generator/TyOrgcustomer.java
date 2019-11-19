package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyOrgcustomer extends BaseModel{
    private String orgName;
    private String serviceSaler;
    private String custName;
    private String custEmail;
    private String custTel;
    private String custLevel;
    private String salerId;
    private String orgId;
    private String industry;
    private String domain;
    private String title;
    private String department;
    private String area;
    private String custMobile;
    private String custBakmobile;
    private String mark;
    private Integer activeDatetime;
    private String idNo;
    private String recEmail; 
    private String recSms;
    private String isAcceptRpt;
    private String rptSendGroup;
    private String rptSendGroupName;
    

    public String getRptSendGroupName() {
		return rptSendGroupName;
	}
	public void setRptSendGroupName(String rptSendGroupName) {
		this.rptSendGroupName = rptSendGroupName;
	}
	public String getIsAcceptRpt() {
		return isAcceptRpt;
	}
	public String getRptSendGroup() {
		return rptSendGroup;
	}
	public void setIsAcceptRpt(String isAcceptRpt) {
		this.isAcceptRpt = isAcceptRpt;
	}
	public void setRptSendGroup(String rptSendGroup) {
		this.rptSendGroup = rptSendGroup;
	}
	public String getRecEmail() {
		return recEmail;
	}
	public void setRecEmail(String recEmail) {
		this.recEmail = recEmail;
	}
	public String getRecSms() {
		return recSms;
	}
	public void setRecSms(String recSms) {
		this.recSms = recSms;
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
    public void setCustName(String custName){
        this.custName = custName;
    }
    public String getCustName(){
        return this.custName;
    }
    public void setCustEmail(String custEmail){
        this.custEmail = custEmail;
    }
    public String getCustEmail(){
        return this.custEmail;
    }
    public void setCustTel(String custTel){
        this.custTel = custTel;
    }
    public String getCustTel(){
        return this.custTel;
    }
    public void setCustLevel(String custLevel){
        this.custLevel = custLevel;
    }
    public String getCustLevel(){
        return this.custLevel;
    }
    public void setSalerId(String salerId){
        this.salerId = salerId;
    }
    public String getSalerId(){
        return this.salerId;
    }
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    public String getOrgId(){
        return this.orgId;
    }
    public void setIndustry(String industry){
        this.industry = industry;
    }
    public String getIndustry(){
        return this.industry;
    }
    public void setDomain(String domain){
        this.domain = domain;
    }
    public String getDomain(){
        return this.domain;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public String getDepartment(){
        return this.department;
    }
    public void setArea(String area){
        this.area = area;
    }
    public String getArea(){
        return this.area;
    }
    public void setCustMobile(String custMobile){
        this.custMobile = custMobile;
    }
    public String getCustMobile(){
        return this.custMobile;
    }
    public void setCustBakmobile(String custBakmobile){
        this.custBakmobile = custBakmobile;
    }
    public String getCustBakmobile(){
        return this.custBakmobile;
    }
    public void setMark(String mark){
        this.mark = mark;
    }
    public String getMark(){
        return this.mark;
    }
	public Integer getActiveDatetime() {
		return activeDatetime;
	}
	public void setActiveDatetime(Integer activeDatetime) {
		this.activeDatetime = activeDatetime;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Override
	public String toString() {
		return "TyOrgcustomer [orgName=" + orgName + ", serviceSaler="
				+ serviceSaler + ", custName=" + custName + ", custEmail="
				+ custEmail + ", custTel=" + custTel + ", custLevel="
				+ custLevel + ", salerId=" + salerId + ", orgId=" + orgId
				+ ", industry=" + industry + ", domain=" + domain + ", title="
				+ title + ", department=" + department + ", area=" + area
				+ ", custMobile=" + custMobile + ", custBakmobile="
				+ custBakmobile + ", mark=" + mark + ", activeDatetime=" 
				+ activeDatetime + ", idNo=" + idNo + "]";
	}
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}

}