package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDctopiccust extends BaseModel{
    private String topicId;
    private String custId;
    
    public void setTopicId(String topicId){
        this.topicId = topicId;
    }
    public String getTopicId(){
        return this.topicId;
    }
    public void setCustId(String custId){
        this.custId = custId;
    }
    public String getCustId(){
        return this.custId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}