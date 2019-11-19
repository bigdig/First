package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivityexpert extends BaseModel{
    private String activityId;
    private String expertId;
    private String expertName;
    private String orgId;
    
    public void setActivityId(String activityId){
        this.activityId = activityId;
    }
    public String getActivityId(){
        return this.activityId;
    }
    public void setExpertId(String expertId){
        this.expertId = expertId;
    }
    public String getExpertId(){
        return this.expertId;
    }
    public void setExpertName(String expertName){
        this.expertName = expertName;
    }
    public String getExpertName(){
        return this.expertName;
    }
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    public String getOrgId(){
        return this.orgId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}