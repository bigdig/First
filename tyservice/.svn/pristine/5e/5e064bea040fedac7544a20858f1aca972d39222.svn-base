package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivityexpert;
import com.tfzq.ty.model.ty.TyActivityexpertBean;
import com.tfzq.ty.provider.TyActivityexpertProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivityexpertService extends BaseService<TyActivityexpertProvider, TyActivityexpert> {
//	@DubboReference
	@Autowired
	public void setProvider(TyActivityexpertProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivityexpertBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}

}
