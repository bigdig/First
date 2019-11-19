package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyPreserviceorg;
import com.tfzq.ty.model.ty.TyPreserviceorgBean;

/**
 *   @author pengtao 
 */
public interface TyPreserviceorgMapper extends BaseMapper<TyPreserviceorg> {

	List<TyPreserviceorgBean> queryByCondition(Map<String, Object> params);

	List<TyPreserviceorgBean> getRecordByOrgName(String orgName);

	Page<TyPreserviceorgBean> queryLatestModifiedRecords(Map<String, Object> params);

}