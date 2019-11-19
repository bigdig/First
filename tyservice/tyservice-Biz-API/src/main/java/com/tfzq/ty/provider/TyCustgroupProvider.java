package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustgroup;
import com.tfzq.ty.model.ty.TyCustgroupBean;
/**
 *   @author pengtao 
 */

public interface TyCustgroupProvider extends BaseProvider<TyCustgroup> {
	
	public PageInfo<TyCustgroupBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
