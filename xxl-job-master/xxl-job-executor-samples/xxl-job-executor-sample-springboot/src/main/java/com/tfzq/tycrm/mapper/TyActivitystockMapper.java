package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.TyActivitystock;

/**
 *   @author pengtao 
 */
public interface TyActivitystockMapper extends BaseMapper<TyActivitystock> {
	public List<TyActivitystock> selectByCondition(Map<String, Object> params) ;
	
	public int deleteByActId(String actId);

	public int deleteRecords(Map<String, Object> params);

}