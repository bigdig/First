package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoAudityRecord;
import com.tfzq.pr.model.pr.GoAudityRecordBean;
import com.tfzq.pr.provider.GoAudityRecordProvider;
import com.tfzq.pr.dao.generator.GoAudityRecordMapper;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goAudityRecord")
//@DubboService(interfaceClass = GoAudityRecordProvider.class)
@Service
public class GoAudityRecordProviderImpl extends BaseProviderImpl<GoAudityRecord> implements GoAudityRecordProvider {
	//@Autowired
	//private GoAudityRecordExpandMapper goAudityRecordExpandMapper;

	public PageInfo<GoAudityRecord> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoAudityRecordBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoAudityRecordBean> pageInfo = getPage(userIds, GoAudityRecordBean.class);
		return pageInfo;
	}

	public List<GoAudityRecordBean> queryByCondition(Map<String, Object> params) {
		List<GoAudityRecordBean> results = ((GoAudityRecordMapper)getMapper()).selectByCondition(params);
		return results;
	}

}
