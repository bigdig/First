package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyMessagelogMapper;
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.ty.TyMessagelogBean;
import com.tfzq.ty.provider.TyMessagelogProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyMessagelog")
//@DubboService(interfaceClass = TyMessagelogProvider.class)
@Service
public class TyMessagelogProviderImpl extends BaseProviderImpl<TyMessagelog> implements TyMessagelogProvider {
	//@Autowired
	//private TyMessagelogExpandMapper tyMessagelogExpandMapper;

	public PageInfo<TyMessagelog> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyMessagelogBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyMessagelogBean> pageInfo = getPage(userIds, TyMessagelogBean.class);
		return pageInfo;
	}

	@Override
	public int batchAdd(List<TyMessagelog> list) {
		return ((TyMessagelogMapper)getMapper()).batchAdd(list);
	}

	@Override
	public List<TyMessagelogBean> queryByCondition(Map<String, Object> params) {
		return ((TyMessagelogMapper)getMapper()).queryByCondition(params);
	}

	@Override
	public List<String> queryReceiverName(Map<String, Object> params) {
		return ((TyMessagelogMapper)getMapper()).queryReceiverName(params);
	}

}
