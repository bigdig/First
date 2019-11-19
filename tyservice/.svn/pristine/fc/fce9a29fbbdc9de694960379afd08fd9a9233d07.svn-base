package org.ibase4j.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class SysPositionrole extends BaseModel{
    private String positionId;
    private String positionName;
    private String roleId;
    private String roleName;
    
    public void setPositionId(String positionId){
        this.positionId = positionId;
    }
    public String getPositionId(){
        return this.positionId;
    }
    public void setPositionName(String positionName){
        this.positionName = positionName;
    }
    public String getPositionName(){
        return this.positionName;
    }
    public void setRoleId(String roleId){
        this.roleId = roleId;
    }
    public String getRoleId(){
        return this.roleId;
    }
    public void setRoleName(String roleName){
        this.roleName = roleName;
    }
    public String getRoleName(){
        return this.roleName;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}