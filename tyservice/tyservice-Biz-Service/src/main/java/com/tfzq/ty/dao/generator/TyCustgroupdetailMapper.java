package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;

import org.ibase4j.core.base.BaseMapper;

/**
 *   @author pengtao 
 */
public interface TyCustgroupdetailMapper extends BaseMapper<TyCustgroupdetail> {

	List<String> getCustIdByGroupId(String groupId);

	void deleteByGroupId(String groupId);

	int batchAdd(List<TyCustgroupdetail> list);

	List<TyCustgroupdetailBean> queryByCondition(Map<String, Object> params);

}