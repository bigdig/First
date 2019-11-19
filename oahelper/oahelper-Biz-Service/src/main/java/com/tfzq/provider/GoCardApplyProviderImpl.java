package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoCardApply;
import com.tfzq.pr.model.pr.GoCardApplyBean;
import com.tfzq.pr.provider.GoCardApplyProvider;
import com.tfzq.pr.dao.generator.GoCardApplyMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goCardApply")
//@DubboService(interfaceClass = GoCardApplyProvider.class)
@Service
public class GoCardApplyProviderImpl extends BaseProviderImpl<GoCardApply> implements GoCardApplyProvider {
	//@Autowired
	//private GoCardApplyExpandMapper goCardApplyExpandMapper;

	public PageInfo<GoCardApply> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoCardApplyBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoCardApplyBean> pageInfo = getPage(userIds, GoCardApplyBean.class);
		return pageInfo;
	}

	public List<GoCardApplyBean> queryByCondition(Map<String, Object> params) {
		List<GoCardApplyBean> results = ((GoCardApplyMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
