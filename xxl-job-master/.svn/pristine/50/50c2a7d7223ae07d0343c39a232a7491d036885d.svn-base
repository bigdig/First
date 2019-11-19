package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.github.pagehelper.Page;
import com.tfzq.tycrm.entity.TyLabel;

/**
 *   @author pengtao 
 */
public interface TyLabelMapper extends BaseMapper<TyLabel> {

	List<TyLabel> getLabelCat();

	List<TyLabel> getAllSubRecords();

	List<TyLabel> selectByLabelName(String LabelName);
	
	List<TyLabel> getCatListByCustomerId(String custId);

	List<TyLabel> getCustSubList(Map<String, Object> pa);

	List<TyLabel> getSubRecordsByCondition(Map<String, Object> params);

	List<TyLabel> getRecordsByCondition(Map<String, Object> params);

	Page<TyLabel> querySubLabel(Map<String, Object> params);

}