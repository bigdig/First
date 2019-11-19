package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDctopic;
import com.tfzq.ty.model.ty.TyDctopicBean;
import com.tfzq.ty.provider.TyDctopicProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDctopicService extends BaseService<TyDctopicProvider, TyDctopic> {
	@Autowired
	public void setProvider(TyDctopicProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDctopicBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
