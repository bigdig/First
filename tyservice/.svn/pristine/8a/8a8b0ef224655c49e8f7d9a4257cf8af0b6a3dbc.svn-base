package org.ibase4j.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.generator.SysRoleMenu;
import org.ibase4j.model.generator.SysUserRole;
import org.ibase4j.model.sys.SysRoleBean;
import org.ibase4j.model.sys.SysRoleMenuBean;
import org.ibase4j.model.sys.SysUserRoleBean;

import com.github.pagehelper.PageInfo;

public interface SysRoleProvider extends BaseProvider<SysRole> {
	public PageInfo<SysRoleBean> queryBean(Map<String, Object> params);

	/** 根据角色Id获取权限选项value */
	public List<String> getPermissions(String id);

	public List<SysRole> getAllList();

	/**
	 * 用户角色
	 * @param userRole
	 */
	public void insertUserRole(SysUserRole userRole);

	public void deleteRoleByUserId(String userId);

	public List<SysUserRoleBean> queryByUserId(String userId);

	/**
	 * 角色菜单
	 * @param userId
	 */
	public void deleteMenuByRoleId(String userId);

	public void insertRoleMenu(SysRoleMenu roleMenu);

	public List<SysRoleMenuBean> queryMenuByRoleId(String id);
	
	public void clearUserRoleCache(String userId);


}
