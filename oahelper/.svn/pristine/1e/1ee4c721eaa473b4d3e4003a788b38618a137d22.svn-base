package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoInformUser extends BaseModel{
    private String informId;
    private String userId;
    private String userName;
    private String isRead;
    private String isReplied;
    private String isSend;

    
    public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	public void setInformId(String informId){
        this.informId = informId;
    }
    public String getInformId(){
        return this.informId;
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
    public void setIsRead(String isRead){
        this.isRead = isRead;
    }
    public String getIsRead(){
        return this.isRead;
    }
    public void setIsReplied(String isReplied){
        this.isReplied = isReplied;
    }
    public String getIsReplied(){
        return this.isReplied;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}