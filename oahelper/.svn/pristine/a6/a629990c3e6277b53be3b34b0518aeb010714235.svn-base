package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.model.sys.SysPositionBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "sysPosition")
@DubboService(interfaceClass = SysPositionProvider.class)
@Service
public class SysPositionProviderImpl extends BaseProviderImpl<SysPosition> implements SysPositionProvider {
	//@Autowired
	//private SysPositionExpandMapper sysPositionExpandMapper;

	public PageInfo<SysPosition> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<SysPositionBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysPositionBean> pageInfo = getPage(userIds, SysPositionBean.class);
		return pageInfo;
	}

}
