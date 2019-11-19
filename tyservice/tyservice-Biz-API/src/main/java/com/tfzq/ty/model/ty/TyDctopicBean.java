package com.tfzq.ty.model.ty;
import java.util.List;

import com.tfzq.ty.model.generator.TyDctopic;
import com.tfzq.ty.model.generator.TyStafflist;

/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyDctopicBean extends TyDctopic{
	
	private List<TyStafflist> staffList;
	private List<TyDctopiccustBean> topicCustList;
    private String topicTypeText;
    private String orgName;
    private String teamCat;
    private String serviceSaler;
    
    public void setTopicTypeText(String topicTypeText){
        this.topicTypeText = topicTypeText;
    }
    public String getTopicTypeText(){
        return this.topicTypeText;
    }
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgName(){
        return this.orgName;
    }
	public List<TyStafflist> getStaffList() {
		return staffList;
	}
	public void setStaffList(List<TyStafflist> staffList) {
		this.staffList = staffList;
	}
	public String getTeamCat() {
		return teamCat;
	}
	public void setTeamCat(String teamCat) {
		this.teamCat = teamCat;
	}
	public String getServiceSaler() {
		return serviceSaler;
	}
	public void setServiceSaler(String serviceSaler) {
		this.serviceSaler = serviceSaler;
	}
	public List<TyDctopiccustBean> getTopicCustList() {
		return topicCustList;
	}
	public void setTopicCustList(List<TyDctopiccustBean> topicCustList) {
		this.topicCustList = topicCustList;
	}
    
}