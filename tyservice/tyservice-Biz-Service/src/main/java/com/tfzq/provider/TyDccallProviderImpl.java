package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDccall;
import com.tfzq.ty.model.ty.TyDccallBean;
import com.tfzq.ty.provider.TyDccallProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDccall")
//@DubboService(interfaceClass = TyDccallProvider.class)
@Service
public class TyDccallProviderImpl extends BaseProviderImpl<TyDccall> implements TyDccallProvider {
	//@Autowired
	//private TyDccallExpandMapper tyDccallExpandMapper;

	public PageInfo<TyDccall> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDccallBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDccallBean> pageInfo = getPage(userIds, TyDccallBean.class);
		return pageInfo;
	}

}
