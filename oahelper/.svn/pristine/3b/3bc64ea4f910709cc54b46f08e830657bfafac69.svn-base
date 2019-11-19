package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoGroup extends BaseModel{
    private String groupName;
    private String groupType;
    private String departmentId;
    private String groupRemark;
    
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return this.groupName;
    }
    public void setGroupType(String groupType){
        this.groupType = groupType;
    }
    public String getGroupType(){
        return this.groupType;
    }
    public void setDepartmentId(String departmentId){
        this.departmentId = departmentId;
    }
    public String getDepartmentId(){
        return this.departmentId;
    }
    public void setGroupRemark(String groupRemark){
        this.groupRemark = groupRemark;
    }
    public String getGroupRemark(){
        return this.groupRemark;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}