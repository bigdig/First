package com.tfzq.ty.model.ty;

import java.util.List;

import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyProjectjour;

/**
 * @author pengtao
 */
@SuppressWarnings("serial")
public class TyProjectjourBean extends TyProjectjour {
	private String projectRoleText;
	private String projectTypeText;
	private String deleteFlagText;
	private String attachmentFlag;
	private List<TyListedcompany> companyList;
	private List<TyExpert> expertList;
	private List<TyOrgcustomer> customerList;

	private String createByName;
	private String updateByName;

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public String getAttachmentFlag() {
		return attachmentFlag;
	}

	public void setAttachmentFlag(String attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}

	public void setProjectRoleText(String projectRoleText) {
		this.projectRoleText = projectRoleText;
	}

	public String getProjectRoleText() {
		return this.projectRoleText;
	}

	public void setProjectTypeText(String projectTypeText) {
		this.projectTypeText = projectTypeText;
	}

	public String getProjectTypeText() {
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