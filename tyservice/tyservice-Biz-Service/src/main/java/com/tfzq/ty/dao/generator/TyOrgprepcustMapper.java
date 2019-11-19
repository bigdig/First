package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.ty.TyOrgprepcustBean;

/**
 *   @author pengtao 
 */
public interface TyOrgprepcustMapper extends BaseMapper<TyOrgprepcust> {

	List<TyOrgprepcustBean> queryByCondition(Map<String, Object> params);

}