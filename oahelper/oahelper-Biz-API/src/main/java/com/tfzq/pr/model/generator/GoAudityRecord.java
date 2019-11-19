package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoAudityRecord extends BaseModel{
    private String audityId;
    private String userId;
    private String userName;
    private String isPassed;
    private String audityComment;
    private String audityPicture;

    public String getAudityPicture() {
		return audityPicture;
	}
	public void setAudityPicture(String audityPicture) {
		this.audityPicture = audityPicture;
	}
	public void setAudityId(String audityId){
        this.audityId = audityId;
    }
    public String getAudityId(){
        return this.audityId;
    }
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
    public void setIsPassed(String isPassed){
        this.isPassed = isPassed;
    }
    public String getIsPassed(){
        return this.isPassed;
    }
    public void setAudityComment(String audityComment){
        this.audityComment = audityComment;
    }
    public String getAudityComment(){
        return this.audityComment;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}