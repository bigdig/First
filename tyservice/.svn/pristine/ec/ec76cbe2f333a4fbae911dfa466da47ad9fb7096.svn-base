package org.ibase4j.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class SysPosition extends BaseModel{
    private String positionName;
    
    public void setPositionName(String positionName){
        this.positionName = positionName;
    }
    public String getPositionName(){
        return this.positionName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}