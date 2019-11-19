package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyStafflistMapper;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.ty.provider.TyStafflistProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyStafflist")
//@DubboService(interfaceClass = TyStafflistProvider.class)
@Service
public class TyStafflistProviderImpl extends BaseProviderImpl<TyStafflist> implements TyStafflistProvider {
	//@Autowired
	//private TyStafflistExpandMapper tyStafflistExpandMapper;

	public PageInfo<TyStafflist> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyStafflistBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyStafflistBean> pageInfo = getPage(userIds, TyStafflistBean.class);
		return pageInfo;
	}

	@Override
	public List<TyStafflist> getSalerRecords() {
		return ((TyStafflistMapper)getMapper()).getSalerRecords();
	}

	@Override
	public List<TyStafflist> getRecordBySalerName(String saler) {
		return ((TyStafflistMapper)getMapper()).getRecordBySalerName(saler);
	}

	@Override
	public List<TyStafflist> queryByActId(String actId) {
		return ((TyStafflistMapper)getMapper()).queryByActId(actId);
	}

	@Override
	public List<TyStafflist> getResearcherRecords() {
		return ((TyStafflistMapper)getMapper()).getResearcherRecords();
	}

	@Override
	public PageInfo<TyStafflistBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc,id ");
		this.startPage(params);
		Page<TyStafflistBean> results =  ((TyStafflistMapper)getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();

	}
	
	public List<TyStafflistBean> queryByCondition(Map<String, Object> params){
		return ((TyStafflistMapper)getMapper()).queryByCondition(params);
	}

}
