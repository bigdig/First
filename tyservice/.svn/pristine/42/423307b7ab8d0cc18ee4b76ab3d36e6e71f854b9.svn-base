package org.ibase4j.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class SysRolebutton extends BaseModel{
    private String roleId;
    private String pageRoute;
    private String authType;
    
    public void setRoleId(String roleId){
        this.roleId = roleId;
    }
    public String getRoleId(){
        return this.roleId;
    }
    public void setPageRoute(String pageRoute){
        this.pageRoute = pageRoute;
    }
    public String getPageRoute(){
        return this.pageRoute;
    }
    public void setAuthType(String authType){
        this.authType = authType;
    }
    public String getAuthType(){
        return this.authType;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}