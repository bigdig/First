package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProjecttrack;
import com.tfzq.ty.model.ty.TyProjecttrackBean;
import com.tfzq.ty.provider.TyProjecttrackProvider;

/**
 * @author pengtao 
 */
@Service
public class TyProjecttrackService extends BaseService<TyProjecttrackProvider, TyProjecttrack> {
	//@DubboReference
	@Autowired
	public void setProvider(TyProjecttrackProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyProjecttrackBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
