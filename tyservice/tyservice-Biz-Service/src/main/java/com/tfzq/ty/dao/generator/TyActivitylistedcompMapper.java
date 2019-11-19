package com.tfzq.ty.dao.generator;

import org.ibase4j.core.base.BaseMapper;

import com.tfzq.ty.model.generator.TyActivitylistedcomp;

/**
 *   @author pengtao 
 */
public interface TyActivitylistedcompMapper extends BaseMapper<TyActivitylistedcomp> {

	int deleteByActId(String id);

}