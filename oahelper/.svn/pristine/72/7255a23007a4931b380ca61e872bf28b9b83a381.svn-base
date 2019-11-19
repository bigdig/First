package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoRemind extends BaseModel{
    private String remindTitle;
    private String remindType;
    private String isInformed;
    
    public void setRemindTitle(String remindTitle){
        this.remindTitle = remindTitle;
    }
    public String getRemindTitle(){
        return this.remindTitle;
    }
    public void setRemindType(String remindType){
        this.remindType = remindType;
    }
    public String getRemindType(){
        return this.remindType;
    }
    public void setIsInformed(String isInformed){
        this.isInformed = isInformed;
    }
    public String getIsInformed(){
        return this.isInformed;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}