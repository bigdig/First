package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyOrgprepcustMapper;
import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.ty.TyOrgprepcustBean;
import com.tfzq.ty.provider.TyOrgprepcustProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyOrgprepcust")
//@DubboService(interfaceClass = TyOrgprepcustProvider.class)
@Service
public class TyOrgprepcustProviderImpl extends BaseProviderImpl<TyOrgprepcust> implements TyOrgprepcustProvider {
	//@Autowired
	//private TyOrgprepcustExpandMapper tyOrgprepcustExpandMapper;

	public PageInfo<TyOrgprepcust> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyOrgprepcustBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyOrgprepcustBean> pageInfo = getPage(userIds, TyOrgprepcustBean.class);
		return pageInfo;
	}

	@Override
	public List<TyOrgprepcustBean> queryByCondition(
			Map<String, Object> params) {
		return ((TyOrgprepcustMapper)getMapper()).queryByCondition(params);
	}

}
