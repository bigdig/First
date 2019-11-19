package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrMediaspread extends BaseModel{
    private String amount;
    private String catId;
    private String catNote;
    private String percent;
    private String companyId;
    public String getCompanyID() {
		return companyId;
	}
	public void setCompanyID(String companyID) {
		this.companyId = companyID;
	}
	private String busiDate;
    
    public void setAmount(String amount){
        this.amount = amount;
    }
    public String getAmount(){
        return this.amount;
    }
    public void setCatId(String catId){
        this.catId = catId;
    }
    public String getCatId(){
        return this.catId;
    }
    public void setCatNote(String catNote){
        this.catNote = catNote;
    }
    public String getCatNote(){
        return this.catNote;
    }
    public void setPercent(String percent){
        this.percent = percent;
    }
    public String getPercent(){
        return this.percent;
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
		return "PrMediaspread [amount=" + amount + ", catId=" + catId + ", catNote=" + catNote + ", percent=" + percent
				+ ", busiDate=" + busiDate + "]";
	}
    
}