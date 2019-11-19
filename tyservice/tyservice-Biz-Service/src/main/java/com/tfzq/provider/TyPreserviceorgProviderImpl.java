package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyPreserviceorgMapper;
import com.tfzq.ty.model.generator.TyPreserviceorg;
import com.tfzq.ty.model.ty.TyPreserviceorgBean;
import com.tfzq.ty.provider.TyPreserviceorgProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyPreServiceorg")
//@DubboService(interfaceClass = TyPreserviceorgProvider.class)
@Service
public class TyPreserviceorgProviderImpl extends BaseProviderImpl<TyPreserviceorg> implements TyPreserviceorgProvider {
	//@Autowired
	//private TyPreserviceorgExpandMapper tyServiceorgExpandMapper;

	public PageInfo<TyPreserviceorg> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyPreserviceorgBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyPreserviceorgBean> pageInfo = getPage(userIds, TyPreserviceorgBean.class);
		return pageInfo;
	}

	@Override
	public List<TyPreserviceorgBean> queryByCondition(Map<String, Object> params) {
		return ((TyPreserviceorgMapper)getMapper()).queryByCondition(params);
	}

	@Override
	public List<TyPreserviceorgBean> getRecordByOrgName(String orgName) {
		return ((TyPreserviceorgMapper)getMapper()).getRecordByOrgName(orgName);
	}

	@Override
	public PageInfo<TyPreserviceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc,id ");
		this.startPage(params);
		Page<TyPreserviceorgBean> results= ((TyPreserviceorgMapper)getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();
	}

}
