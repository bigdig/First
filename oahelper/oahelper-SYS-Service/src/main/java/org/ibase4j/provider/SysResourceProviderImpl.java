package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.model.generator.SysResource;
import org.ibase4j.model.sys.SysResourceBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "sysResource")
@DubboService(interfaceClass = SysResourceProvider.class)
@Service
public class SysResourceProviderImpl extends BaseProviderImpl<SysResource> implements SysResourceProvider {
	//@Autowired
	//private SysResourceExpandMapper sysResourceExpandMapper;

	public PageInfo<SysResource> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<SysResourceBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysResourceBean> pageInfo = getPage(userIds, SysResourceBean.class);
		return pageInfo;
	}

}
