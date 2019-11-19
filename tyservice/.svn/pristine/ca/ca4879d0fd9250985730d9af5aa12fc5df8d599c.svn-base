package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyMessageaudit extends BaseModel{
    private String msgType;
    private String title;
    private String msgContent;
    private String auditFlag;
    private String auditBy;
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date auditTime;
    private String fileName;
    private String filePath;
    
    public void setMsgType(String msgType){
        this.msgType = msgType;
    }
    public String getMsgType(){
        return this.msgType;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setMsgContent(String msgContent){
        this.msgContent = msgContent;
    }
    public String getMsgContent(){
        return this.msgContent;
    }
    public void setAuditFlag(String auditFlag){
        this.auditFlag = auditFlag;
    }
    public String getAuditFlag(){
        return this.auditFlag;
    }
    public void setAuditBy(String auditBy){
        this.auditBy = auditBy;
    }
    public String getAuditBy(){
        return this.auditBy;
    }
    public void setAuditTime(Date auditTime){
        this.auditTime = auditTime;
    }
    public Date getAuditTime(){
        return this.auditTime;
    }
    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}