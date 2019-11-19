package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyCustomerlabelMapper;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.provider.TyCustomerlabelProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyCustomerlabel")
//@DubboService(interfaceClass = TyCustomerlabelProvider.class)
@Service
public class TyCustomerlabelProviderImpl extends BaseProviderImpl<TyCustomerlabel> implements TyCustomerlabelProvider {
	//@Autowired
	//private TyCustomerlabelExpandMapper tyCustomerlabelExpandMapper;

	public PageInfo<TyCustomerlabel> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyCustomerlabelBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyCustomerlabelBean> pageInfo = getPage(userIds, TyCustomerlabelBean.class);
		return pageInfo;
	}

	@Override
	public int deleteByCustId(String customerId) {
		return ((TyCustomerlabelMapper)getMapper()).deleteByCustId(customerId);
	}

	@Override
	public int deleteByLabelId(String labelId) {
		return ((TyCustomerlabelMapper)getMapper()).deleteByLabelId(labelId);
	}

	@Override
	public List<TyCustomerlabelBean> queryByCustId(String customerId) {
		return ((TyCustomerlabelMapper)getMapper()).queryByCustId(customerId);
	}

	@Override
	public void transferLableId(Map<String, Object> params1) {
		((TyCustomerlabelMapper)getMapper()).transferLableId(params1);
	}

}
