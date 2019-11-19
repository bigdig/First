package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcsalon;
import com.tfzq.ty.model.ty.TyDcsalonBean;
import com.tfzq.ty.provider.TyDcsalonProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcsalonService extends BaseService<TyDcsalonProvider, TyDcsalon> {
	@Autowired
	public void setProvider(TyDcsalonProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcsalonBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
