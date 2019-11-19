package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProjecttrack;
import com.tfzq.ty.model.ty.TyProjecttrackBean;
import com.tfzq.ty.provider.TyProjecttrackProvider;
import com.tfzq.ty.dao.generator.TyProjecttrackMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyProjecttrack")
//@DubboService(interfaceClass = TyProjecttrackProvider.class)
@Service
public class TyProjecttrackProviderImpl extends BaseProviderImpl<TyProjecttrack> implements TyProjecttrackProvider {
	//@Autowired
	//private TyProjecttrackExpandMapper tyProjectExpandMapper;

	public PageInfo<TyProjecttrack> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyProjecttrackBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyProjecttrackBean> pageInfo = getPage(userIds, TyProjecttrackBean.class);
		return pageInfo;
	}

	public List<TyProjecttrackBean> queryByCondition(Map<String, Object> params) {
		List<TyProjecttrackBean> results = ((TyProjecttrackMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
