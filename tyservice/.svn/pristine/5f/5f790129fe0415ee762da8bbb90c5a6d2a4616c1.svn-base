package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcsalon;
import com.tfzq.ty.model.ty.TyDcsalonBean;
import com.tfzq.ty.provider.TyDcsalonProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcsalon")
//@DubboService(interfaceClass = TyDcsalonProvider.class)
@Service
public class TyDcsalonProviderImpl extends BaseProviderImpl<TyDcsalon> implements TyDcsalonProvider {
	//@Autowired
	//private TyDcsalonExpandMapper tyDcsalonExpandMapper;

	public PageInfo<TyDcsalon> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcsalonBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcsalonBean> pageInfo = getPage(userIds, TyDcsalonBean.class);
		return pageInfo;
	}

}
