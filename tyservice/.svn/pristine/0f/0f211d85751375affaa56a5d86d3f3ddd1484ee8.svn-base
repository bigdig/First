package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.ty.TyListedcompanyBean;
/**
 *   @author pengtao 
 */

public interface TyListedcompanyProvider extends BaseProvider<TyListedcompany> {
	
	public PageInfo<TyListedcompanyBean> queryBeans(Map<String, Object> params);

	public PageInfo<TyListedcompanyBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);

	public List<TyListedcompany> queryByActId(String actId);
	
	//其他自定义的方法
}
