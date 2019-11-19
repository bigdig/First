package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.ty.TyActivitystaffBean;
/**
 *   @author pengtao 
 */

public interface TyActivitystaffProvider extends BaseProvider<TyActivitystaff> {
	
	public PageInfo<TyActivitystaffBean> queryBeans(Map<String, Object> params);
	
	public List<TyActivitystaffBean> queryByCondition(Map<String, Object> params);

	public int deleteByActId(String actId);

	public List<TyActivitystaffBean> queryByActId(String actId);
	
	//其他自定义的方法
}
