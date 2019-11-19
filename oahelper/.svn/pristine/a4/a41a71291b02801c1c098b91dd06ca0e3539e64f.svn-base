package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVote;
import com.tfzq.pr.model.pr.GoVoteBean;
import com.tfzq.pr.provider.GoVoteProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoVoteService extends BaseService<GoVoteProvider, GoVote> {
	//@DubboReference
	@Autowired
	public void setProvider(GoVoteProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoVoteBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoVoteBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
