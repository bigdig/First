package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoAudity;
import com.tfzq.pr.model.pr.GoAudityBean;
/**
 *   @author yuzhitao 
 */

public interface GoAudityProvider extends BaseProvider<GoAudity> {
	
	public PageInfo<GoAudityBean> queryBeans(Map<String, Object> params);
	
	public List<GoAudityBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
