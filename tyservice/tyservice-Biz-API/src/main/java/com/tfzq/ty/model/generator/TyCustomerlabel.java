package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyCustomerlabel extends BaseModel{
    private String customerId;
    private String labelId;
    private Double score;
    private Integer activeDate;
    private String labelName;
	private Integer validCount;

    public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public Integer getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Integer activeDate) {
		this.activeDate = activeDate;
	}

    
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
	public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    public String getCustomerId(){
        return this.customerId;
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