package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDccall extends BaseModel{
    private String callDate;
    private String callTitle;
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    @JSONField (format="yyyy-MM-dd")
    private Date beginTime;
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    @JSONField (format="yyyy-MM-dd")
    private Date endTime;
    private Integer personNum;
    private Integer totalDuration;
    private Integer avgDuration;
    
    public void setCallDate(String callDate){
        this.callDate = callDate;
    }
    public String getCallDate(){
        return this.callDate;
    }
    public void setCallTitle(String callTitle){
        this.callTitle = callTitle;
    }
    public String getCallTitle(){
        return this.callTitle;
    }
    public void setBeginTime(Date beginTime){
        this.beginTime = beginTime;
    }
    public Date getBeginTime(){
        return this.beginTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }
    public void setPersonNum(Integer personNum){
        this.personNum = personNum;
    }
    public Integer getPersonNum(){
        return this.personNum;
    }
    public void setTotalDuration(Integer totalDuration){
        this.totalDuration = totalDuration;
    }
    public Integer getTotalDuration(){
        return this.totalDuration;
    }
    public void setAvgDuration(Integer avgDuration){
        this.avgDuration = avgDuration;
    }
    public Integer getAvgDuration(){
        return this.avgDuration;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}