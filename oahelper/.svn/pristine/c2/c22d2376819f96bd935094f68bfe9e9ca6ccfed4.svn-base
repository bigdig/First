package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserFavorite;
import com.tfzq.pr.model.pr.GoUserFavoriteBean;
import com.tfzq.pr.provider.GoUserFavoriteProvider;
import com.tfzq.pr.dao.generator.GoUserFavoriteMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goUserFavorite")
//@DubboService(interfaceClass = GoUserFavoriteProvider.class)
@Service
public class GoUserFavoriteProviderImpl extends BaseProviderImpl<GoUserFavorite> implements GoUserFavoriteProvider {
	//@Autowired
	//private GoUserFavoriteExpandMapper goUserFavoriteExpandMapper;

	public PageInfo<GoUserFavorite> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoUserFavoriteBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoUserFavoriteBean> pageInfo = getPage(userIds, GoUserFavoriteBean.class);
		return pageInfo;
	}

	public List<GoUserFavoriteBean> queryByCondition(Map<String, Object> params) {
		List<GoUserFavoriteBean> results = ((GoUserFavoriteMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
