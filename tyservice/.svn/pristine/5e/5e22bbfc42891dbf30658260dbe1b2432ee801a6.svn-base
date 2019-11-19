package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMsgtemplate;
import com.tfzq.ty.model.ty.TyMsgtemplateBean;
import com.tfzq.ty.provider.TyMsgtemplateProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyMsgtemplate")
//@DubboService(interfaceClass = TyMsgtemplateProvider.class)
@Service
public class TyMsgtemplateProviderImpl extends BaseProviderImpl<TyMsgtemplate> implements TyMsgtemplateProvider {
	//@Autowired
	//private TyMsgtemplateExpandMapper tyMsgtemplateExpandMapper;

	public PageInfo<TyMsgtemplate> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyMsgtemplateBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyMsgtemplateBean> pageInfo = getPage(userIds, TyMsgtemplateBean.class);
		return pageInfo;
	}

}
