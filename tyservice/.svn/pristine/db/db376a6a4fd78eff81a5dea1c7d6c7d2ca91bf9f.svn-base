package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.ty.TyOrgprepcustBean;
import com.tfzq.ty.provider.TyOrgprepcustProvider;

/**
 * @author pengtao 
 */
@Service
public class TyOrgprepcustService extends BaseService<TyOrgprepcustProvider, TyOrgprepcust> {
	@Autowired
	public void setProvider(TyOrgprepcustProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyOrgprepcustBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<TyOrgprepcustBean> queryByCondition(Map<String, Object> params) {
    	return provider.queryByCondition(params);
    }

}
