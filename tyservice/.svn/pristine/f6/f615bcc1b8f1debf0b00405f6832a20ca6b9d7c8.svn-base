package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProject;
import com.tfzq.ty.model.ty.TyProjectBean;
/**
 *   @author pengtao 
 */

public interface TyProjectProvider extends BaseProvider<TyProject> {
	
	public PageInfo<TyProjectBean> queryBeans(Map<String, Object> params);
	
	public List<TyProjectBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
