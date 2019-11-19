package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyOrgcustomerpush extends BaseModel{
    private String customerId;
    private String platFlag;
    private String dealFlag;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPlatFlag() {
		return platFlag;
	}
	public void setPlatFlag(String platFlag) {
		this.platFlag = platFlag;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
    
    
}