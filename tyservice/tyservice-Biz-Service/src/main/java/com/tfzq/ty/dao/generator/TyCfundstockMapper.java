package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.ty.model.generator.TyCfundstock;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.ty.model.ty.TyCfundstockBean;

/**
 *   @author pengtao 
 */
public interface TyCfundstockMapper extends BaseMapper<TyCfundstock> {
	public List<TyCfundstockBean> selectByCondition(Map<String, Object> params) ;

}