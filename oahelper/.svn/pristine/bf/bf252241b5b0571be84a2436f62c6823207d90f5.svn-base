package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoRemind;
import com.tfzq.pr.model.pr.GoRemindBean;
import com.tfzq.pr.provider.GoRemindProvider;
import com.tfzq.pr.dao.generator.GoRemindMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goRemind")
//@DubboService(interfaceClass = GoRemindProvider.class)
@Service
public class GoRemindProviderImpl extends BaseProviderImpl<GoRemind> implements GoRemindProvider {
	//@Autowired
	//private GoRemindExpandMapper goRemindExpandMapper;

	public PageInfo<GoRemind> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoRemindBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoRemindBean> pageInfo = getPage(userIds, GoRemindBean.class);
		return pageInfo;
	}

	public List<GoRemindBean> queryByCondition(Map<String, Object> params) {
		List<GoRemindBean> results = ((GoRemindMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
