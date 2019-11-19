package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.tfzq.ty.model.generator.TyProjectjour;
import com.tfzq.ty.model.ty.TyProjectjourBean;

/**
 *   @author pengtao 
 */
public interface TyProjectjourMapper extends BaseMapper<TyProjectjour> {
	public List<TyProjectjourBean> selectByCondition(Map<String, Object> params) ;

}