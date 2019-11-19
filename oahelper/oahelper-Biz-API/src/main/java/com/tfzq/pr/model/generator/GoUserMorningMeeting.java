package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoUserMorningMeeting extends BaseModel{
    private String meetingDate;
    private String informId;
    private String userId;
    private String userName;
    private String attendenceWay;
    private String presentWay;
    private String absenceWay;
    private String absencePicture;
    
    public void setMeetingDate(String meetingDate){
        this.meetingDate = meetingDate;
    }
    public String getMeetingDate(){
        return this.meetingDate;
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
    public void setAttendenceWay(String attendenceWay){
        this.attendenceWay = attendenceWay;
    }
    public String getAttendenceWay(){
        return this.attendenceWay;
    }
    public void setPresentWay(String presentWay){
        this.presentWay = presentWay;
    }
    public String getPresentWay(){
        return this.presentWay;
    }
    public void setAbsenceWay(String absenceWay){
        this.absenceWay = absenceWay;
    }
    public String getAbsenceWay(){
        return this.absenceWay;
    }
    public void setAbsencePicture(String absencePicture){
        this.absencePicture = absencePicture;
    }
    public String getAbsencePicture(){
        return this.absencePicture;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}