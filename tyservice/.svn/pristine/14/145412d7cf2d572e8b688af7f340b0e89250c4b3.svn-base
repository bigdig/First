package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDccallcust extends BaseModel{
    private String custName;
    private String orgSplname;
    private String custMobile;
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    @JSONField (format="yyyy-MM-dd")
    private Date arriveTime;
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    @JSONField (format="yyyy-MM-dd")
    private Date leaveTime;
    private Integer attendTime;
    private String callId;
    private String inWhitelist;
    private String custId;
    
    public void setCustName(String custName){
        this.custName = custName;
    }
    public String getCustName(){
        return this.custName;
    }
    public void setOrgSplname(String orgSplname){
        this.orgSplname = orgSplname;
    }
    public String getOrgSplname(){
        return this.orgSplname;
    }
    public void setCustMobile(String custMobile){
        this.custMobile = custMobile;
    }
    public String getCustMobile(){
        return this.custMobile;
    }
    public void setArriveTime(Date arriveTime){
        this.arriveTime = arriveTime;
    }
    public Date getArriveTime(){
        return this.arriveTime;
    }
    public void setLeaveTime(Date leaveTime){
        this.leaveTime = leaveTime;
    }
    public Date getLeaveTime(){
        return this.leaveTime;
    }
    public void setAttendTime(Integer attendTime){
        this.attendTime = attendTime;
    }
    public Integer getAttendTime(){
        return this.attendTime;
    }
    public void setCallId(String callId){
        this.callId = callId;
    }
    public String getCallId(){
        return this.callId;
    }
    public void setInWhitelist(String inWhitelist){
        this.inWhitelist = inWhitelist;
    }
    public String getInWhitelist(){
        return this.inWhitelist;
    }
    public void setCustId(String custId){
        this.custId = custId;
    }
    public String getCustId(){
        return this.custId;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}