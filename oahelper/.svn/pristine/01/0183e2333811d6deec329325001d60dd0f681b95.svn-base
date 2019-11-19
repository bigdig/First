package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVote;
import com.tfzq.pr.model.pr.GoVoteBean;
/**
 *   @author yuzhitao 
 */

public interface GoVoteProvider extends BaseProvider<GoVote> {
	
	public PageInfo<GoVoteBean> queryBeans(Map<String, Object> params);
	
	public List<GoVoteBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
