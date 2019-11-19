package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDctopic extends BaseModel{
    private String topicDate;
    private String topicType;
    private String topicContent;
    private String orgId;
    
    public void setTopicDate(String topicDate){
        this.topicDate = topicDate;
    }
    public String getTopicDate(){
        return this.topicDate;
    }
    public void setTopicType(String topicType){
        this.topicType = topicType;
    }
    public String getTopicType(){
        return this.topicType;
    }
    public void setTopicContent(String topicContent){
        this.topicContent = topicContent;
    }
    public String getTopicContent(){
        return this.topicContent;
    }
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    public String getOrgId(){
        return this.orgId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}