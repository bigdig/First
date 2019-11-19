package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDccallcust;
import com.tfzq.ty.model.ty.TyDccallcustBean;
/**
 *   @author pengtao 
 */

public interface TyDccallcustProvider extends BaseProvider<TyDccallcust> {
	
	public PageInfo<TyDccallcustBean> queryBeans(Map<String, Object> params);

	public List<TyDccallcustBean> queryByCallId(String callId);

	public int deleteByCallId(String callId);
	
	//其他自定义的方法
}
