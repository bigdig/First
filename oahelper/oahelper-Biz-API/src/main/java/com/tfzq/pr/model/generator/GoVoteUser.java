package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoVoteUser extends BaseModel{
    private String voteId;
    private String userId;
    private String userName;
    private String voteItemId;
    private String voteItemContent;
    
    public void setVoteId(String voteId){
        this.voteId = voteId;
    }
    public String getVoteId(){
        return this.voteId;
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
    public void setVoteItemId(String voteItemId){
        this.voteItemId = voteItemId;
    }
    public String getVoteItemId(){
        return this.voteItemId;
    }
    public void setVoteItemContent(String voteItemContent){
        this.voteItemContent = voteItemContent;
    }
    public String getVoteItemContent(){
        return this.voteItemContent;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}