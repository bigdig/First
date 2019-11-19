package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoNotice;
import com.tfzq.pr.model.pr.GoNoticeBean;
import com.tfzq.pr.provider.GoNoticeProvider;
import com.tfzq.pr.dao.generator.GoNoticeMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goNotice")
//@DubboService(interfaceClass = GoNoticeProvider.class)
@Service
public class GoNoticeProviderImpl extends BaseProviderImpl<GoNotice> implements GoNoticeProvider {
	//@Autowired
	//private GoNoticeExpandMapper goNoticeExpandMapper;

	public PageInfo<GoNotice> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoNoticeBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoNoticeBean> pageInfo = getPage(userIds, GoNoticeBean.class);
		return pageInfo;
	}

	public List<GoNoticeBean> queryByCondition(Map<String, Object> params) {
		List<GoNoticeBean> results = ((GoNoticeMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
