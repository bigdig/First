package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoVoteItem extends BaseModel{
    private String voteId;
    private String voteItemContent;
    private Integer sortNo;
    
    public void setVoteId(String voteId){
        this.voteId = voteId;
    }
    public String getVoteId(){
        return this.voteId;
    }
    public void setVoteItemContent(String voteItemContent){
        this.voteItemContent = voteItemContent;
    }
    public String getVoteItemContent(){
        return this.voteItemContent;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }
    public Integer getSortNo(){
        return this.sortNo;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}