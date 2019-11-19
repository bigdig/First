package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoRemindInform extends BaseModel{
    private String remindId;
    private String remindTitle;
    private String remindContent;
    private String remindStatus;
    private String userId;
    private String userName;
    
    public void setRemindId(String remindId){
        this.remindId = remindId;
    }
    public String getRemindId(){
        return this.remindId;
    }
    public void setRemindTitle(String remindTitle){
        this.remindTitle = remindTitle;
    }
    public String getRemindTitle(){
        return this.remindTitle;
    }
    public void setRemindContent(String remindContent){
        this.remindContent = remindContent;
    }
    public String getRemindContent(){
        return this.remindContent;
    }
    public void setRemindStatus(String remindStatus){
        this.remindStatus = remindStatus;
    }
    public String getRemindStatus(){
        return this.remindStatus;
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
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}