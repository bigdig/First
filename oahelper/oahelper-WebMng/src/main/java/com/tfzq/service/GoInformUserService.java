package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoInformUserBean;
import com.tfzq.pr.provider.GoInformUserProvider;

/**
 * @author yuzhitao
 */
@Service
public class GoInformUserService extends BaseService<GoInformUserProvider, GoInformUser> {
	// @DubboReference
	@Autowired
	public void setProvider(GoInformUserProvider provider) {
		this.provider = provider;
	}

	public PageInfo<GoInformUserBean> queryBeans(Map<String, Object> params) {
		return provider.queryBeans(params);
	}

	public List<GoInformUserBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}
	
	public void updateByInformId(Map<String, Object> params){
		provider.updateByInformId(params);
	}

}
