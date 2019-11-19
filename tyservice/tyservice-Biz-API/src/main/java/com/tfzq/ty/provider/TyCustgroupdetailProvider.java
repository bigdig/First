package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;
/**
 *   @author pengtao 
 */

public interface TyCustgroupdetailProvider extends BaseProvider<TyCustgroupdetail> {
	
	public PageInfo<TyCustgroupdetailBean> queryBeans(Map<String, Object> params);

	public List<String> getCustIdByGroupId(String groupId);

	public void deleteByGroupId(String groupId);

	public int batchAdd(List<TyCustgroupdetail> list);

	public List<TyCustgroupdetailBean> queryByCondition(Map<String, Object> params);
	
	//其他自定义的方法
}
