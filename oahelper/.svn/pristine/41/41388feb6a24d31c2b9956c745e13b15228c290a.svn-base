package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteUser;
import com.tfzq.pr.model.pr.GoVoteUserBean;
/**
 *   @author yuzhitao 
 */

public interface GoVoteUserProvider extends BaseProvider<GoVoteUser> {
	
	public PageInfo<GoVoteUserBean> queryBeans(Map<String, Object> params);
	
	public List<GoVoteUserBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
