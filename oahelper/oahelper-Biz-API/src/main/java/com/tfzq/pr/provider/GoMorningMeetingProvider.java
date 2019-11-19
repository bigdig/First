package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoMorningMeeting;
import com.tfzq.pr.model.pr.GoMorningMeetingBean;
/**
 *   @author yuzhitao 
 */

public interface GoMorningMeetingProvider extends BaseProvider<GoMorningMeeting> {
	
	public PageInfo<GoMorningMeetingBean> queryBeans(Map<String, Object> params);
	
	public List<GoMorningMeetingBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
