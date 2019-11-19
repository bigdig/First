package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserGroup;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.pr.provider.GoUserGroupProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoUserGroupService extends BaseService<GoUserGroupProvider, GoUserGroup> {
	//@DubboReference
	@Autowired
	public void setProvider(GoUserGroupProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoUserGroupBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
	
	public List<GoUserGroupBean> queryByCondition(Map<String, Object> params){
		return provider.queryByCondition(params);
	}

}
