package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDcrecostockMapper;
import com.tfzq.ty.model.generator.TyDcrecostock;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
import com.tfzq.ty.provider.TyDcrecostockProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcrecostock")
//@DubboService(interfaceClass = TyDcrecostockProvider.class)
@Service
public class TyDcrecostockProviderImpl extends BaseProviderImpl<TyDcrecostock> implements TyDcrecostockProvider {
	//@Autowired
	//private TyDcrecostockExpandMapper tyDcrecostockExpandMapper;

	public PageInfo<TyDcrecostock> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcrecostockBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcrecostockBean> pageInfo = getPage(userIds, TyDcrecostockBean.class);
		return pageInfo;
	}

	@Override
	public List<TyDcrecostockBean> queryBySpeakId(String speakId) {
		return ((TyDcrecostockMapper)getMapper()).queryBySpeakId(speakId);
	}
	
	public List<TyDcrecostockBean> queryByCondition(Map<String, Object> params){
		return ((TyDcrecostockMapper)getMapper()).queryByCondition(params);
	}

}
