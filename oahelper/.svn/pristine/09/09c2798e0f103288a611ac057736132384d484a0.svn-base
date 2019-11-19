package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoCardApply;
import com.tfzq.pr.model.pr.GoCardApplyBean;
import com.tfzq.pr.provider.GoCardApplyProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoCardApplyService extends BaseService<GoCardApplyProvider, GoCardApply> {
	//@DubboReference
	@Autowired
	public void setProvider(GoCardApplyProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoCardApplyBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
