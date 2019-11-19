package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyActivity extends BaseModel{
    private String title;
    private String content;
    private Integer beginDate;
    private Integer beginTime;
    private Integer endDate;
    private Integer endTime;
    private Integer totalDuration;
    private Integer avgDuration;
    private String activityType;
    private String priorityLevel;
    private Integer totalLimit;
    private Integer perLimit;
    private String locale;
    private String labels;
    private String activityStatus;
    private String topicType;
    private String attachs;
    private String attachsName;
    private String dataSrc;
    private String dataSrcname;
    private String orgId;
    private String createName;
    private String contactId;
    private String detailAddr;
    private String contactName;
    private String activityCate;
    private String createDeptId;
    private String contactDeptId;
    private String subActFlag;
    private String parentActId;
    
    
    public String getSubActFlag() {
		return subActFlag;
	}
	public void setSubActFlag(String subActFlag) {
		this.subActFlag = subActFlag;
	}
	public String getParentActId() {
		return parentActId;
	}
	public void setParentActId(String parentActId) {
		this.parentActId = parentActId;
	}

	private String notifyFlag;
	public String getNotifyFlag() {
		return notifyFlag;
	}
	public void setNotifyFlag(String notifyFlag) {
		this.notifyFlag = notifyFlag;
	}
	public String getCreateDeptId() {
		return createDeptId;
	}
	public void setCreateDeptId(String createDeptId) {
		this.createDeptId = createDeptId;
	}
	public String getContactDeptId() {
		return contactDeptId;
	}
	public void setContactDeptId(String contactDeptId) {
		this.contactDeptId = contactDeptId;
	}
	public String getActivityCate() {
		return activityCate;
	}
	public void setActivityCate(String activityCate) {
		this.activityCate = activityCate;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getDetailAddr() {
		return detailAddr;
	}
	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setBeginDate(Integer beginDate){
        this.beginDate = beginDate;
    }
    public Integer getBeginDate(){
        return this.beginDate;
    }
    public void setBeginTime(Integer beginTime){
        this.beginTime = beginTime;
    }
    public Integer getBeginTime(){
        return this.beginTime;
    }
    public void setEndDate(Integer endDate){
        this.endDate = endDate;
    }
    public Integer getEndDate(){
        return this.endDate;
    }
    public void setEndTime(Integer endTime){
        this.endTime = endTime;
    }
    public Integer getEndTime(){
        return this.endTime;
    }
    public void setTotalDuration(Integer totalDuration){
        this.totalDuration = totalDuration;
    }
    public Integer getTotalDuration(){
        return this.totalDuration;
    }
    public void setAvgDuration(Integer avgDuration){
        this.avgDuration = avgDuration;
    }
    public Integer getAvgDuration(){
        return this.avgDuration;
    }
    public void setActivityType(String activityType){
        this.activityType = activityType;
    }
    public String getActivityType(){
        return this.activityType;
    }
    public void setPriorityLevel(String priorityLevel){
        this.priorityLevel = priorityLevel;
    }
    public String getPriorityLevel(){
        return this.priorityLevel;
    }
    public void setTotalLimit(Integer totalLimit){
        this.totalLimit = totalLimit;
    }
    public Integer getTotalLimit(){
        return this.totalLimit;
    }
    public void setPerLimit(Integer perLimit){
        this.perLimit = perLimit;
    }
    public Integer getPerLimit(){
        return this.perLimit;
    }
    public void setLocale(String locale){
        this.locale = locale;
    }
    public String getLocale(){
        return this.locale;
    }
    public void setLabels(String labels){
        this.labels = labels;
    }
    public String getLabels(){
        return this.labels;
    }
    public void setActivityStatus(String activityStatus){
        this.activityStatus = activityStatus;
    }
    public String getActivityStatus(){
        return this.activityStatus;
    }
    public void setTopicType(String topicType){
        this.topicType = topicType;
    }
    public String getTopicType(){
        return this.topicType;
    }
    public void setAttachs(String attachs){
        this.attachs = attachs;
    }
    public String getAttachs(){
        return this.attachs;
    }
    public void setAttachsName(String attachsName){
        this.attachsName = attachsName;
    }
    public String getAttachsName(){
        return this.attachsName;
    }
    public void setDataSrc(String dataSrc){
        this.dataSrc = dataSrc;
    }
    public String getDataSrc(){
        return this.dataSrc;
    }
    public void setDataSrcname(String dataSrcname){
        this.dataSrcname = dataSrcname;
    }
    public String getDataSrcname(){
        return this.dataSrcname;
    }
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    public String getOrgId(){
        return this.orgId;
    }
    public void setCreateName(String createName){
        this.createName = createName;
    }
    public String getCreateName(){
        return this.createName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}