package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.generator.TyServiceorg;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyOrgprepcustBean extends TyOrgprepcust{
    private String prepcustStatusText;
    private String existMobileFlagText;
    
	public String getExistMobileFlagText() {
		return existMobileFlagText;
	}
	public void setExistMobileFlagText(String existMobileFlagText) {
		this.existMobileFlagText = existMobileFlagText;
	}
	public void setPrepcustStatusText(String prepcustStatusText){
        this.prepcustStatusText = prepcustStatusText;
    }
    public String getPrepcustStatusText(){
        return this.prepcustStatusText;
    }
}