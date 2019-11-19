package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteItem;
import com.tfzq.pr.model.pr.GoVoteItemBean;
import com.tfzq.pr.provider.GoVoteItemProvider;
import com.tfzq.pr.dao.generator.GoVoteItemMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goVoteItem")
//@DubboService(interfaceClass = GoVoteItemProvider.class)
@Service
public class GoVoteItemProviderImpl extends BaseProviderImpl<GoVoteItem> implements GoVoteItemProvider {
	//@Autowired
	//private GoVoteItemExpandMapper goVoteItemExpandMapper;

	public PageInfo<GoVoteItem> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoVoteItemBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoVoteItemBean> pageInfo = getPage(userIds, GoVoteItemBean.class);
		return pageInfo;
	}

	public List<GoVoteItemBean> queryByCondition(Map<String, Object> params) {
		List<GoVoteItemBean> results = ((GoVoteItemMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
