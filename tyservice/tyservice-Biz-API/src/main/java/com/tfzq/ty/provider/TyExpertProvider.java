package com.tfzq.ty.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.ty.TyExpertBean;
/**
 *   @author pengtao 
 */

public interface TyExpertProvider extends BaseProvider<TyExpert> {
	
	public PageInfo<TyExpertBean> queryBeans(Map<String, Object> params);
	
	public List<TyExpertBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
	public List<TyExpertBean> queryIndustryNum(Map<String, Object> params);
	public List<TyExpertBean> queryCloseLevelNum(Map<String, Object> params);

	public List<TyExpert> queryByActId(String actId);
}
