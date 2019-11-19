package com.tfzq.ty.dao.generator;

import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

/**
 *   @author pengtao 
 */
public interface TyCustomerlabelMapper extends BaseMapper<TyCustomerlabel> {

	int deleteByCustId(String customerId);

	int deleteByLabelId(String labelId);

	List<TyCustomerlabelBean> queryByCustId(String customerId);

	void transferLableId(Map<String, Object> params1);


}