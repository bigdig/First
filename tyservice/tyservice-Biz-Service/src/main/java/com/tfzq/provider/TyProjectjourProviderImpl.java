package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProjectjour;
import com.tfzq.ty.model.ty.TyProjectjourBean;
import com.tfzq.ty.provider.TyProjectjourProvider;
import com.tfzq.ty.dao.generator.TyProjectjourMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyProjectjour")
//@DubboService(interfaceClass = TyProjectjourProvider.class)
@Service
public class TyProjectjourProviderImpl extends BaseProviderImpl<TyProjectjour> implements TyProjectjourProvider {
	//@Autowired
	//private TyProjectjourExpandMapper tyProjectExpandMapper;

	public PageInfo<TyProjectjour> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyProjectjourBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyProjectjourBean> pageInfo = getPage(userIds, TyProjectjourBean.class);
		return pageInfo;
	}

	public List<TyProjectjourBean> queryByCondition(Map<String, Object> params) {
		List<TyProjectjourBean> results = ((TyProjectjourMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
