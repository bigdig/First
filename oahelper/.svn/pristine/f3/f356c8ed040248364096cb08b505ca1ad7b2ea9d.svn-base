package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoAudity;
import com.tfzq.pr.model.pr.GoAudityBean;
import com.tfzq.pr.provider.GoAudityProvider;
import com.tfzq.pr.dao.generator.GoAudityMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goAudity")
//@DubboService(interfaceClass = GoAudityProvider.class)
@Service
public class GoAudityProviderImpl extends BaseProviderImpl<GoAudity> implements GoAudityProvider {
	//@Autowired
	//private GoAudityExpandMapper goAudityExpandMapper;

	public PageInfo<GoAudity> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoAudityBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoAudityBean> pageInfo = getPage(userIds, GoAudityBean.class);
		return pageInfo;
	}

	public List<GoAudityBean> queryByCondition(Map<String, Object> params) {
		List<GoAudityBean> results = ((GoAudityMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
