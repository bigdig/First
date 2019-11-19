package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.ty.TyActivitystockBean;
/**
 *   @author pengtao 
 */

public interface TyActivitystockProvider extends BaseProvider<TyActivitystock> {
	
	public PageInfo<TyActivitystockBean> queryBeans(Map<String, Object> params);
	
	public List<TyActivitystockBean> queryByCondition(Map<String, Object> params);
	
	public int deleteByActId(String actId);
	//其他自定义的方法
	public int deleteRecords(Map<String, Object> params);

}
