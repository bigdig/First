package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.model.generator.SysPositionrole;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "sysPositionrole")
@DubboService(interfaceClass = SysPositionroleProvider.class)
@Service
public class SysPositionroleProviderImpl extends BaseProviderImpl<SysPositionrole> implements SysPositionroleProvider {
	//@Autowired
	//private SysPositionroleExpandMapper sysPositionroleExpandMapper;

	public PageInfo<SysPositionrole> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<SysPositionroleBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysPositionroleBean> pageInfo = getPage(userIds, SysPositionroleBean.class);
		return pageInfo;
	}

}
