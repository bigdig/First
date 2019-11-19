package com.tfzq.pr.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoFileArchive;
import com.tfzq.pr.model.pr.GoFileArchiveBean;
/**
 *   @author yuzhitao 
 */

public interface GoFileArchiveProvider extends BaseProvider<GoFileArchive> {
	
	public PageInfo<GoFileArchiveBean> queryBeans(Map<String, Object> params);
	
	public List<GoFileArchiveBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
