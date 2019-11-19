package com.tfzq.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.DictItem;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyOrgcustomerMapper;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.provider.TyOrgcustomerProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyOrgcustomer")
//@DubboService(interfaceClass = TyOrgcustomerProvider.class)
@Service
public class TyOrgcustomerProviderImpl extends BaseProviderImpl<TyOrgcustomer> implements TyOrgcustomerProvider {
	//@Autowired
	//private TyOrgcustomerExpandMapper tyOrgcustomerExpandMapper;

	public PageInfo<TyOrgcustomer> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyOrgcustomerBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyOrgcustomerBean> pageInfo = getPage(userIds, TyOrgcustomerBean.class);
		return pageInfo;
	}

	@Override
	public List<TyOrgcustomerBean> queryByCondition(Map<String, Object> params) {
		return ((TyOrgcustomerMapper)getMapper()).queryByCondition(params);
	}

	@Override
	public void updateByOrgId(Map<String, Object> pa) {
		((TyOrgcustomerMapper)getMapper()).updateByOrgId(pa);
	}

	@Override
	public List<DictItem> queryCustdict(Map<String, Object> params) {
		return ((TyOrgcustomerMapper)getMapper()).queryCustdict(params);
	}

	@Override
	public PageInfo<TyOrgcustomerBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc,id ");
		this.startPage(params);
		Page<TyOrgcustomerBean> results =  ((TyOrgcustomerMapper)getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();
	}
	
	@Override
	public List<TyOrgcustomerBean> queryCustActive(Map<String, Object> params) {
		return ((TyOrgcustomerMapper)getMapper()).queryCustActive(params);
	}

	@Override
	public int queryMyActiveCustCount(Map<String, Object> params) {
		return ((TyOrgcustomerMapper)getMapper()).queryMyActiveCustCount(params);
	}

	@Override
	public PageInfo<TyOrgcustomerBean> queryCustomerWithMarksByProc(Map<String, Object> params) {
		((TyOrgcustomerMapper)getMapper()).queryCustomerWithMarksByProc(params);
		List<TyOrgcustomerBean> list = (List<TyOrgcustomerBean>) params.get("customerList");
		PageInfo<TyOrgcustomerBean> page = new PageInfo<>(list);
		int total = (int) params.get("total");
		int pageSize = Integer.parseInt(params.get("pageSize").toString());
		if(params.get("pageNum") == null) {
			params.put("pageNum", 1);
		}
		int pageNum = Integer.parseInt(params.get("pageNum").toString());
		int pages = total%pageSize >  0 ? (total/pageSize +1 ) : (total/pageSize); 
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setTotal(total);
		page.setPages(pages);
		return page;
	}

	@Override
	public void updateServiceSaler(String id) {
	  ((TyOrgcustomerMapper)getMapper()).updateServiceSaler(id);
	}

	@Override
	public List<TyOrgcustomerBean> queryByConditionFromProcResult(Map<String, Object> params) {
		return ((TyOrgcustomerMapper)getMapper()).queryByConditionFromProcResult(params);
	}

	@Override
	public void updateBatchMark(String oldLabelName, String newLabelName) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oldLabelName", oldLabelName);
		params.put("newLabelName", newLabelName);
		
		((TyOrgcustomerMapper)getMapper()).updateBatchMark(params);

	}

	@Override
	public void sycnMarkAuto(TyOrgcustomer record) {
		// TODO Auto-generated method stub
		//getMapper().updateByPrimaryKey(record);
		this.update(record);
	}

}
