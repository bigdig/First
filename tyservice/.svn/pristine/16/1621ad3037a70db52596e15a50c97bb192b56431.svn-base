package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.ty.TyServiceorgBean;

/**
 *   @author pengtao 
 */
public interface TyServiceorgMapper extends BaseMapper<TyServiceorg> {

	List<TyServiceorgBean> queryByCondition(Map<String, Object> params);

	List<TyServiceorgBean> getRecordByOrgName(String orgName);

	Page<TyServiceorgBean> queryLatestModifiedRecords(Map<String, Object> params);

}