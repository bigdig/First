package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoMorningMeeting;
import com.tfzq.pr.model.pr.GoMorningMeetingBean;
import com.tfzq.pr.provider.GoMorningMeetingProvider;

/**
 * @author yuzhitao
 */
@Service
public class GoMorningMeetingService extends BaseService<GoMorningMeetingProvider, GoMorningMeeting> {
	// @DubboReference
	@Autowired
	public void setProvider(GoMorningMeetingProvider provider) {
		this.provider = provider;
	}

	public PageInfo<GoMorningMeetingBean> queryBeans(Map<String, Object> params) {
		return provider.queryBeans(params);
	}

	public List<GoMorningMeetingBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

}
