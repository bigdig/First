package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivityexpert;
import com.tfzq.ty.model.ty.TyActivityexpertBean;
/**
 *   @author pengtao 
 */

public interface TyActivityexpertProvider extends BaseProvider<TyActivityexpert> {
	
	public PageInfo<TyActivityexpertBean> queryBeans(Map<String, Object> params);

	public int deleteByActId(String actId);
	
	//其他自定义的方法
}
