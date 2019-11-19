package com.tfzq.tycrm.entity;
import com.ddfc.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivitystock extends BaseModel{
    private String activityId;
    private String stockId;
    private String actType;
    
    public void setActivityId(String activityId){
        this.activityId = activityId;
    }
    public String getActivityId(){
        return this.activityId;
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
    
}