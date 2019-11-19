package com.tfzq.tycrm.entity;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivitysign extends BaseModel{
    private String activityId;
    private String custName;
    private String orgSimpleName;
    private String orgName;
    private String custId;
    private String orgId;
    private String custMobile;
    private String inWhitelist;
    private String signId;
    private String signName;
    private String signStatus;
    private Integer signDate;
    private Integer signTime;
    private Integer arriveTime;
    private Integer leaveTime;
    private Integer totalDuration;
    private String deptId;
    
    
    public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public void setActivityId(String activityId){
        this.activityId = activityId;
    }
    public String getActivityId(){
        return this.activityId;
    }
    public void setCustName(String custName){
        this.custName = custName;
    }
    public String getCustName(){
        return this.custName;
    }
    public void setOrgSimpleName(String orgSimpleName){
        this.orgSimpleName = orgSimpleName;
    }
    public String getOrgSimpleName(){
        return this.orgSimpleName;
    }
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgName(){
        return this.orgName;
    }
    public void setCustId(String custId){
        this.custId = custId;
    }
    public String getCustId(){
        return this.custId;
    }
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    public String getOrgId(){
        return this.orgId;
    }
    public void setCustMobile(String custMobile){
        this.custMobile = custMobile;
    }
    public String getCustMobile(){
        return this.custMobile;
    }
    public void setInWhitelist(String inWhitelist){
        this.inWhitelist = inWhitelist;
    }
    public String getInWhitelist(){
        return this.inWhitelist;
    }
    public void setSignId(String signId){
        this.signId = signId;
    }
    public String getSignId(){
        return this.signId;
    }
    public void setSignName(String signName){
        this.signName = signName;
    }
    public String getSignName(){
        return this.signName;
    }
    public void setSignStatus(String signStatus){
        this.signStatus = signStatus;
    }
    public String getSignStatus(){
        return this.signStatus;
    }
    public void setSignDate(Integer signDate){
        this.signDate = signDate;
    }
    public Integer getSignDate(){
        return this.signDate;
    }
    public void setSignTime(Integer signTime){
        this.signTime = signTime;
    }
    public Integer getSignTime(){
        return this.signTime;
    }
    public void setArriveTime(Integer arriveTime){
        this.arriveTime = arriveTime;
    }
    public Integer getArriveTime(){
        return this.arriveTime;
    }
    public void setLeaveTime(Integer leaveTime){
        this.leaveTime = leaveTime;
    }
    public Integer getLeaveTime(){
        return this.leaveTime;
    }
    public void setTotalDuration(Integer totalDuration){
        this.totalDuration = totalDuration;
    }
    public Integer getTotalDuration(){
        return this.totalDuration;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}