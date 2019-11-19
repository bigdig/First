package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoVote extends BaseModel{
    private String voteGroupId;
    private Integer sortNo;
    private String voteName;
    private String voteDescription;
    private String isCustomizable;
    private String isMulti;
    
    public void setVoteGroupId(String voteGroupId){
        this.voteGroupId = voteGroupId;
    }
    public String getVoteGroupId(){
        return this.voteGroupId;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }
    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setVoteName(String voteName){
        this.voteName = voteName;
    }
    public String getVoteName(){
        return this.voteName;
    }
    public void setVoteDescription(String voteDescription){
        this.voteDescription = voteDescription;
    }
    public String getVoteDescription(){
        return this.voteDescription;
    }
    public void setIsCustomizable(String isCustomizable){
        this.isCustomizable = isCustomizable;
    }
    public String getIsCustomizable(){
        return this.isCustomizable;
    }
    public void setIsMulti(String isMulti){
        this.isMulti = isMulti;
    }
    public String getIsMulti(){
        return this.isMulti;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}