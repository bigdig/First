package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDctopic;
import com.tfzq.ty.model.ty.TyDctopicBean;
import com.tfzq.ty.provider.TyDctopicProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDctopic")
//@DubboService(interfaceClass = TyDctopicProvider.class)
@Service
public class TyDctopicProviderImpl extends BaseProviderImpl<TyDctopic> implements TyDctopicProvider {
	//@Autowired
	//private TyDctopicExpandMapper tyDctopicExpandMapper;

	public PageInfo<TyDctopic> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDctopicBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDctopicBean> pageInfo = getPage(userIds, TyDctopicBean.class);
		return pageInfo;
	}

}
