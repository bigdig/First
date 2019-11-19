package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyProjectjour;
import com.tfzq.ty.model.ty.TyProjectjourBean;
import com.tfzq.ty.provider.TyProjectjourProvider;

/**
 * @author pengtao 
 */
@Service
public class TyProjectjourService extends BaseService<TyProjectjourProvider, TyProjectjour> {
	//@DubboReference
	@Autowired
	public void setProvider(TyProjectjourProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyProjectjourBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
