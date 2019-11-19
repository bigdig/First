package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.ty.TyActivitysignBean;
/**
 *   @author pengtao 
 */

public interface TyActivitysignProvider extends BaseProvider<TyActivitysign> {
	
	public PageInfo<TyActivitysignBean> queryBeans(Map<String, Object> params);
	
	public List<TyActivitysignBean> queryByCondition(Map<String, Object> params);

	public int deleteByActId(String actId);

	public List<TyActivitysignBean> queryByActId(String actId);
	
	//其他自定义的方法
}
