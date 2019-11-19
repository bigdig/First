package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoRemindInform;
import com.tfzq.pr.model.pr.GoRemindInformBean;
import com.tfzq.pr.provider.GoRemindInformProvider;
import com.tfzq.pr.dao.generator.GoRemindInformMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goRemindInform")
//@DubboService(interfaceClass = GoRemindInformProvider.class)
@Service
public class GoRemindInformProviderImpl extends BaseProviderImpl<GoRemindInform> implements GoRemindInformProvider {
	//@Autowired
	//private GoRemindInformExpandMapper goRemindInformExpandMapper;

	public PageInfo<GoRemindInform> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoRemindInformBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoRemindInformBean> pageInfo = getPage(userIds, GoRemindInformBean.class);
		return pageInfo;
	}

	public List<GoRemindInformBean> queryByCondition(Map<String, Object> params) {
		List<GoRemindInformBean> results = ((GoRemindInformMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
