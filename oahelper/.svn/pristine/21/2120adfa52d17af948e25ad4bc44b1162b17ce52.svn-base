package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoInform extends BaseModel{
    private String informTitle;
    private String informContent;
    private String externalLink;
    private String srcType;
    private String srcId;
    private String isReply;
    private String informGroup;
    
    public void setInformTitle(String informTitle){
        this.informTitle = informTitle;
    }
    public String getInformTitle(){
        return this.informTitle;
    }
    public void setInformContent(String informContent){
        this.informContent = informContent;
    }
    public String getInformContent(){
        return this.informContent;
    }
    public void setExternalLink(String externalLink){
        this.externalLink = externalLink;
    }
    public String getExternalLink(){
        return this.externalLink;
    }
    public void setSrcType(String srcType){
        this.srcType = srcType;
    }
    public String getSrcType(){
        return this.srcType;
    }
    public void setSrcId(String srcId){
        this.srcId = srcId;
    }
    public String getSrcId(){
        return this.srcId;
    }
    public void setIsReply(String isReply){
        this.isReply = isReply;
    }
    public String getIsReply(){
        return this.isReply;
    }
    public void setInformGroup(String informGroup){
        this.informGroup = informGroup;
    }
    public String getInformGroup(){
        return this.informGroup;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}