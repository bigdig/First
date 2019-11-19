package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyStafflistBean;

/**
 *   @author pengtao 
 */
public interface TyStafflistMapper extends BaseMapper<TyStafflist> {

	List<TyStafflist> getSalerRecords();

	List<TyStafflist> getRecordBySalerName(String saler);

	List<TyStafflist> queryByActId(String actId);

	List<TyStafflist> getResearcherRecords();

	Page<TyStafflistBean> queryLatestModifiedRecords(Map<String, Object> params);

	List<TyStafflistBean> queryByCondition(Map<String, Object> params);
}