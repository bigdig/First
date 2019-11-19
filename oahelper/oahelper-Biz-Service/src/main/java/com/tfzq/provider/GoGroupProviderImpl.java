package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.pr.GoGroupBean;
import com.tfzq.pr.provider.GoGroupProvider;
import com.tfzq.pr.dao.generator.GoGroupMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goGroup")
//@DubboService(interfaceClass = GoGroupProvider.class)
@Service
public class GoGroupProviderImpl extends BaseProviderImpl<GoGroup> implements GoGroupProvider {
	//@Autowired
	//private GoGroupExpandMapper goGroupExpandMapper;

	public PageInfo<GoGroup> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoGroupBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoGroupBean> pageInfo = getPage(userIds, GoGroupBean.class);
		return pageInfo;
	}

	public List<GoGroupBean> queryByCondition(Map<String, Object> params) {
		List<GoGroupBean> results = ((GoGroupMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
