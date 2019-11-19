package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyAppsecret extends BaseModel{
    private String platSrc;
    private String companyId;
    private String secretCode;
    
    public void setPlatSrc(String platSrc){
        this.platSrc = platSrc;
    }
    public String getPlatSrc(){
        return this.platSrc;
    }
    public void setSecretCode(String secretCode){
        this.secretCode = secretCode;
    }
    public String getSecretCode(){
        return this.secretCode;
    }
    public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}