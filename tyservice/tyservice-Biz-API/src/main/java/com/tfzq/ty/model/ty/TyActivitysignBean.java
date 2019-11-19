package com.tfzq.ty.model.ty;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyStafflist;

import java.util.List;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivitysignBean extends TyActivitysign{
    private String signStatusText;
	private String serviceSaler;
	private String area;
	private String title; 
	private String activityTypeText;
	private Integer perLimit;
	private String priorityLevel;
	private String actCreateName; // 活动创建人姓名
	private String actCreatePosi; //活动创建人的职业
	
	private TyActivity tyActivity;
	private List<TyStafflist> staffList;
	

	public String getActCreatePosi() {
		return actCreatePosi;
	}
	public void setActCreatePosi(String actCreatePosi) {
		this.actCreatePosi = actCreatePosi;
	}
	public String getActCreateName() {
		return actCreateName;
	}
	public void setActCreateName(String actCreateName) {
		this.actCreateName = actCreateName;
	}
	public String getServiceSaler() {
		return serviceSaler;
	}
	public void setServiceSaler(String serviceSaler) {
		this.serviceSaler = serviceSaler;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public void setSignStatusText(String signStatusText){
        this.signStatusText = signStatusText;
    }
    public String getSignStatusText(){
        return this.signStatusText;
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getActivityTypeText() {
		return activityTypeText;
	}
	public void setActivityTypeText(String activityTypeText) {
		this.activityTypeText = activityTypeText;
	}
	public Integer getPerLimit() {
		return perLimit;
	}
	public void setPerLimit(Integer perLimit) {
		this.perLimit = perLimit;
	}
	public String getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	public TyActivity getTyActivity() {
		return tyActivity;
	}
	public void setTyActivity(TyActivity tyActivity) {
		this.tyActivity = tyActivity;
	}
	public List<TyStafflist> getStaffList() {
		return staffList;
	}
	public void setStaffList(List<TyStafflist> staffList) {
		this.staffList = staffList;
	}
	
}