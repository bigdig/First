package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.model.generator.SysRolebutton;
import org.ibase4j.model.sys.SysRolebuttonBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "sysRolebutton")
@DubboService(interfaceClass = SysRolebuttonProvider.class)
@Service
public class SysRolebuttonProviderImpl extends BaseProviderImpl<SysRolebutton> implements SysRolebuttonProvider {
	//@Autowired
	//private SysRolebuttonExpandMapper sysRolebuttonExpandMapper;

	public PageInfo<SysRolebutton> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<SysRolebuttonBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysRolebuttonBean> pageInfo = getPage(userIds, SysRolebuttonBean.class);
		return pageInfo;
	}

}
