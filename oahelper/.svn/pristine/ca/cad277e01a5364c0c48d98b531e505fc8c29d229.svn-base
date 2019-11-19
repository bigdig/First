package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoNotice;
import com.tfzq.pr.model.pr.GoNoticeBean;
/**
 *   @author yuzhitao 
 */

public interface GoNoticeProvider extends BaseProvider<GoNotice> {
	
	public PageInfo<GoNoticeBean> queryBeans(Map<String, Object> params);
	
	public List<GoNoticeBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
