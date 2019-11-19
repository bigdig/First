package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoVoteGroup extends BaseModel{
    private String voteGroupName;
    private String voteGroupDescription;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String isOpen;
    private String isActivity;
    
    public void setVoteGroupName(String voteGroupName){
        this.voteGroupName = voteGroupName;
    }
    public String getVoteGroupName(){
        return this.voteGroupName;
    }
    public void setVoteGroupDescription(String voteGroupDescription){
        this.voteGroupDescription = voteGroupDescription;
    }
    public String getVoteGroupDescription(){
        return this.voteGroupDescription;
    }
    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }
    public Date getStartTime(){
        return this.startTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }
    public void setIsOpen(String isOpen){
        this.isOpen = isOpen;
    }
    public String getIsOpen(){
        return this.isOpen;
    }
    public void setIsActivity(String isActivity){
        this.isActivity = isActivity;
    }
    public String getIsActivity(){
        return this.isActivity;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}