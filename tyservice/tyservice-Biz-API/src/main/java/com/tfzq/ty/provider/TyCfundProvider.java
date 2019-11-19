package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCfund;
import com.tfzq.ty.model.ty.TyCfundBean;
/**
 *   @author pengtao 
 */

public interface TyCfundProvider extends BaseProvider<TyCfund> {
	
	public PageInfo<TyCfundBean> queryBeans(Map<String, Object> params);
	
	public List<TyCfundBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
