package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.sys.SysAreaBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "sysArea")
@DubboService(interfaceClass = SysAreaProvider.class)
@Service
public class SysAreaProviderImpl extends BaseProviderImpl<SysArea> implements SysAreaProvider {
	//@Autowired
	//private SysAreaExpandMapper sysAreaExpandMapper;

	public PageInfo<SysArea> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<SysAreaBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysAreaBean> pageInfo = getPage(userIds, SysAreaBean.class);
		return pageInfo;
	}

}
