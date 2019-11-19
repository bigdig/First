package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserGroup;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.pr.provider.GoUserGroupProvider;
import com.tfzq.pr.dao.generator.GoUserGroupMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goUserGroup")
//@DubboService(interfaceClass = GoUserGroupProvider.class)
@Service
public class GoUserGroupProviderImpl extends BaseProviderImpl<GoUserGroup> implements GoUserGroupProvider {
	//@Autowired
	//private GoUserGroupExpandMapper goUserGroupExpandMapper;

	public PageInfo<GoUserGroup> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoUserGroupBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoUserGroupBean> pageInfo = getPage(userIds, GoUserGroupBean.class);
		return pageInfo;
	}

	public List<GoUserGroupBean> queryByCondition(Map<String, Object> params) {
		List<GoUserGroupBean> results = ((GoUserGroupMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
