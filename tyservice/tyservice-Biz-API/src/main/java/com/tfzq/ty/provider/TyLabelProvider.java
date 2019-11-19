package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.ty.TyLabelBean;
/**
 *   @author pengtao 
 */

public interface TyLabelProvider extends BaseProvider<TyLabel> {
	
	public PageInfo<TyLabelBean> queryBeans(Map<String, Object> params);

	public List<TyLabelBean> getLabelCat();

	public List<TyLabel> getAllSubRecords();

	public List<TyLabel> selectByLabelName(String LabelName);

	public List<TyLabelBean> getCatListByCustomerId(String custId);

	public List<TyLabelBean> getCustSubList(Map<String, Object> pa);

	public List<TyLabel> getSubRecordsByCondition(Map<String, Object> params);

	public List<TyLabel> getRecordsByCondition(Map<String, Object> params);

	public PageInfo<TyLabelBean> querySubLabel(Map<String, Object> params);

	public List<TyLabelBean> getCatListByOrgId(String orgId);

	public List<TyLabelBean> getOrgSubList(Map<String, Object> pa);

	public List<TyLabelBean> getcurrentSubLabels(String id);

	public List<TyLabelBean> getBindLabels(String labelId);

	public List<TyLabelBean> getBindRelatedLabels(String labelId);

	public void updateCustomerlabel(TyLabel oldRecord);
	
	//其他自定义的方法
}
