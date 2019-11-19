package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCfundstock;
import com.tfzq.ty.model.ty.TyCfundstockBean;
import com.tfzq.ty.provider.TyCfundstockProvider;
import com.tfzq.ty.dao.generator.TyCfundstockMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyCfundstock")
//@DubboService(interfaceClass = TyCfundstockProvider.class)
@Service
public class TyCfundstockProviderImpl extends BaseProviderImpl<TyCfundstock> implements TyCfundstockProvider {
	//@Autowired
	//private TyCfundstockExpandMapper tyCfundstockExpandMapper;

	public PageInfo<TyCfundstock> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyCfundstockBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyCfundstockBean> pageInfo = getPage(userIds, TyCfundstockBean.class);
		return pageInfo;
	}

	public List<TyCfundstockBean> queryByCondition(Map<String, Object> params) {
		List<TyCfundstockBean> results = ((TyCfundstockMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
