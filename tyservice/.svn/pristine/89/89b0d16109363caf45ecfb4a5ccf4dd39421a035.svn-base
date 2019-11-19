package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.ty.TyMessageauditBean;
import com.tfzq.ty.provider.TyMessageauditProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyMessageaudit")
//@DubboService(interfaceClass = TyMessageauditProvider.class)
@Service
public class TyMessageauditProviderImpl extends BaseProviderImpl<TyMessageaudit> implements TyMessageauditProvider {
	//@Autowired
	//private TyMessageauditExpandMapper tyMessageauditExpandMapper;

	public PageInfo<TyMessageaudit> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyMessageauditBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyMessageauditBean> pageInfo = getPage(userIds, TyMessageauditBean.class);
		return pageInfo;
	}

}
