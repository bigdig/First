package com.tfzq.ty.model.ty;
import java.util.List;

import com.tfzq.ty.model.generator.TyDccall;
import com.tfzq.ty.model.generator.TyStafflist;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDccallBean extends TyDccall{
    
	private List<TyStafflist> staffList;
	private List<TyDccallcustBean> custList;
	
	public List<TyStafflist> getStaffList() {
		return staffList;
	}
	public void setStaffList(List<TyStafflist> staffList) {
		this.staffList = staffList;
	}
	public List<TyDccallcustBean> getCustList() {
		return custList;
	}
	public void setCustList(List<TyDccallcustBean> custList) {
		this.custList = custList;
	}
	
}