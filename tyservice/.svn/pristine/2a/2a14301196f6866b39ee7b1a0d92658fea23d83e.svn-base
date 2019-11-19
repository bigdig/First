package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDcmorningstock extends BaseModel{
    private String speakId;
    private String stockId;
    private String actType;
    
    public void setSpeakId(String speakId){
        this.speakId = speakId;
    }
    public String getSpeakId(){
        return this.speakId;
    }
    public void setStockId(String stockId){
        this.stockId = stockId;
    }
    public String getStockId(){
        return this.stockId;
    }
    public void setActType(String actType){
        this.actType = actType;
    }
    public String getActType(){
        return this.actType;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}