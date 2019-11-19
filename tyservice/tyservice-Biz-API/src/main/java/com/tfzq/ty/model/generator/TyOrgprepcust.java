package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyOrgprepcust extends BaseModel{
    private String orgName;
    private String serviceSaler;
    private String custName;
    private String industry;
    private String title;
    private String area;
    private String custEmail;
    private String custMobile;
    private String customerId;
    private String prepcustStatus;
    private String existMobileFlag;
    private String mark;
    
    
    
    public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getExistMobileFlag() {
		return existMobileFlag;
	}
	public void setExistMobileFlag(String existMobileFlag) {
		this.existMobileFlag = existMobileFlag;
	}
	public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgName(){
        return this.orgName;
    }
    public void setServiceSaler(String serviceSaler){
        this.serviceSaler = serviceSaler;
    }
    public String getServiceSaler(){
        return this.serviceSaler;
    }
    public void setCustName(String custName){
        this.custName = custName;
    }
    public String getCustName(){
        return this.custName;
    }
    public void setIndustry(String industry){
        this.industry = industry;
    }
    public String getIndustry(){
        return this.industry;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setArea(String area){
        this.area = area;
    }
    public String getArea(){
        return this.area;
    }
    public void setCustEmail(String custEmail){
        this.custEmail = custEmail;
    }
    public String getCustEmail(){
        return this.custEmail;
    }
    public void setCustMobile(String custMobile){
        this.custMobile = custMobile;
    }
    public String getCustMobile(){
        return this.custMobile;
    }
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    public String getCustomerId(){
        return this.customerId;
    }
    public void setPrepcustStatus(String prepcustStatus){
        this.prepcustStatus = prepcustStatus;
    }
    public String getPrepcustStatus(){
        return this.prepcustStatus;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}