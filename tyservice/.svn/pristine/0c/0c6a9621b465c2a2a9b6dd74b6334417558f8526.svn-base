package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDctopiccustMapper;
import com.tfzq.ty.model.generator.TyDctopiccust;
import com.tfzq.ty.model.ty.TyDctopiccustBean;
import com.tfzq.ty.provider.TyDctopiccustProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDctopiccust")
//@DubboService(interfaceClass = TyDctopiccustProvider.class)
@Service
public class TyDctopiccustProviderImpl extends BaseProviderImpl<TyDctopiccust> implements TyDctopiccustProvider {
	//@Autowired
	//private TyDctopiccustExpandMapper tyDctopiccustExpandMapper;

	public PageInfo<TyDctopiccust> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDctopiccustBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDctopiccustBean> pageInfo = getPage(userIds, TyDctopiccustBean.class);
		return pageInfo;
	}

	@Override
	public List<TyDctopiccustBean> queryByTopicId(String topicId) {
		return ((TyDctopiccustMapper)getMapper()).queryByTopicId(topicId);
	}

	@Override
	public int deleteByTopicId(String topicId) {
		return ((TyDctopiccustMapper)getMapper()).deleteByTopicId(topicId);
	}

}
