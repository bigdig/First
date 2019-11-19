package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcmorningstock;
import com.tfzq.ty.model.ty.TyDcmorningstockBean;
/**
 *   @author pengtao 
 */

public interface TyDcmorningstockProvider extends BaseProvider<TyDcmorningstock> {
	
	public PageInfo<TyDcmorningstockBean> queryBeans(Map<String, Object> params);

	public List<String> getStockBySpeakId(String speakId);

	public int deleteRecords(Map<String, Object> params);
	
	//其他自定义的方法
}
