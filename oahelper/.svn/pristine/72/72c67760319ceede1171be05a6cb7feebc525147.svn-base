package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.pr.GoGroupBean;
import com.tfzq.pr.provider.GoGroupProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoGroupService extends BaseService<GoGroupProvider, GoGroup> {
	//@DubboReference
	@Autowired
	public void setProvider(GoGroupProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoGroupBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoGroupBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
