package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.ty.TyExpertBean;
import com.tfzq.ty.provider.TyExpertProvider;
import com.tfzq.ty.dao.generator.TyExpertMapper;
import com.tfzq.ty.dao.generator.TyStafflistMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyExpert")
//@DubboService(interfaceClass = TyExpertProvider.class)
@Service
public class TyExpertProviderImpl extends BaseProviderImpl<TyExpert> implements TyExpertProvider {
	//@Autowired
	//private TyExpertExpandMapper tyExpertExpandMapper;

	public PageInfo<TyExpert> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyExpertBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyExpertBean> pageInfo = getPage(userIds, TyExpertBean.class);
		return pageInfo;
	}

	public List<TyExpertBean> queryByCondition(Map<String, Object> params) {
		List<TyExpertBean> results = ((TyExpertMapper)getMapper()).selectByCondition(params);
		return results;
	}
	
	public List<TyExpertBean> queryIndustryNum(Map<String, Object> params) {
		List<TyExpertBean> results = ((TyExpertMapper)getMapper()).queryIndustryNum(params);
		return results;
	}
	
	public List<TyExpertBean> queryCloseLevelNum(Map<String, Object> params) {
		List<TyExpertBean> results = ((TyExpertMapper)getMapper()).queryCloseLevelNum(params);
		return results;
	}

	@Override
	public List<TyExpert> queryByActId(String actId) {
		return ((TyExpertMapper)getMapper()).queryByActId(actId);
	}

}
