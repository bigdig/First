package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProject;
import com.tfzq.ty.model.ty.TyProjectBean;
import com.tfzq.ty.provider.TyProjectProvider;

/**
 * @author pengtao 
 */
@Service
public class TyProjectService extends BaseService<TyProjectProvider, TyProject> {
	//@DubboReference
	@Autowired
	public void setProvider(TyProjectProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyProjectBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
