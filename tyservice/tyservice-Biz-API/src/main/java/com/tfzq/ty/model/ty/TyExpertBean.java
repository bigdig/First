package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyExpert;
import java.util.List;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyExpertBean extends TyExpert{
    private String expertTypeText;
    private String closeLevelText;
    private String expertProjectRoleText;
    private String industryNum;
    private String closeLevelNum;
    private String deleteFlagText;
    
    public void setExpertTypeText(String expertTypeText){
        this.expertTypeText = expertTypeText;
    }
    public String getExpertTypeText(){
        return this.expertTypeText;
    }
    public void setCloseLevelText(String closeLevelText){
        this.closeLevelText = closeLevelText;
    }
    public String getCloseLevelText(){
        return this.closeLevelText;
    }
	public String getIndustryNum() {
		return industryNum;
	}
	public void setIndustryNum(String industryNum) {
		this.industryNum = industryNum;
	}
	public String getCloseLevelNum() {
		return closeLevelNum;
	}
	public void setCloseLevelNum(String closeLevelNum) {
		this.closeLevelNum = closeLevelNum;
	}
	public String getExpertProjectRoleText() {
		return expertProjectRoleText;
	}
	public void setExpertProjectRoleText(String expertProjectRoleText) {
		this.expertProjectRoleText = expertProjectRoleText;
	}
	public String getDeleteFlagText() {
		return deleteFlagText;
	}
	public void setDeleteFlagText(String deleteFlagText) {
		this.deleteFlagText = deleteFlagText;
	}
}