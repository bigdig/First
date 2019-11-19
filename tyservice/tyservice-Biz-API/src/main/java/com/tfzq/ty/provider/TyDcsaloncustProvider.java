package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcsaloncust;
import com.tfzq.ty.model.ty.TyDcsaloncustBean;
/**
 *   @author pengtao 
 */

public interface TyDcsaloncustProvider extends BaseProvider<TyDcsaloncust> {
	
	public PageInfo<TyDcsaloncustBean> queryBeans(Map<String, Object> params);

	public List<TyDcsaloncustBean> queryBySalonId(String salonId);

	public int deleteBySalonId(String salonId);
	
	//其他自定义的方法
}
