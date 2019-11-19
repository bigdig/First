package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProjecttrack;
import com.tfzq.ty.model.ty.TyProjecttrackBean;
/**
 *   @author pengtao 
 */

public interface TyProjecttrackProvider extends BaseProvider<TyProjecttrack> {
	
	public PageInfo<TyProjecttrackBean> queryBeans(Map<String, Object> params);
	
	public List<TyProjecttrackBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
