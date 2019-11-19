package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoMorningMeeting;
import com.tfzq.pr.model.pr.GoMorningMeetingBean;
import com.tfzq.pr.provider.GoMorningMeetingProvider;
import com.tfzq.pr.dao.generator.GoMorningMeetingMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goMorningMeeting")
//@DubboService(interfaceClass = GoMorningMeetingProvider.class)
@Service
public class GoMorningMeetingProviderImpl extends BaseProviderImpl<GoMorningMeeting> implements GoMorningMeetingProvider {
	//@Autowired
	//private GoMorningMeetingExpandMapper goMorningMeetingExpandMapper;

	public PageInfo<GoMorningMeeting> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoMorningMeetingBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoMorningMeetingBean> pageInfo = getPage(userIds, GoMorningMeetingBean.class);
		return pageInfo;
	}

	public List<GoMorningMeetingBean> queryByCondition(Map<String, Object> params) {
		List<GoMorningMeetingBean> results = ((GoMorningMeetingMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
