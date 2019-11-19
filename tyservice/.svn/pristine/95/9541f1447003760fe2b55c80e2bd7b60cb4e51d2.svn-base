package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyLabel extends BaseModel{
    private String catId;		//类别id
    private String catName;
    private String labelName;
    private String orderNum;
    private String preId;
    private String nextId;
    private String labelLevel;  //标签等级
    private String pid;   //父标签id
    
    public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLabelLevel() {
		return labelLevel;
	}
	public void setLabelLevel(String labelLevel) {
		this.labelLevel = labelLevel;
	}
	public void setCatId(String catId){
        this.catId = catId;
    }
    public String getCatId(){
        return this.catId;
    }
    public void setCatName(String catName){
        this.catName = catName;
    }
    public String getCatName(){
        return this.catName;
    }
    public void setLabelName(String labelName){
        this.labelName = labelName;
    }
    public String getLabelName(){
        return this.labelName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getPreId() {
		return preId;
	}
	public void setPreId(String preId) {
		this.preId = preId;
	}
	public String getNextId() {
		return nextId;
	}
	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
    
}