package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDcsaloncust extends BaseModel{
    private String custId;
    private String salonId;
    
    public void setCustId(String custId){
        this.custId = custId;
    }
    public String getCustId(){
        return this.custId;
    }
    public void setSalonId(String salonId){
        this.salonId = salonId;
    }
    public String getSalonId(){
        return this.salonId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}