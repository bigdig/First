package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoNotice extends BaseModel{
    private String noticeTitle;
    private String noticeType;
    private String noticeUrl;
    public String getNoticeUrl() {
		return noticeUrl;
	}
	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	private String isInform;
    private String informId;
    private String informGroup;
    private String noticeContent;
    
    public void setNoticeTitle(String noticeTitle){
        this.noticeTitle = noticeTitle;
    }
    public String getNoticeTitle(){
        return this.noticeTitle;
    }
    public void setNoticeType(String noticeType){
        this.noticeType = noticeType;
    }
    public String getNoticeType(){
        return this.noticeType;
    }
    public void setIsInform(String isInform){
        this.isInform = isInform;
    }
    public String getIsInform(){
        return this.isInform;
    }
    public void setInformId(String informId){
        this.informId = informId;
    }
    public String getInformId(){
        return this.informId;
    }
    public void setInformGroup(String informGroup){
        this.informGroup = informGroup;
    }
    public String getInformGroup(){
        return this.informGroup;
    }
    public void setNoticeContent(String noticeContent){
        this.noticeContent = noticeContent;
    }
    public String getNoticeContent(){
        return this.noticeContent;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}