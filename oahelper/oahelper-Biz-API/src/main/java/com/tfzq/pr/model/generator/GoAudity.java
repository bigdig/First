package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoAudity extends BaseModel{
    private String userId;
    private String userName;
    private String audityTitle;
    private String audityLink;
    private String audityPicture;
    private String audityStatus;
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setAudityTitle(String audityTitle){
        this.audityTitle = audityTitle;
    }
    public String getAudityTitle(){
        return this.audityTitle;
    }
    public void setAudityLink(String audityLink){
        this.audityLink = audityLink;
    }
    public String getAudityLink(){
        return this.audityLink;
    }
    public void setAudityPicture(String audityPicture){
        this.audityPicture = audityPicture;
    }
    public String getAudityPicture(){
        return this.audityPicture;
    }
    public void setAudityStatus(String audityStatus){
        this.audityStatus = audityStatus;
    }
    public String getAudityStatus(){
        return this.audityStatus;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}