package com.tfzq.pr.entity;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class InfContent extends BaseModel{
    private String title;
    private String summary;
    private String thumbnail;
    private String userIp;
    private String userAgent;
    private Integer orderNumber;
    private String status;
    private Integer collectCount;
    private Integer voteUp;
    private Integer voteDown;
    private Integer rate;
    private Integer rateCount;
    private String commentStatus;
    private Integer commentCount;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date commentTime;
    private Integer viewCount;
    private String weixinFlag;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date weixinTime;
    private String editType;
    private String hotFlag;
    private String metaKeywords;
    private String metaDescription;
    private String categoryId;
    private String editmodelId;
    private String subjectId;
    private String sourceFiles;
    private String remarks;
    private String userName;
    private String companyId;
    private String sourceContentid;
    private String attachment;
    private String attachName;
    private String rejectMsg;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date rejectTime;
    private String  viewPwd;
    
    
    public String getViewPwd() {
		return viewPwd;
	}
	public void setViewPwd(String viewPwd) {
		this.viewPwd = viewPwd;
	}
	//发布标志
    private String publishFlag;
    
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setThumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }
    public String getThumbnail(){
        return this.thumbnail;
    }
    public void setUserIp(String userIp){
        this.userIp = userIp;
    }
    public String getUserIp(){
        return this.userIp;
    }
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }
    public String getUserAgent(){
        return this.userAgent;
    }
    public void setOrderNumber(Integer orderNumber){
        this.orderNumber = orderNumber;
    }
    public Integer getOrderNumber(){
        return this.orderNumber;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setVoteUp(Integer voteUp){
        this.voteUp = voteUp;
    }
    public Integer getVoteUp(){
        return this.voteUp;
    }
    public void setVoteDown(Integer voteDown){
        this.voteDown = voteDown;
    }
    public Integer getVoteDown(){
        return this.voteDown;
    }
    public void setRate(Integer rate){
        this.rate = rate;
    }
    public Integer getRate(){
        return this.rate;
    }
    public void setRateCount(Integer rateCount){
        this.rateCount = rateCount;
    }
    public Integer getRateCount(){
        return this.rateCount;
    }
    public void setCommentStatus(String commentStatus){
        this.commentStatus = commentStatus;
    }
    public String getCommentStatus(){
        return this.commentStatus;
    }
    public void setCommentCount(Integer commentCount){
        this.commentCount = commentCount;
    }
    public Integer getCommentCount(){
        return this.commentCount;
    }
    public void setCommentTime(Date commentTime){
        this.commentTime = commentTime;
    }
    public Date getCommentTime(){
        return this.commentTime;
    }
    public void setViewCount(Integer viewCount){
        this.viewCount = viewCount;
    }
    public Integer getViewCount(){
        return this.viewCount;
    }
    public void setWeixinFlag(String weixinFlag){
        this.weixinFlag = weixinFlag;
    }
    public String getWeixinFlag(){
        return this.weixinFlag;
    }
    public void setWeixinTime(Date weixinTime){
        this.weixinTime = weixinTime;
    }
    public Date getWeixinTime(){
        return this.weixinTime;
    }
    public void setEditType(String editType){
        this.editType = editType;
    }
    public String getEditType(){
        return this.editType;
    }

    public String getHotFlag() {
		return hotFlag;
	}
	public void setHotFlag(String hotFlag) {
		this.hotFlag = hotFlag;
	}
	public void setMetaKeywords(String metaKeywords){
        this.metaKeywords = metaKeywords;
    }
    public String getMetaKeywords(){
        return this.metaKeywords;
    }
    public void setMetaDescription(String metaDescription){
        this.metaDescription = metaDescription;
    }
    public String getMetaDescription(){
        return this.metaDescription;
    }
    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
    }
    public String getCategoryId(){
        return this.categoryId;
    }
    public void setEditmodelId(String editmodelId){
        this.editmodelId = editmodelId;
    }
    public String getEditmodelId(){
        return this.editmodelId;
    }
    public void setSubjectId(String subjectId){
        this.subjectId = subjectId;
    }
    public String getSubjectId(){
        return this.subjectId;
    }
    public void setSourceFiles(String sourceFiles){
        this.sourceFiles = sourceFiles;
    }
    public String getSourceFiles(){
        return this.sourceFiles;
    }
    public void setRemarks(String remarks){
        this.remarks = remarks;
    }
    public String getRemarks(){
        return this.remarks;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSourceContentid() {
		return sourceContentid;
	}
	public void setSourceContentid(String sourceContentid) {
		this.sourceContentid = sourceContentid;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getRejectMsg() {
		return rejectMsg;
	}
	public void setRejectMsg(String rejectMsg) {
		this.rejectMsg = rejectMsg;
	}
	public Date getRejectTime() {
		return rejectTime;
	}
	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}
	public String getPublishFlag() {
		return publishFlag;
	}
	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}
	public Integer getCollectCount() {
		return collectCount;
	}
	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}
	
}