package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDcsalon extends BaseModel{
    private String salonDate;
    private String salonTitle;
    private String salonArea;
    
    public void setSalonDate(String salonDate){
        this.salonDate = salonDate;
    }
    public String getSalonDate(){
        return this.salonDate;
    }
    public void setSalonTitle(String salonTitle){
        this.salonTitle = salonTitle;
    }
    public String getSalonTitle(){
        return this.salonTitle;
    }
    public void setSalonArea(String salonArea){
        this.salonArea = salonArea;
    }
    public String getSalonArea(){
        return this.salonArea;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}