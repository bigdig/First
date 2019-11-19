package org.ibase4j.service.sys;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.generator.SysRoleMenu;
import org.ibase4j.model.generator.SysUserRole;
import org.ibase4j.model.sys.SysRoleBean;
import org.ibase4j.model.sys.SysRoleMenuBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.provider.SysRoleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月31日 上午11:09:29
 */
@Service
public class SysRoleService extends BaseService<SysRoleProvider, SysRole> {
	@Autowired
    public void setProvider(SysRoleProvider provider) {
        this.provider = provider;
    }

    public PageInfo<SysRoleBean> queryBean(Map<String, Object> params) {
        return provider.queryBean(params);
    }

    public List<String> getPermissions(String id) {
        return provider.getPermissions(id);
    }
    
    public List<SysRole> getAllList(){
    	return provider.getAllList();
    }

	public void deleteMenuByRoleId(String userId) {
		provider.deleteMenuByRoleId(userId);
	}

	public void insertRoleMenu(SysRoleMenu roleMenu) {
        String uid = WebUtil.getCurrentUser();
        roleMenu.setCreateBy(uid == null ? "" : uid);
        roleMenu.setUpdateBy(uid == null ? "" : uid);
		provider.insertRoleMenu(roleMenu);
		
	}

	public List<SysRoleMenuBean> queryMenuByRoleId(String id) {
		return provider.queryMenuByRoleId(id);
	}

    public void insertUserRole(SysUserRole userRole) {
        String uid = WebUtil.getCurrentUser();
    	userRole.setCreateBy(uid == null ? "" : uid);
    	userRole.setUpdateBy(uid == null ? "" : uid);
		provider.insertUserRole(userRole);
	}

	
	public void deleteRoleByUserId(String userId) {
		provider.deleteRoleByUserId(userId);
	}
	
	public void clearUserRoleCache(String userId) {
		provider.clearUserRoleCache(userId);
	}

	public List<SysUserRoleBean> queryRoleByUserId(String userId){
		return provider.queryByUserId(userId);
	}

}
