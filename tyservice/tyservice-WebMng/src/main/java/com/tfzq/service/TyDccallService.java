package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDccall;
import com.tfzq.ty.model.ty.TyDccallBean;
import com.tfzq.ty.provider.TyDccallProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDccallService extends BaseService<TyDccallProvider, TyDccall> {
	@Autowired
	public void setProvider(TyDccallProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDccallBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
