package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoUserGroup;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoUserGroupBean;

/**
 *   @author yuzhitao 
 */
public interface GoUserGroupMapper extends BaseMapper<GoUserGroup> {
	public List<GoUserGroupBean> selectByCondition(Map<String, Object> params) ;

}