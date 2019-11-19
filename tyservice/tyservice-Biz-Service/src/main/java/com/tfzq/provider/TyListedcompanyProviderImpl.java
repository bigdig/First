package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyListedcompanyMapper;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.ty.TyListedcompanyBean;
import com.tfzq.ty.provider.TyListedcompanyProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyListedcompany")
//@DubboService(interfaceClass = TyListedcompanyProvider.class)
@Service
public class TyListedcompanyProviderImpl extends BaseProviderImpl<TyListedcompany> implements TyListedcompanyProvider {
	//@Autowired
	//private TyListedcompanyExpandMapper tyListedcompanyExpandMapper;

	public PageInfo<TyListedcompany> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyListedcompanyBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyListedcompanyBean> pageInfo = getPage(userIds, TyListedcompanyBean.class);
		return pageInfo;
	}

	@Override
	public PageInfo<TyListedcompanyBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc,id");
		this.startPage(params);
		Page<TyListedcompanyBean> results = ((TyListedcompanyMapper)getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();
	}

	@Override
	public List<TyListedcompany> queryByActId(String actId) {
		return ((TyListedcompanyMapper)getMapper()).queryByActId(actId);
	}

}
