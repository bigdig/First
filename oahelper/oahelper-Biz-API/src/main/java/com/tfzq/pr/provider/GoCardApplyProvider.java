package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoCardApply;
import com.tfzq.pr.model.pr.GoCardApplyBean;
/**
 *   @author yuzhitao 
 */

public interface GoCardApplyProvider extends BaseProvider<GoCardApply> {
	
	public PageInfo<GoCardApplyBean> queryBeans(Map<String, Object> params);
	
	public List<GoCardApplyBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
