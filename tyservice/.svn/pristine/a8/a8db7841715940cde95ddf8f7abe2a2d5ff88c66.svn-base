package org.ibase4j.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class SysArea extends BaseModel{
    private String areaCode;
    private String areaName;
    
    public void setAreaCode(String areaCode){
        this.areaCode = areaCode;
    }
    public String getAreaCode(){
        return this.areaCode;
    }
    public void setAreaName(String areaName){
        this.areaName = areaName;
    }
    public String getAreaName(){
        return this.areaName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}