package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoVoteUser;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoVoteUserBean;

/**
 *   @author yuzhitao 
 */
public interface GoVoteUserMapper extends BaseMapper<GoVoteUser> {
	public List<GoVoteUserBean> selectByCondition(Map<String, Object> params) ;

}