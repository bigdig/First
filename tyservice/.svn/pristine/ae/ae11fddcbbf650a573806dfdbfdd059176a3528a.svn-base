package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyCfund extends BaseModel{
    private String windcode;
    private String fullname;
    private String shortname;
    
    public void setWindcode(String windcode){
        this.windcode = windcode;
    }
    public String getWindcode(){
        return this.windcode;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public String getFullname(){
        return this.fullname;
    }
    public void setShortname(String shortname){
        this.shortname = shortname;
    }
    public String getShortname(){
        return this.shortname;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}