package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyActivitylistedcompMapper;
import com.tfzq.ty.model.generator.TyActivitylistedcomp;
import com.tfzq.ty.model.ty.TyActivitylistedcompBean;
import com.tfzq.ty.provider.TyActivitylistedcompProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivitylistedcomp")
//@DubboService(interfaceClass = TyActivitylistedcompProvider.class)
@Service
public class TyActivitylistedcompProviderImpl extends BaseProviderImpl<TyActivitylistedcomp> implements TyActivitylistedcompProvider {
	//@Autowired
	//private TyActivitylistedcompExpandMapper tyActivitylistedcompExpandMapper;

	public PageInfo<TyActivitylistedcomp> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivitylistedcompBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivitylistedcompBean> pageInfo = getPage(userIds, TyActivitylistedcompBean.class);
		return pageInfo;
	}

	@Override
	public int deleteByActId(String actId) {
		return ((TyActivitylistedcompMapper)getMapper()).deleteByActId(actId);
	}

}
