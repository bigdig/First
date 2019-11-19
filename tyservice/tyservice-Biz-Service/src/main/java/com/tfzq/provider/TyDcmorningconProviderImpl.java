package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcmorningcon;
import com.tfzq.ty.model.ty.TyDcmorningconBean;
import com.tfzq.ty.provider.TyDcmorningconProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcmorningcon")
//@DubboService(interfaceClass = TyDcmorningconProvider.class)
@Service
public class TyDcmorningconProviderImpl extends BaseProviderImpl<TyDcmorningcon> implements TyDcmorningconProvider {
	//@Autowired
	//private TyDcmorningconExpandMapper tyDcmorningconExpandMapper;

	public PageInfo<TyDcmorningcon> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcmorningconBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcmorningconBean> pageInfo = getPage(userIds, TyDcmorningconBean.class);
		return pageInfo;
	}

}
