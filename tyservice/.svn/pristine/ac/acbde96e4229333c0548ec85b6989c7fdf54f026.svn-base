package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDcactivity extends BaseModel{
    private String actType;
    private String actId;
    private String userid;
    
    public void setActType(String actType){
        this.actType = actType;
    }
    public String getActType(){
        return this.actType;
    }
    public void setActId(String actId){
        this.actId = actId;
    }
    public String getActId(){
        return this.actId;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public String getUserid(){
        return this.userid;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}