package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.ty.TyActivityBean;
/**
 *   @author pengtao 
 */

public interface TyActivityProvider extends BaseProvider<TyActivity> {
	
	public PageInfo<TyActivityBean> queryBeans(Map<String, Object> params);
	
	public List<TyActivityBean> queryByCondition(Map<String, Object> params);

	public int queryMyActCount(String staffId);
	
	//其他自定义的方法
}
