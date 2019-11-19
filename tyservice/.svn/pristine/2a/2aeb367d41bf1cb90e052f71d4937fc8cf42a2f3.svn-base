package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyLabelnet;
import org.ibase4j.core.base.BaseMapper;

import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyLabelnetBean;

/**
 *   @author pengtao 
 */
public interface TyLabelnetMapper extends BaseMapper<TyLabelnet> {
	public List<TyLabelnetBean> selectByCondition(Map<String, Object> params) ;

	public List<TyLabelBean> getBindLabels(String labelId);

	public void deleteByLabelId(String labelId);

}