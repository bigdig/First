package org.ibase4j.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ibase4j.core.base.BaseExpandMapper;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.sys.SysRoleMenuBean;
import org.ibase4j.model.sys.SysUserRoleBean;

public interface SysRoleExpandMapper extends BaseExpandMapper {

    List<String> queryPermission(@Param("roleId") String id);

    List<String> getPermissions(@Param("roleId") String id);
    
    void deleteByUserId(@Param("userId") String id);
    
    List<SysUserRoleBean> queryRoleByUserId(@Param("userId") String id);
    
    List<SysRole> getAllList();
    
	public void deleteMenuByRoleId(String userId);
	
    List<SysRoleMenuBean> queryMenuByRoleId(@Param("roleId") String id);

}
