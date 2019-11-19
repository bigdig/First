package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoVoteGroup;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoVoteGroupBean;

/**
 *   @author yuzhitao 
 */
public interface GoVoteGroupMapper extends BaseMapper<GoVoteGroup> {
	public List<GoVoteGroupBean> selectByCondition(Map<String, Object> params) ;

}