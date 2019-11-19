package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCfund;
import com.tfzq.ty.model.ty.TyCfundBean;
import com.tfzq.ty.provider.TyCfundProvider;
import com.tfzq.ty.dao.generator.TyCfundMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyCfund")
//@DubboService(interfaceClass = TyCfundProvider.class)
@Service
public class TyCfundProviderImpl extends BaseProviderImpl<TyCfund> implements TyCfundProvider {
	//@Autowired
	//private TyCfundExpandMapper tyCfundExpandMapper;

	public PageInfo<TyCfund> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyCfundBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyCfundBean> pageInfo = getPage(userIds, TyCfundBean.class);
		return pageInfo;
	}

	public List<TyCfundBean> queryByCondition(Map<String, Object> params) {
		List<TyCfundBean> results = ((TyCfundMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
