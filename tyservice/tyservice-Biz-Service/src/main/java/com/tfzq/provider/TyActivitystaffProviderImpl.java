package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyActivitystaffMapper;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.ty.TyActivitystaffBean;
import com.tfzq.ty.provider.TyActivitystaffProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivitystaff")
//@DubboService(interfaceClass = TyActivitystaffProvider.class)
@Service
public class TyActivitystaffProviderImpl extends BaseProviderImpl<TyActivitystaff> implements TyActivitystaffProvider {
	//@Autowired
	//private TyActivitystaffExpandMapper tyActivitystaffExpandMapper;

	public PageInfo<TyActivitystaff> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivitystaffBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivitystaffBean> pageInfo = getPage(userIds, TyActivitystaffBean.class);
		return pageInfo;
	}

	public List<TyActivitystaffBean> queryByCondition(Map<String, Object> params) {
		List<TyActivitystaffBean> results = ((TyActivitystaffMapper)getMapper()).selectByCondition(params);
		return results;
	}

	@Override
	public int deleteByActId(String actId) {
		return ((TyActivitystaffMapper)getMapper()).deleteByActId(actId);
	}

	@Override
	public List<TyActivitystaffBean> queryByActId(String actId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", actId);
		return ((TyActivitystaffMapper)getMapper()).selectByCondition(params);
	}

}
