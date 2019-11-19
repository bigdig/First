package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyAppsecret;
import com.tfzq.ty.model.ty.TyAppsecretBean;
/**
 *   @author pengtao 
 */

public interface TyAppsecretProvider extends BaseProvider<TyAppsecret> {
	
	public PageInfo<TyAppsecretBean> queryBeans(Map<String, Object> params);

	public TyAppsecret queryByPlatSrc(String p);
	
	//其他自定义的方法
}
