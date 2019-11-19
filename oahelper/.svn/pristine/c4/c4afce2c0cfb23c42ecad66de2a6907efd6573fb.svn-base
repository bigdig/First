package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserMorningMeeting;
import com.tfzq.pr.model.pr.GoUserMorningMeetingBean;
import com.tfzq.pr.provider.GoUserMorningMeetingProvider;

/**
 * @author yuzhitao
 */
@Service
public class GoUserMorningMeetingService extends BaseService<GoUserMorningMeetingProvider, GoUserMorningMeeting> {
	// @DubboReference
	@Autowired
	public void setProvider(GoUserMorningMeetingProvider provider) {
		this.provider = provider;
	}

	public PageInfo<GoUserMorningMeetingBean> queryBeans(Map<String, Object> params) {
		return provider.queryBeans(params);
	}

	public List<GoUserMorningMeetingBean> queryByCondition(Map<String, Object> params) {

		return provider.queryByCondition(params);
	}

}
