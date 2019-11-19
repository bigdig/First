package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyProject;
import java.util.List;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyProjectBean extends TyProject{
    private String projectRoleText;
    private String projectTypeText;
    private String deleteFlagText;
    private String attachmentFlag;
    private List<TyListedcompany> companyList;
    private List<TyExpert> expertList;
    private List<TyOrgcustomer> customerList;
    
    private List<TyProjectjourBean> jours;
    private List<TyProjecttrackBean> tracks;
    
    public List<TyProjectjourBean> getJours() {
		return jours;
	}
	public void setJours(List<TyProjectjourBean> jours) {
		this.jours = jours;
	}
	public List<TyProjecttrackBean> getTracks() {
		return tracks;
	}
	public void setTracks(List<TyProjecttrackBean> tracks) {
		this.tracks = tracks;
	}
	public String getAttachmentFlag() {
		return attachmentFlag;
	}
	public void setAttachmentFlag(String attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}
	public void setProjectRoleText(String projectRoleText){
        this.projectRoleText = projectRoleText;
    }
    public String getProjectRoleText(){
        return this.projectRoleText;
    }
    public void setProjectTypeText(String projectTypeText){
        this.projectTypeText = projectTypeText;
    }
    public String getProjectTypeText(){
        return this.projectTypeText;
    }
	public String getDeleteFlagText() {
		return deleteFlagText;
	}
	public void setDeleteFlagText(String deleteFlagText) {
		this.deleteFlagText = deleteFlagText;
	}
	public List<TyListedcompany> getCompanyList() {
		return companyList;
	}
	public List<TyExpert> getExpertList() {
		return expertList;
	}
	public List<TyOrgcustomer> getCustomerList() {
		return customerList;
	}
	public void setCompanyList(List<TyListedcompany> companyList) {
		this.companyList = companyList;
	}
	public void setExpertList(List<TyExpert> expertList) {
		this.expertList = expertList;
	}
	public void setCustomerList(List<TyOrgcustomer> customerList) {
		this.customerList = customerList;
	}
	
}