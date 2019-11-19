package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseMapper;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.ty.TyListedcompanyBean;

/**
 *   @author pengtao 
 */
public interface TyListedcompanyMapper extends BaseMapper<TyListedcompany> {

	Page<TyListedcompanyBean> queryLatestModifiedRecords(Map<String, Object> params);

	List<TyListedcompany> queryByActId(String actId);

}