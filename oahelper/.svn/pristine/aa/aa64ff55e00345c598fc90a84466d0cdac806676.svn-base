package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoVoteItem;
import com.tfzq.pr.model.pr.GoVoteItemBean;
import com.tfzq.pr.provider.GoVoteItemProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoVoteItemService extends BaseService<GoVoteItemProvider, GoVoteItem> {
	//@DubboReference
	@Autowired
	public void setProvider(GoVoteItemProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoVoteItemBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoVoteItemBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
