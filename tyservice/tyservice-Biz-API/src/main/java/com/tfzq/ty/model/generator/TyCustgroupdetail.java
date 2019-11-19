package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyCustgroupdetail extends BaseModel{
    private String groupId;
    private String custId;
    
    public void setGroupId(String groupId){
        this.groupId = groupId;
    }
    public String getGroupId(){
        return this.groupId;
    }
    public void setCustId(String custId){
        this.custId = custId;
    }
    public String getCustId(){
        return this.custId;
    }
    
}