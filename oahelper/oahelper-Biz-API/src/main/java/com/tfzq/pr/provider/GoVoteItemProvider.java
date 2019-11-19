package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteItem;
import com.tfzq.pr.model.pr.GoVoteItemBean;
/**
 *   @author yuzhitao 
 */

public interface GoVoteItemProvider extends BaseProvider<GoVoteItem> {
	
	public PageInfo<GoVoteItemBean> queryBeans(Map<String, Object> params);
	
	public List<GoVoteItemBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
