package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserMorningMeeting;
import com.tfzq.pr.model.pr.GoUserMorningMeetingBean;
import com.tfzq.pr.provider.GoUserMorningMeetingProvider;
import com.tfzq.pr.dao.generator.GoUserMorningMeetingMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goUserMorningMeeting")
//@DubboService(interfaceClass = GoUserMorningMeetingProvider.class)
@Service
public class GoUserMorningMeetingProviderImpl extends BaseProviderImpl<GoUserMorningMeeting> implements GoUserMorningMeetingProvider {
	//@Autowired
	//private GoUserMorningMeetingExpandMapper goUserMorningMeetingExpandMapper;

	public PageInfo<GoUserMorningMeeting> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoUserMorningMeetingBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoUserMorningMeetingBean> pageInfo = getPage(userIds, GoUserMorningMeetingBean.class);
		return pageInfo;
	}

	public List<GoUserMorningMeetingBean> queryByCondition(Map<String, Object> params) {
		List<GoUserMorningMeetingBean> results = ((GoUserMorningMeetingMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
