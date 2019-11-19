package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcmorningcon;
import com.tfzq.ty.model.ty.TyDcmorningconBean;
import com.tfzq.ty.provider.TyDcmorningconProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcmorningconService extends BaseService<TyDcmorningconProvider, TyDcmorningcon> {
	@Autowired
	public void setProvider(TyDcmorningconProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcmorningconBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
