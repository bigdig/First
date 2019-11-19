package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMsgtemplate;
import com.tfzq.ty.model.ty.TyMsgtemplateBean;
/**
 *   @author pengtao 
 */

public interface TyMsgtemplateProvider extends BaseProvider<TyMsgtemplate> {
	
	public PageInfo<TyMsgtemplateBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
