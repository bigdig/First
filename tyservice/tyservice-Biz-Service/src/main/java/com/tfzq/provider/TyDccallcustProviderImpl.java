package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDccallcustMapper;
import com.tfzq.ty.model.generator.TyDccallcust;
import com.tfzq.ty.model.ty.TyDccallcustBean;
import com.tfzq.ty.provider.TyDccallcustProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDccallcust")
//@DubboService(interfaceClass = TyDccallcustProvider.class)
@Service
public class TyDccallcustProviderImpl extends BaseProviderImpl<TyDccallcust> implements TyDccallcustProvider {
	//@Autowired
	//private TyDccallcustExpandMapper tyDccallcustExpandMapper;

	public PageInfo<TyDccallcust> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDccallcustBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDccallcustBean> pageInfo = getPage(userIds, TyDccallcustBean.class);
		return pageInfo;
	}

	@Override
	public List<TyDccallcustBean> queryByCallId(String callId) {
		return ((TyDccallcustMapper)getMapper()).queryByCallId(callId);
	}

	@Override
	public int deleteByCallId(String callId) {
		return ((TyDccallcustMapper)getMapper()).deleteByCallId(callId);
	}

}
