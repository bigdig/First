package com.tfzq.ty.model.ty;
import java.util.ArrayList;
import java.util.List;

import com.tfzq.ty.model.generator.TyCustomerlabel;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyCustomerlabelBean extends TyCustomerlabel{
    private String catId;		//类别id
    private String catName;
    private String labelName;
    private String pid;   //父标签id
    private Integer validCount ; //有效次数
    private Double daysPower; //平滑递减值

    private List<TyCustomerlabelBean> children= new ArrayList<TyCustomerlabelBean>();
    
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<TyCustomerlabelBean> getChildren() {
		return children;
	}
	public void setChildren(List<TyCustomerlabelBean> children) {
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
	public Integer getValidCount() {
		return validCount;
	}
	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}
	public Double getDaysPower() {
		return daysPower;
	}
	public void setDaysPower(Double daysPower) {
		this.daysPower = daysPower;
	}
	
}