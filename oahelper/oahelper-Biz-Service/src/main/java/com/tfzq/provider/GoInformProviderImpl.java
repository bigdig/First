package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.pr.GoInformBean;
import com.tfzq.pr.provider.GoInformProvider;
import com.tfzq.pr.dao.generator.GoInformMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goInform")
//@DubboService(interfaceClass = GoInformProvider.class)
@Service
public class GoInformProviderImpl extends BaseProviderImpl<GoInform> implements GoInformProvider {
	//@Autowired
	//private GoInformExpandMapper goInformExpandMapper;

	public PageInfo<GoInform> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoInformBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoInformBean> pageInfo = getPage(userIds, GoInformBean.class);
		return pageInfo;
	}

	public List<GoInformBean> queryByCondition(Map<String, Object> params) {
		List<GoInformBean> results = ((GoInformMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
