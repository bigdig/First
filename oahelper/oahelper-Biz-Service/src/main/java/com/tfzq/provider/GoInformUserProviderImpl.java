package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.pr.dao.generator.GoInformUserMapper;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoInformUserBean;
import com.tfzq.pr.provider.GoInformUserProvider;

/**
 *   @author yuzhitao 
 */
@CacheConfig(cacheNames = "goInformUser")
//@DubboService(interfaceClass = GoInformUserProvider.class)
@Service
public class GoInformUserProviderImpl extends BaseProviderImpl<GoInformUser> implements GoInformUserProvider {
	//@Autowired
	//private GoInformUserExpandMapper goInformUserExpandMapper;

	public PageInfo<GoInformUser> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<GoInformUserBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<GoInformUserBean> pageInfo = getPage(userIds, GoInformUserBean.class);
		return pageInfo;
	}

	public List<GoInformUserBean> queryByCondition(Map<String, Object> params) {
		List<GoInformUserBean> results = ((GoInformUserMapper)getMapper()).selectByCondition(params);
		return results;
	}
	
	@Override
    public GoInformUser add(GoInformUser record) {
    	record.setIsSend("0");
    	return super.add(record);
    }

	@Override
	public void updateByInformId(Map<String, Object> params) {
		((GoInformUserMapper)getMapper()).updateByInformId(params);
	}


}