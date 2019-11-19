package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.ty.TyLabelBean;

/**
 *   @author pengtao 
 */
public interface TyLabelMapper extends BaseMapper<TyLabel> {

	List<TyLabelBean> getLabelCat();

	List<TyLabel> getAllSubRecords();

	List<TyLabel> selectByLabelName(String LabelName);
	
	List<TyLabelBean> getCatListByCustomerId(String custId);

	List<TyLabelBean> getCustSubList(Map<String, Object> pa);

	List<TyLabel> getSubRecordsByCondition(Map<String, Object> params);

	List<TyLabel> getRecordsByCondition(Map<String, Object> params);

	Page<TyLabelBean> querySubLabel(Map<String, Object> params);

	List<TyLabelBean> getCatListByOrgId(String orgId);

	List<TyLabelBean> getOrgSubList(Map<String, Object> pa);

	List<TyLabelBean> getcurrentSubLabels(String id);

	List<TyLabelBean> getBindLabels(String labelId);

	List<TyLabelBean> getBindRelatedLabels(String labelId);

	void updateCustomerlabel(TyLabel oldRecord);

}