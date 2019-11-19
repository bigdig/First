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
import com.tfzq.ty.dao.generator.TyActivitysignMapper;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.provider.TyActivitysignProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivitysign")
//@DubboService(interfaceClass = TyActivitysignProvider.class)
@Service
public class TyActivitysignProviderImpl extends BaseProviderImpl<TyActivitysign> implements TyActivitysignProvider {
	//@Autowired
	//private TyActivitysignExpandMapper tyActivitysignExpandMapper;

	public PageInfo<TyActivitysign> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivitysignBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivitysignBean> pageInfo = getPage(userIds, TyActivitysignBean.class);
		return pageInfo;
	}

	public List<TyActivitysignBean> queryByCondition(Map<String, Object> params) {
		List<TyActivitysignBean> results = ((TyActivitysignMapper)getMapper()).selectByCondition(params);
		return results;
	}
	@Override
	public int deleteByActId(String actId) {
		return ((TyActivitysignMapper)getMapper()).deleteByActId(actId);
	}
	@Override
	public List<TyActivitysignBean> queryByActId(String actId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", actId);
		params.put("orderBy", "create_time  , id  ");
		return ((TyActivitysignMapper)getMapper()).selectByCondition(params);
	}

}
