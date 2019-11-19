package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.provider.TyActivityProvider;
import com.tfzq.ty.dao.generator.TyActivityMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivity")
//@DubboService(interfaceClass = TyActivityProvider.class)
@Service
public class TyActivityProviderImpl extends BaseProviderImpl<TyActivity> implements TyActivityProvider {
	//@Autowired
	//private TyActivityExpandMapper tyActivityExpandMapper;

	public PageInfo<TyActivity> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivityBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivityBean> pageInfo = getPage(userIds, TyActivityBean.class);
		return pageInfo;
	}

	public List<TyActivityBean> queryByCondition(Map<String, Object> params) {
		List<TyActivityBean> results = ((TyActivityMapper)getMapper()).selectByCondition(params);
		return results;
	}

	@Override
	public int queryMyActCount(String staffId) {
		// TODO Auto-generated method stub
		return ((TyActivityMapper)getMapper()).queryMyActCount(staffId);
	}

}
