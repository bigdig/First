package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteGroup;
import com.tfzq.pr.model.pr.GoVoteGroupBean;
import com.tfzq.pr.provider.GoVoteGroupProvider;
import com.tfzq.pr.dao.generator.GoVoteGroupMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goVoteGroup")
//@DubboService(interfaceClass = GoVoteGroupProvider.class)
@Service
public class GoVoteGroupProviderImpl extends BaseProviderImpl<GoVoteGroup> implements GoVoteGroupProvider {
	//@Autowired
	//private GoVoteGroupExpandMapper goVoteGroupExpandMapper;

	public PageInfo<GoVoteGroup> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoVoteGroupBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoVoteGroupBean> pageInfo = getPage(userIds, GoVoteGroupBean.class);
		return pageInfo;
	}

	public List<GoVoteGroupBean> queryByCondition(Map<String, Object> params) {
		List<GoVoteGroupBean> results = ((GoVoteGroupMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
