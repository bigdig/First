package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyProject extends BaseModel{
    private String projectRole;
    private String projectType;
    private String projectName;
    private String projectDemand;
    private String brokerId;
    private String brokerName;
    private String deptName;
    private String expertStaffId;
    private String expertStaffName;
    private String attachment;
    private String attachName;
    private String companyId;
    private String companyName;
    private String orgCustId;
    private String orgCustName;
    private Integer endTime;
    private Integer beginTime;
    
    public String getAttachName() {
		return attachName;
	}
	public void setAttachNames(String attachName) {
		this.attachName = attachName;
	}
	public void setProjectRole(String projectRole){
        this.projectRole = projectRole;
    }
    public String getProjectRole(){
        return this.projectRole;
    }
    public void setProjectType(String projectType){
        this.projectType = projectType;
    }
    public String getProjectType(){
        return this.projectType;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public String getProjectName(){
        return this.projectName;
    }
    public void setProjectDemand(String projectDemand){
        this.projectDemand = projectDemand;
    }
    public String getProjectDemand(){
        return this.projectDemand;
    }
    public void setBrokerId(String brokerId){
        this.brokerId = brokerId;
    }
    public String getBrokerId(){
        return this.brokerId;
    }
    public void setBrokerName(String brokerName){
        this.brokerName = brokerName;
    }
    public String getBrokerName(){
        return this.brokerName;
    }
    public void setDeptName(String deptName){
        this.deptName = deptName;
    }
    public String getDeptName(){
        return this.deptName;
    }
    public void setExpertStaffId(String expertStaffId){
        this.expertStaffId = expertStaffId;
    }
    public String getExpertStaffId(){
        return this.expertStaffId;
    }
    public void setExpertStaffName(String expertStaffName){
        this.expertStaffName = expertStaffName;
    }
    public String getExpertStaffName(){
        return this.expertStaffName;
    }
    public void setAttachment(String attachment){
        this.attachment = attachment;
    }
    public String getAttachment(){
        return this.attachment;
    }
    public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOrgCustId() {
		return orgCustId;
	}
	public void setOrgCustId(String orgCustId) {
		this.orgCustId = orgCustId;
	}
	public String getOrgCustName() {
		return orgCustName;
	}
	public void setOrgCustName(String orgCustName) {
		this.orgCustName = orgCustName;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}	
	public Integer getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Integer beginTime) {
		this.beginTime = beginTime;
	}
	
	@JSONField( format = "yyyy-MM-dd" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	@JSONField( format = "yyyy-MM-dd" )
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}
}