package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.ty.TyMessageauditBean;
import com.tfzq.ty.provider.TyMessageauditProvider;

/**
 * @author pengtao 
 */
@Service
public class TyMessageauditService extends BaseService<TyMessageauditProvider, TyMessageaudit> {
	@Autowired
	public void setProvider(TyMessageauditProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyMessageauditBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
