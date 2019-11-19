package com.tfzq.ty.dao.generator;

import java.util.List;

import com.tfzq.ty.model.generator.TyDctopiccust;
import com.tfzq.ty.model.ty.TyDctopiccustBean;

import org.ibase4j.core.base.BaseMapper;

/**
 *   @author pengtao 
 */
public interface TyDctopiccustMapper extends BaseMapper<TyDctopiccust> {

	List<TyDctopiccustBean> queryByTopicId(String topicId);

	int deleteByTopicId(String topicId);

}