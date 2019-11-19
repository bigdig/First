package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyCustgroupdetailMapper;
import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;
import com.tfzq.ty.provider.TyCustgroupdetailProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyCustgroupdetail")
//@DubboService(interfaceClass = TyCustgroupdetailProvider.class)
@Service
public class TyCustgroupdetailProviderImpl extends BaseProviderImpl<TyCustgroupdetail> implements TyCustgroupdetailProvider {
	//@Autowired
	//private TyCustgroupdetailExpandMapper tyCustgroupdetailExpandMapper;

	public PageInfo<TyCustgroupdetail> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyCustgroupdetailBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyCustgroupdetailBean> pageInfo = getPage(userIds, TyCustgroupdetailBean.class);
		return pageInfo;
	}

	@Override
	public List<String> getCustIdByGroupId(String groupId) {
		return ((TyCustgroupdetailMapper)getMapper()).getCustIdByGroupId(groupId);
	}

	@Override
	public void deleteByGroupId(String groupId) {
		((TyCustgroupdetailMapper)getMapper()).deleteByGroupId(groupId);
	}

	@Override
	public int batchAdd(List<TyCustgroupdetail> list) {
		return ((TyCustgroupdetailMapper)getMapper()).batchAdd(list);
		
	}

	@Override
	public List<TyCustgroupdetailBean> queryByCondition(Map<String, Object> params) {
		return ((TyCustgroupdetailMapper)getMapper()).queryByCondition(params);
	}

}
