package com.tfzq.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMeeting;
import com.tfzq.ty.model.ty.TyMeetingBean;
import com.tfzq.ty.provider.TyMeetingProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyMeeting")
//@DubboService(interfaceClass = TyMeetingProvider.class)
@Service
public class TyMeetingProviderImpl extends BaseProviderImpl<TyMeeting> implements TyMeetingProvider {
	//@Autowired
	//private TyMeetingExpandMapper tyMeetingExpandMapper;

	public PageInfo<TyMeeting> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyMeetingBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyMeetingBean> pageInfo = getPage(userIds, TyMeetingBean.class);
		return pageInfo;
	}

}
