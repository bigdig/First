package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivitylistedcomp extends BaseModel{
    private String activityId;
    private String listedcompId;
    private String stockCode;
    private String stockName;
    
    public void setActivityId(String activityId){
        this.activityId = activityId;
    }
    public String getActivityId(){
        return this.activityId;
    }
    public void setListedcompId(String listedcompId){
        this.listedcompId = listedcompId;
    }
    public String getListedcompId(){
        return this.listedcompId;
    }
    public void setStockCode(String stockCode){
        this.stockCode = stockCode;
    }
    public String getStockCode(){
        return this.stockCode;
    }
    public void setStockName(String stockName){
        this.stockName = stockName;
    }
    public String getStockName(){
        return this.stockName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}