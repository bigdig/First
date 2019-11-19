package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyPreserviceorg;
import com.tfzq.ty.model.ty.TyPreserviceorgBean;
/**
 *   @author pengtao 
 */

public interface TyPreserviceorgProvider extends BaseProvider<TyPreserviceorg> {
	
	public PageInfo<TyPreserviceorgBean> queryBeans(Map<String, Object> params);

	public List<TyPreserviceorgBean> queryByCondition(Map<String, Object> params);

	public List<TyPreserviceorgBean> getRecordByOrgName(String orgName);

	public PageInfo<TyPreserviceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);
	
	//其他自定义的方法
}
