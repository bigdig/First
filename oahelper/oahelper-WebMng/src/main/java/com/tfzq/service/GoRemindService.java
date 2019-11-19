package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoRemind;
import com.tfzq.pr.model.pr.GoRemindBean;
import com.tfzq.pr.provider.GoRemindProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoRemindService extends BaseService<GoRemindProvider, GoRemind> {
	//@DubboReference
	@Autowired
	public void setProvider(GoRemindProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoRemindBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
