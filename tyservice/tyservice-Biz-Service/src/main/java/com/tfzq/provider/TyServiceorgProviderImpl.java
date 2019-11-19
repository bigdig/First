package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyServiceorgMapper;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.provider.TyServiceorgProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyServiceorg")
//@DubboService(interfaceClass = TyServiceorgProvider.class)
@Service
public class TyServiceorgProviderImpl extends BaseProviderImpl<TyServiceorg> implements TyServiceorgProvider {
	//@Autowired
	//private TyServiceorgExpandMapper tyServiceorgExpandMapper;

	public PageInfo<TyServiceorg> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyServiceorgBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyServiceorgBean> pageInfo = getPage(userIds, TyServiceorgBean.class);
		return pageInfo;
	}

	@Override
	public List<TyServiceorgBean> queryByCondition(Map<String, Object> params) {
		return ((TyServiceorgMapper)getMapper()).queryByCondition(params);
	}

	@Override
	public List<TyServiceorgBean> getRecordByOrgName(String orgName) {
		return ((TyServiceorgMapper)getMapper()).getRecordByOrgName(orgName);
	}

	@Override
	public PageInfo<TyServiceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc,id ");
		this.startPage(params);
		Page<TyServiceorgBean> results= ((TyServiceorgMapper)getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();
	}

}
