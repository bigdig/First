package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoFileArchive;
import com.tfzq.pr.model.pr.GoFileArchiveBean;
import com.tfzq.pr.provider.GoFileArchiveProvider;
import com.tfzq.pr.dao.generator.GoFileArchiveMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goFileArchive")
//@DubboService(interfaceClass = GoFileArchiveProvider.class)
@Service
public class GoFileArchiveProviderImpl extends BaseProviderImpl<GoFileArchive> implements GoFileArchiveProvider {
	//@Autowired
	//private GoFileArchiveExpandMapper goFileArchiveExpandMapper;

	public PageInfo<GoFileArchive> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoFileArchiveBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoFileArchiveBean> pageInfo = getPage(userIds, GoFileArchiveBean.class);
		return pageInfo;
	}

	public List<GoFileArchiveBean> queryByCondition(Map<String, Object> params) {
		List<GoFileArchiveBean> results = ((GoFileArchiveMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
