package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDcsaloncustMapper;
import com.tfzq.ty.model.generator.TyDcsaloncust;
import com.tfzq.ty.model.ty.TyDcsaloncustBean;
import com.tfzq.ty.provider.TyDcsaloncustProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcsaloncust")
//@DubboService(interfaceClass = TyDcsaloncustProvider.class)
@Service
public class TyDcsaloncustProviderImpl extends BaseProviderImpl<TyDcsaloncust> implements TyDcsaloncustProvider {
	//@Autowired
	//private TyDcsaloncustExpandMapper tyDcsaloncustExpandMapper;

	public PageInfo<TyDcsaloncust> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcsaloncustBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcsaloncustBean> pageInfo = getPage(userIds, TyDcsaloncustBean.class);
		return pageInfo;
	}

	@Override
	public List<TyDcsaloncustBean> queryBySalonId(String salonId) {
		return ((TyDcsaloncustMapper)getMapper()).queryBySalonId(salonId);
	}

	@Override
	public int deleteBySalonId(String salonId) {
		return ((TyDcsaloncustMapper)getMapper()).deleteBySalonId(salonId);
	}

}
