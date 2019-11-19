package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;
import org.ibase4j.core.support.DictItem;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;

/**
 *   @author pengtao 
 */
public interface TyOrgcustomerMapper extends BaseMapper<TyOrgcustomer> {

	List<TyOrgcustomerBean> queryByCondition(Map<String, Object> params);

	void updateByOrgId(Map<String, Object> pa);

	List<DictItem> queryCustdict(Map<String, Object> params);

	Page<TyOrgcustomerBean> queryLatestModifiedRecords(Map<String, Object> params);

	int queryMyActiveCustCount(Map<String, Object> params);
	
	List<TyOrgcustomerBean> queryCustActive(Map<String, Object> params);

	void queryCustomerWithMarksByProc(Map<String, Object> params);

	void updateServiceSaler(String id);

	List<TyOrgcustomerBean> queryByConditionFromProcResult(Map<String, Object> params);

	void updateBatchMark(Map<String, Object> params);
}