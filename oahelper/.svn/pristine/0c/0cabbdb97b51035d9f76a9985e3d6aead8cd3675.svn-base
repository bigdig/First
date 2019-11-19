package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVote;
import com.tfzq.pr.model.pr.GoVoteBean;
import com.tfzq.pr.provider.GoVoteProvider;
import com.tfzq.pr.dao.generator.GoVoteMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goVote")
//@DubboService(interfaceClass = GoVoteProvider.class)
@Service
public class GoVoteProviderImpl extends BaseProviderImpl<GoVote> implements GoVoteProvider {
	//@Autowired
	//private GoVoteExpandMapper goVoteExpandMapper;

	public PageInfo<GoVote> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoVoteBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoVoteBean> pageInfo = getPage(userIds, GoVoteBean.class);
		return pageInfo;
	}

	public List<GoVoteBean> queryByCondition(Map<String, Object> params) {
		List<GoVoteBean> results = ((GoVoteMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
