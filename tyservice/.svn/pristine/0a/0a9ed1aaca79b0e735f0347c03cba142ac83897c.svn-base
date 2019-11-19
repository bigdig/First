package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyAppsecretMapper;
import com.tfzq.ty.model.generator.TyAppsecret;
import com.tfzq.ty.model.ty.TyAppsecretBean;
import com.tfzq.ty.provider.TyAppsecretProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyAppsecret")
//@DubboService(interfaceClass = TyAppsecretProvider.class)
@Service
public class TyAppsecretProviderImpl extends BaseProviderImpl<TyAppsecret> implements TyAppsecretProvider {
	//@Autowired
	//private TyAppsecretExpandMapper tyAppsecretExpandMapper;

	public PageInfo<TyAppsecret> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyAppsecretBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyAppsecretBean> pageInfo = getPage(userIds, TyAppsecretBean.class);
		return pageInfo;
	}

	@Override
	public TyAppsecret queryByPlatSrc(String p) {
		return ((TyAppsecretMapper)getMapper()).queryByPlatSrc(p);
	}

}
