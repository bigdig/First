package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyLabelnet;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyLabelnetBean;
/**
 *   @author pengtao 
 */

public interface TyLabelnetProvider extends BaseProvider<TyLabelnet> {
	
	public PageInfo<TyLabelnetBean> queryBeans(Map<String, Object> params);
	
	public List<TyLabelnetBean> queryByCondition(Map<String, Object> params);

	public void deleteByLabelId(String id);

	//其他自定义的方法
}
