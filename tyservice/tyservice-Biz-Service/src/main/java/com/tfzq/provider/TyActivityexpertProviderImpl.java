package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyActivityexpertMapper;
import com.tfzq.ty.dao.generator.TyActivitystaffMapper;
import com.tfzq.ty.model.generator.TyActivityexpert;
import com.tfzq.ty.model.ty.TyActivityexpertBean;
import com.tfzq.ty.provider.TyActivityexpertProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivityexpert")
//@DubboService(interfaceClass = TyActivityexpertProvider.class)
@Service
public class TyActivityexpertProviderImpl extends BaseProviderImpl<TyActivityexpert> implements TyActivityexpertProvider {
	//@Autowired
	//private TyActivityexpertExpandMapper tyActivityexpertExpandMapper;

	public PageInfo<TyActivityexpert> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivityexpertBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivityexpertBean> pageInfo = getPage(userIds, TyActivityexpertBean.class);
		return pageInfo;
	}

	@Override
	public int deleteByActId(String actId) {
		return ((TyActivityexpertMapper)getMapper()).deleteByActId(actId);
	}

}
