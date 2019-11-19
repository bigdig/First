package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteGroup;
import com.tfzq.pr.model.pr.GoVoteGroupBean;
import com.tfzq.pr.provider.GoVoteGroupProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoVoteGroupService extends BaseService<GoVoteGroupProvider, GoVoteGroup> {
	//@DubboReference
	@Autowired
	public void setProvider(GoVoteGroupProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoVoteGroupBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
