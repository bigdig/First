package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrEmotion extends BaseModel{
    @Override
	public String toString() {
		return "PrEmotion [amount=" + amount + ", catId=" + catId + ", catNote=" + catNote + ", companyId=" + companyId
				+ ", busiDate=" + busiDate + "]";
	}

	private String amount;
    private String catId;
    private String catNote;
    private String companyId;
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
    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public String getCompanyId(){
        return this.companyId;
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
}