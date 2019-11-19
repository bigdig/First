package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.core.support.DictItem;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
/**
 *   @author pengtao 
 */

public interface TyOrgcustomerProvider extends BaseProvider<TyOrgcustomer> {
	
	public PageInfo<TyOrgcustomerBean> queryBeans(Map<String, Object> params);

	public List<TyOrgcustomerBean> queryByCondition(Map<String, Object> params);

	public void updateByOrgId(Map<String, Object> pa);

	public List<DictItem> queryCustdict(Map<String, Object> params);

	public PageInfo<TyOrgcustomerBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);

	public int queryMyActiveCustCount(Map<String, Object> params);
	
	public List<TyOrgcustomerBean> queryCustActive(Map<String, Object> params);

	public PageInfo<TyOrgcustomerBean> queryCustomerWithMarksByProc(Map<String, Object> params);

	public void updateServiceSaler(String id);

	public List<TyOrgcustomerBean> queryByConditionFromProcResult(Map<String, Object> params);

	public void updateBatchMark(String oldLabelName, String newLabelName);

	public void sycnMarkAuto(TyOrgcustomer record);
	
	//其他自定义的方法
}
