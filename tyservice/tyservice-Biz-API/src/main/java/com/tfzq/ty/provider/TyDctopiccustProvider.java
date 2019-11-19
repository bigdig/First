package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDctopiccust;
import com.tfzq.ty.model.ty.TyDctopiccustBean;
/**
 *   @author pengtao 
 */

public interface TyDctopiccustProvider extends BaseProvider<TyDctopiccust> {
	
	public PageInfo<TyDctopiccustBean> queryBeans(Map<String, Object> params);

	public List<TyDctopiccustBean> queryByTopicId(String topicId);

	public int deleteByTopicId(String topicId);
	
	//其他自定义的方法
}
