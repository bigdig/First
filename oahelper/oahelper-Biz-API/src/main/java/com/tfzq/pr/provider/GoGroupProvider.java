package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.pr.GoGroupBean;
/**
 *   @author yuzhitao 
 */

public interface GoGroupProvider extends BaseProvider<GoGroup> {
	
	public PageInfo<GoGroupBean> queryBeans(Map<String, Object> params);
	
	public List<GoGroupBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
