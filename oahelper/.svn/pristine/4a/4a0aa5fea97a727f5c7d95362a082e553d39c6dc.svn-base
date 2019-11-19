package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrMonitoring extends BaseModel{
    private String dataType;
    private String dataText;
    
    public void setDataType(String dataType){
        this.dataType = dataType;
    }
    public String getDataType(){
        return this.dataType;
    }
    public void setDataText(String dataText){
        this.dataText = dataText;
    }
    public String getDataText(){
        return this.dataText;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	@Override
	public String toString() {
		return "PrMonitoring [dataType=" + dataType + ", dataText=" + dataText + "]";
	}
}