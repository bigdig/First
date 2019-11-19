package com.tfzq.pr.entity;
import com.ddfc.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrWeektrend extends BaseModel{
    
    @Override
	public String toString() {
		return "PrWeektrend [amount=" + amount + ", ymd=" + ymd + ", busiDate=" + busiDate + "]";
	}

	private String amount;
    private String ymd;
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
    public void setYmd(String ymd){
        this.ymd = ymd;
    }
    public String getYmd(){
        return this.ymd;
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