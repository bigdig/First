package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.ty.TyMessagelogBean;
/**
 *   @author pengtao 
 */

public interface TyMessagelogProvider extends BaseProvider<TyMessagelog> {
	
	public PageInfo<TyMessagelogBean> queryBeans(Map<String, Object> params);

	public int batchAdd(List<TyMessagelog> list);

	public List<TyMessagelogBean> queryByCondition(Map<String, Object> params);

	public List<String> queryReceiverName(Map<String, Object> params);
	
	//其他自定义的方法
}
