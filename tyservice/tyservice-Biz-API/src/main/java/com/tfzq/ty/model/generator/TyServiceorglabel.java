package com.tfzq.ty.model.generator;

import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings("serial")
public class TyServiceorglabel extends BaseModel {

    private String orgId;
    private String labelId;
    private Double score;
    private Integer activeDate;
    public Integer getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Integer activeDate) {
		this.activeDate = activeDate;
	}

	private Integer validCount;
    
	public Integer getValidCount() {
		return validCount;
	}
	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
    public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setLabelId(String labelId){
        this.labelId = labelId;
    }
    public String getLabelId(){
        return this.labelId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}
