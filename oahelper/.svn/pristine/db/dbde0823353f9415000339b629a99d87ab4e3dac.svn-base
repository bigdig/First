package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.pr.GoInformBean;
/**
 *   @author yuzhitao 
 */

public interface GoInformProvider extends BaseProvider<GoInform> {
	
	public PageInfo<GoInformBean> queryBeans(Map<String, Object> params);
	
	public List<GoInformBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
