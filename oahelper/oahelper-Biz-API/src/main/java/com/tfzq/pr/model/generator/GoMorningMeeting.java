package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoMorningMeeting extends BaseModel{
    private String meetingDate;
    private String meetingContent;
    private String informId;
    
    public void setMeetingDate(String meetingDate){
        this.meetingDate = meetingDate;
    }
    public String getMeetingDate(){
        return this.meetingDate;
    }
    public void setMeetingContent(String meetingContent){
        this.meetingContent = meetingContent;
    }
    public String getMeetingContent(){
        return this.meetingContent;
    }
    public void setInformId(String informId){
        this.informId = informId;
    }
    public String getInformId(){
        return this.informId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}