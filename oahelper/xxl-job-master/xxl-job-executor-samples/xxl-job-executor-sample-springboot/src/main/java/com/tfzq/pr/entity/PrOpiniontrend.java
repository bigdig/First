package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrOpiniontrend extends BaseModel{
    private String catNote;
    private String ymd;
    private String companyId;
    public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	private String dataAmount;
    private String busiDate;
    
    public void setCatNote(String catNote){
        this.catNote = catNote;
    }
    public String getCatNote(){
        return this.catNote;
    }
    public void setYmd(String ymd){
        this.ymd = ymd;
    }
    public String getYmd(){
        return this.ymd;
    }
    public void setDataAmount(String dataAmount){
        this.dataAmount = dataAmount;
    }
    public String getDataAmount(){
        return this.dataAmount;
    }
    public void setBusiDate(String busiDate){
        this.busiDate = busiDate;
    }
    public String getBusiDate(){
        return this.busiDate;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	@Override
	public String toString() {
		return "PrOpiniontrend [catNote=" + catNote + ", ymd=" + ymd + ", dataAmount=" + dataAmount + ", busiDate="
				+ busiDate + "]";
	}
    
}