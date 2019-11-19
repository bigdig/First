package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteUser;
import com.tfzq.pr.model.pr.GoVoteUserBean;
import com.tfzq.pr.provider.GoVoteUserProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoVoteUserService extends BaseService<GoVoteUserProvider, GoVoteUser> {
	//@DubboReference
	@Autowired
	public void setProvider(GoVoteUserProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoVoteUserBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoVoteUserBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
