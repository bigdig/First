package org.ibase4j.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.dao.generator.SysRoleMenuMapper;
import org.ibase4j.dao.generator.SysUserRoleMapper;
import org.ibase4j.dao.sys.SysRoleExpandMapper;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.generator.SysRoleMenu;
import org.ibase4j.model.generator.SysUserRole;
import org.ibase4j.model.sys.SysRoleBean;
import org.ibase4j.model.sys.SysRoleMenuBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月31日 上午11:01:33
 */
@CacheConfig(cacheNames = "sysRole")
@DubboService(interfaceClass = SysRoleProvider.class)
@Service
public class SysRoleProviderImpl extends BaseProviderImpl<SysRole> implements SysRoleProvider {
    @Autowired
    private SysRoleExpandMapper sysRoleExpandMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

	
    public PageInfo<SysRole> query(Map<String, Object> params) {
        startPage(params);
        return getPage(sysRoleExpandMapper.query(params));
    }

    public PageInfo<SysRoleBean> queryBean(Map<String, Object> params) {
        startPage(params);
        PageInfo<SysRoleBean> pageInfo = getPage(sysRoleExpandMapper.query(params), SysRoleBean.class);
        // 权限信息
        for (SysRoleBean bean : pageInfo.getList()) {
            List<String> permissions = sysRoleExpandMapper.queryPermission(bean.getId());
            for (String permission : permissions) {
                if (StringUtils.isBlank(bean.getPermission())) {
                    bean.setPermission(permission);
                } else {
                    bean.setPermission(bean.getPermission() + ";" + permission);
                }
            }
        }
        return pageInfo;
    }

    /* (non-Javadoc)
     * @see org.ibase4j.provider.SysRoleProvider#getPermissions(java.lang.String) */
    @Override
    public List<String> getPermissions(String id) {
        return sysRoleExpandMapper.getPermissions(id);
    }
    
    public List<SysRole> getAllList(){
    	return getMapper().selectAll(); 
    }

	@Override
	public void insertRoleMenu(SysRoleMenu roleMenu) {
		roleMenu.setUpdateTime(new Date());
		roleMenu.setCreateTime(new Date());
		sysRoleMenuMapper.insert(roleMenu);

	}
	
	@Override
	public void deleteMenuByRoleId(String userId) {
		sysRoleExpandMapper.deleteMenuByRoleId(userId);
	}


	@Override
	public List<SysRoleMenuBean> queryMenuByRoleId(String id) {
		return sysRoleExpandMapper.queryMenuByRoleId(id);
	}
	@Override
	public void insertUserRole(SysUserRole userRole) {
		// TODO Auto-generated method stub
		userRole.setUpdateTime(new Date());
		userRole.setCreateTime(new Date());
		sysUserRoleMapper.insert(userRole);
	}

	@Override
	public void deleteRoleByUserId(String userId) {
		// TODO Auto-generated method stub
		sysRoleExpandMapper.deleteByUserId(userId);
		//清除该用户的
		RedisUtil.del(getCacheKey("getAuthorize", userId));
		RedisUtil.del(getCacheKey("sysPermission", userId));
	}
	
	@Override
	public void clearUserRoleCache(String userId) {
		//清除该用户的的权限相关缓存
		RedisUtil.del(getCacheKey("getAuthorize", userId));
		RedisUtil.del(getCacheKey("sysPermission", userId));
	}
	
	public List<SysUserRoleBean> queryByUserId(String userId){
		return sysRoleExpandMapper.queryRoleByUserId(userId);
	}

}
