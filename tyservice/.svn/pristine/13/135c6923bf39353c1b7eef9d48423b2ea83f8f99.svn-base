package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
/**
 *   @author pengtao 
 */

public interface TyCustomerlabelProvider extends BaseProvider<TyCustomerlabel> {
	
	public PageInfo<TyCustomerlabelBean> queryBeans(Map<String, Object> params);

	public int deleteByCustId(String customerId);

	public int deleteByLabelId(String labelId);

	public List<TyCustomerlabelBean> queryByCustId(String customerId);

	public void transferLableId(Map<String, Object> params1);
	
	//其他自定义的方法
}
