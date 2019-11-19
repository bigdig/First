package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDcactivityMapper;
import com.tfzq.ty.model.generator.TyDcactivity;
import com.tfzq.ty.model.ty.TyDcactivityBean;
import com.tfzq.ty.provider.TyDcactivityProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcactivity")
//@DubboService(interfaceClass = TyDcactivityProvider.class)
@Service
public class TyDcactivityProviderImpl extends BaseProviderImpl<TyDcactivity> implements TyDcactivityProvider {
	//@Autowired
	//private TyDcactivityExpandMapper tyDcactivityExpandMapper;

	public PageInfo<TyDcactivity> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcactivityBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcactivityBean> pageInfo = getPage(userIds, TyDcactivityBean.class);
		return pageInfo;
	}

	@Override
	public int deleteByActId(String actId) {
		return ((TyDcactivityMapper)getMapper()).deleteByActId(actId);
	}

}
