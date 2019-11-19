package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoRemind;
import com.tfzq.pr.model.pr.GoRemindBean;
/**
 *   @author yuzhitao 
 */

public interface GoRemindProvider extends BaseProvider<GoRemind> {
	
	public PageInfo<GoRemindBean> queryBeans(Map<String, Object> params);
	
	public List<GoRemindBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
