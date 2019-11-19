package com.tfzq.ty.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyMsgtemplate extends BaseModel{
    private String tmpTitle;
    private String tmpContent;
    private String sendShortmsg;
    private String sendMail;
    
    public void setTmpTitle(String tmpTitle){
        this.tmpTitle = tmpTitle;
    }
    public String getTmpTitle(){
        return this.tmpTitle;
    }
    public void setTmpContent(String tmpContent){
        this.tmpContent = tmpContent;
    }
    public String getTmpContent(){
        return this.tmpContent;
    }
    public void setSendShortmsg(String sendShortmsg){
        this.sendShortmsg = sendShortmsg;
    }
    public String getSendShortmsg(){
        return this.sendShortmsg;
    }
    public void setSendMail(String sendMail){
        this.sendMail = sendMail;
    }
    public String getSendMail(){
        return this.sendMail;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}