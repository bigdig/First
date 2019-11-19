package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustgroup;
import com.tfzq.ty.model.ty.TyCustgroupBean;
import com.tfzq.ty.provider.TyCustgroupProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyCustgroup")
//@DubboService(interfaceClass = TyCustgroupProvider.class)
@Service
public class TyCustgroupProviderImpl extends BaseProviderImpl<TyCustgroup> implements TyCustgroupProvider {
	//@Autowired
	//private TyCustgroupExpandMapper tyCustgroupExpandMapper;

	public PageInfo<TyCustgroup> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyCustgroupBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyCustgroupBean> pageInfo = getPage(userIds, TyCustgroupBean.class);
		return pageInfo;
	}

}
