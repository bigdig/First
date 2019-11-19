package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoUserMorningMeeting;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoUserMorningMeetingBean;

/**
 *   @author yuzhitao 
 */
public interface GoUserMorningMeetingMapper extends BaseMapper<GoUserMorningMeeting> {
	public List<GoUserMorningMeetingBean> selectByCondition(Map<String, Object> params) ;

}