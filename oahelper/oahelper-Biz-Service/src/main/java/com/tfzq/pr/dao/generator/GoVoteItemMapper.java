package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoVoteItem;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoVoteItemBean;

/**
 *   @author yuzhitao 
 */
public interface GoVoteItemMapper extends BaseMapper<GoVoteItem> {
	public List<GoVoteItemBean> selectByCondition(Map<String, Object> params) ;

}