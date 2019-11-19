package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyStafflistBean;
/**
 *   @author pengtao 
 */

public interface TyStafflistProvider extends BaseProvider<TyStafflist> {
	
	public PageInfo<TyStafflistBean> queryBeans(Map<String, Object> params);

	public List<TyStafflist> getSalerRecords();

	public List<TyStafflist> getRecordBySalerName(String saler);

	public List<TyStafflist> queryByActId(String actId);

	public List<TyStafflist> getResearcherRecords();

	public PageInfo<TyStafflistBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);
	
	public List<TyStafflistBean> queryByCondition(Map<String, Object> params);

	//其他自定义的方法
}
