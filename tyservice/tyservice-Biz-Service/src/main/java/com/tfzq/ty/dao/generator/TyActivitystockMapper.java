package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyActivitystock;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.ty.model.ty.TyActivitystockBean;

/**
 *   @author pengtao 
 */
public interface TyActivitystockMapper extends BaseMapper<TyActivitystock> {
	public List<TyActivitystockBean> selectByCondition(Map<String, Object> params) ;
	
	public int deleteByActId(String actId);

	public int deleteRecords(Map<String, Object> params);

}