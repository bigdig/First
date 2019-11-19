package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.ty.TyMessagelogBean;

import org.ibase4j.core.base.BaseMapper;

/**
 *   @author pengtao 
 */
public interface TyMessagelogMapper extends BaseMapper<TyMessagelog> {

	int batchAdd(List<TyMessagelog> mlog);

	List<TyMessagelogBean> queryByCondition(Map<String, Object> params);

	List<String> queryReceiverName(Map<String, Object> params);

}