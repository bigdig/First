package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoVote;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoVoteBean;

/**
 *   @author yuzhitao 
 */
public interface GoVoteMapper extends BaseMapper<GoVote> {
	public List<GoVoteBean> selectByCondition(Map<String, Object> params) ;

}