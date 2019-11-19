package com.tfzq.pr.entity;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ddfc.base.BaseModel;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class PrRealPublicopinionnotice extends BaseModel{
    private String abstractstr;
    private String catIcon;
    private String catId;
    private String catName;
    private String companyCode;
    private String companyId;
    private String companyName;
    private String newsId;
    private String newsMedia;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date newsTime;
    private String title;
    private String transmit;
    
    public void setAbstractstr(String abstractstr){
        this.abstractstr = abstractstr;
    }
    public String getAbstractstr(){
        return this.abstractstr;
    }
    public void setCatIcon(String catIcon){
        this.catIcon = catIcon;
    }
    public String getCatIcon(){
        return this.catIcon;
    }
    public void setCatId(String catId){
        this.catId = catId;
    }
    public String getCatId(){
        return this.catId;
    }
    public void setCatName(String catName){
        this.catName = catName;
    }
    public String getCatName(){
        return this.catName;
    }
    public void setCompanyCode(String companyCode){
        this.companyCode = companyCode;
    }
    public String getCompanyCode(){
        return this.companyCode;
    }
    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public String getCompanyId(){
        return this.companyId;
    }
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public String getCompanyName(){
        return this.companyName;
    }
    public void setNewsId(String newsId){
        this.newsId = newsId;
    }
    public String getNewsId(){
        return this.newsId;
    }
    public void setNewsMedia(String newsMedia){
        this.newsMedia = newsMedia;
    }
    public String getNewsMedia(){
        return this.newsMedia;
    }
    public void setNewsTime(Date newsTime){
        this.newsTime = newsTime;
    }
    public Date getNewsTime(){
        return this.newsTime;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setTransmit(String transmit){
        this.transmit = transmit;
    }
    public String getTransmit(){
        return this.transmit;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
	@Override
	public String toString() {
		return "RealPublicopinion [abstractstr=" + abstractstr + ", catIcon=" + catIcon + ", catId=" + catId
				+ ", catName=" + catName + ", companyCode=" + companyCode + ", companyId=" + companyId
				+ ", companyName=" + companyName + ", newsId=" + newsId + ", newsMedia=" + newsMedia + ", newsTime="
				+ newsTime + ", title=" + title + ", transmit=" + transmit + "]";
	}
    
}