package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.ty.TyServiceorgBean;
/**
 *   @author pengtao 
 */

public interface TyServiceorgProvider extends BaseProvider<TyServiceorg> {
	
	public PageInfo<TyServiceorgBean> queryBeans(Map<String, Object> params);

	public List<TyServiceorgBean> queryByCondition(Map<String, Object> params);

	public List<TyServiceorgBean> getRecordByOrgName(String orgName);

	public PageInfo<TyServiceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);
	
	//其他自定义的方法
}
