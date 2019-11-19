package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyActivitystaff;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.ty.model.ty.TyActivitystaffBean;

/**
 *   @author pengtao 
 */
public interface TyActivitystaffMapper extends BaseMapper<TyActivitystaff> {
	public List<TyActivitystaffBean> selectByCondition(Map<String, Object> params) ;

	public int deleteByActId(String actId);

//	public List<TyActivitystaffBean> queryByActId(String actId);

}