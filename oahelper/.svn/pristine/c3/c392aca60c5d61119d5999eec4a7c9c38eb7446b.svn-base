package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoAudity;
import com.tfzq.pr.model.pr.GoAudityBean;
import com.tfzq.pr.provider.GoAudityProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoAudityService extends BaseService<GoAudityProvider, GoAudity> {
	//@DubboReference
	@Autowired
	public void setProvider(GoAudityProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoAudityBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoAudityBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
