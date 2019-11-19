package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyExpert;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.ty.model.ty.TyExpertBean;

/**
 *   @author pengtao 
 */
public interface TyExpertMapper extends BaseMapper<TyExpert> {
	public List<TyExpertBean> selectByCondition(Map<String, Object> params) ;
	
	public List<TyExpertBean> queryIndustryNum(Map<String, Object> params) ;
	
	public List<TyExpertBean> queryCloseLevelNum(Map<String, Object> params) ;

	public List<TyExpert> queryByActId(String actId);

}