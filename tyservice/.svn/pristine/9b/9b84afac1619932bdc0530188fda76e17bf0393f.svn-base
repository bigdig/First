package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyCfundstock extends BaseModel{
    private String windcode;
    private String stockwindcode;
    private String stockname;
    private Double stkvaluetonav;
    
    public void setWindcode(String windcode){
        this.windcode = windcode;
    }
    public String getWindcode(){
        return this.windcode;
    }
    public void setStockwindcode(String stockwindcode){
        this.stockwindcode = stockwindcode;
    }
    public String getStockwindcode(){
        return this.stockwindcode;
    }
    public void setStockname(String stockname){
        this.stockname = stockname;
    }
    public String getStockname(){
        return this.stockname;
    }
    public void setStkvaluetonav(Double stkvaluetonav){
        this.stkvaluetonav = stkvaluetonav;
    }
    public Double getStkvaluetonav(){
        return this.stkvaluetonav;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}