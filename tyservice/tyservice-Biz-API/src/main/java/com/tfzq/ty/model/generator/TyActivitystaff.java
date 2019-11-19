package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivitystaff extends BaseModel{
    private String activityId;
    private String staffId;
    private String staffName;
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
    public void setStaffId(String staffId){
        this.staffId = staffId;
    }
    public String getStaffId(){
        return this.staffId;
    }
    public void setStaffName(String staffName){
        this.staffName = staffName;
    }
    public String getStaffName(){
        return this.staffName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}