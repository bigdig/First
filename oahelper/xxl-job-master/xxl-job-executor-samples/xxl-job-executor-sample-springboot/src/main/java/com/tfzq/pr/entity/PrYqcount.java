package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrYqcount extends BaseModel{
    private String amount;
    private String amount30;
    private String amount7;
    private String catId;
    private String catName;
    private String companyId;
    public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	private String busiDate;
    
    public void setAmount(String amount){
        this.amount = amount;
    }
    public String getAmount(){
        return this.amount;
    }
    public void setAmount30(String amount30){
        this.amount30 = amount30;
    }
    public String getAmount30(){
        return this.amount30;
    }
    public void setAmount7(String amount7){
        this.amount7 = amount7;
    }
    public String getAmount7(){
        return this.amount7;
    }
    public void setCatId(String catId){
        this.catId = catId;
    }
    public String getCatId(){
        return this.catId;
    }
    public void setCatName(String catName){
        this.catName = catName;
    }
    public String getCatName(){
        return this.catName;
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
		return "PrYqcount [amount=" + amount + ", amount30=" + amount30 + ", amount7=" + amount7 + ", catId=" + catId
				+ ", catName=" + catName + ", busiDate=" + busiDate + "]";
	}
    
}