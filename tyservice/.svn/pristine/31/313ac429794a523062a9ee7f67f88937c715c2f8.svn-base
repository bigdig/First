package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcrecostock;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
/**
 *   @author pengtao 
 */

public interface TyDcrecostockProvider extends BaseProvider<TyDcrecostock> {
	
	public PageInfo<TyDcrecostockBean> queryBeans(Map<String, Object> params);

	public List<TyDcrecostockBean> queryBySpeakId(String speakId);
	
	public List<TyDcrecostockBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
