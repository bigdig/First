package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDcmorningcon extends BaseModel{
    private String conDate;
    private String speakTitle;
    private String flag;
    
    public void setConDate(String conDate){
        this.conDate = conDate;
    }
    public String getConDate(){
        return this.conDate;
    }
    public void setSpeakTitle(String speakTitle){
        this.speakTitle = speakTitle;
    }
    public String getSpeakTitle(){
        return this.speakTitle;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }
    public String getFlag(){
        return this.flag;
    }
    
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}