package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyLabelMapper;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.provider.TyLabelProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyLabel")
//@DubboService(interfaceClass = TyLabelProvider.class)
@Service
public class TyLabelProviderImpl extends BaseProviderImpl<TyLabel> implements TyLabelProvider {
	//@Autowired
	//private TyLabelExpandMapper tyLabelExpandMapper;

	public PageInfo<TyLabel> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyLabelBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyLabelBean> pageInfo = getPage(userIds, TyLabelBean.class);
		return pageInfo;
	}

	@Override
	public List<TyLabelBean> getLabelCat() {
		return ((TyLabelMapper)getMapper()).getLabelCat();
	}

	@Override
	public List<TyLabel> getAllSubRecords() {
		return ((TyLabelMapper)getMapper()).getAllSubRecords();
	}

	public List<TyLabel> selectByLabelName(String LabelName){
		return ((TyLabelMapper)getMapper()).selectByLabelName(LabelName);

	}

	@Override
	public List<TyLabelBean> getCatListByCustomerId(String custId) {
		return ((TyLabelMapper)getMapper()).getCatListByCustomerId(custId);
	}

	@Override
	public List<TyLabelBean> getCustSubList(Map<String, Object> pa) {
		return ((TyLabelMapper)getMapper()).getCustSubList(pa);
	}

	@Override
	public List<TyLabel> getSubRecordsByCondition(Map<String, Object> params) {
		return ((TyLabelMapper)getMapper()).getSubRecordsByCondition(params);
	}

	@Override
	public List<TyLabel> getRecordsByCondition(Map<String, Object> params) {
		return ((TyLabelMapper)getMapper()).getRecordsByCondition(params);
	}

	@Override
	public PageInfo<TyLabelBean> querySubLabel(Map<String, Object> params) {
		//this.startPage(params);
		Page<TyLabelBean> results =((TyLabelMapper) getMapper()).querySubLabel(params);
		return results.toPageInfo();
	}

	@Override
	public List<TyLabelBean> getCatListByOrgId(String orgId) {
		return ((TyLabelMapper)getMapper()).getCatListByOrgId(orgId);
	}

	@Override
	public List<TyLabelBean> getOrgSubList(Map<String, Object> pa) {
		return ((TyLabelMapper)getMapper()).getOrgSubList(pa);
	}

	@Override
	public List<TyLabelBean> getcurrentSubLabels(String id) {
		return ((TyLabelMapper)getMapper()).getcurrentSubLabels(id);
	}

	@Override
	public List<TyLabelBean> getBindLabels(String labelId) {
		return ((TyLabelMapper)getMapper()).getBindLabels(labelId);
	}

	@Override
	public List<TyLabelBean> getBindRelatedLabels(String labelId) {
		return ((TyLabelMapper)getMapper()).getBindRelatedLabels(labelId);
	}

	@Override
	public void updateCustomerlabel(TyLabel oldRecord) {
		((TyLabelMapper)getMapper()).updateCustomerlabel(oldRecord);
	}

}
