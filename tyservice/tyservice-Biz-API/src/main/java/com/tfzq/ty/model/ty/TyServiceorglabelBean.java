package com.tfzq.ty.model.ty;

import java.util.ArrayList;
import java.util.List;

import com.tfzq.ty.model.generator.TyServiceorglabel;

public class TyServiceorglabelBean extends TyServiceorglabel {
    private String catId;		//类别id
    private String catName;
    private String labelName;
    private String pid;   //父标签id

    private List<TyServiceorglabelBean> children= new ArrayList<TyServiceorglabelBean>();
    
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public List<TyServiceorglabelBean> getChildren() {
		return children;
	}
	public void setChildren(List<TyServiceorglabelBean> children) {
		this.children = children;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
}
