package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcmorningcon;
import com.tfzq.ty.model.ty.TyDcmorningconBean;
/**
 *   @author pengtao 
 */

public interface TyDcmorningconProvider extends BaseProvider<TyDcmorningcon> {
	
	public PageInfo<TyDcmorningconBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
