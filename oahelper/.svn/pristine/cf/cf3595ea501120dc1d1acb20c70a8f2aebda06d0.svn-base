package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteUser;
import com.tfzq.pr.model.pr.GoVoteUserBean;
import com.tfzq.pr.provider.GoVoteUserProvider;
import com.tfzq.pr.dao.generator.GoVoteUserMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goVoteUser")
//@DubboService(interfaceClass = GoVoteUserProvider.class)
@Service
public class GoVoteUserProviderImpl extends BaseProviderImpl<GoVoteUser> implements GoVoteUserProvider {
	//@Autowired
	//private GoVoteUserExpandMapper goVoteUserExpandMapper;

	public PageInfo<GoVoteUser> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoVoteUserBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoVoteUserBean> pageInfo = getPage(userIds, GoVoteUserBean.class);
		return pageInfo;
	}

	public List<GoVoteUserBean> queryByCondition(Map<String, Object> params) {
		List<GoVoteUserBean> results = ((GoVoteUserMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
