package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyLabelnet extends BaseModel{
    private String labelIdFrom;
    private String labelIdTo;
    private String type;
    
    
    
    
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setLabelIdFrom(String labelIdFrom){
        this.labelIdFrom = labelIdFrom;
    }
    public String getLabelIdFrom(){
        return this.labelIdFrom;
    }
    public void setLabelIdTo(String labelIdTo){
        this.labelIdTo = labelIdTo;
    }
    public String getLabelIdTo(){
        return this.labelIdTo;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}