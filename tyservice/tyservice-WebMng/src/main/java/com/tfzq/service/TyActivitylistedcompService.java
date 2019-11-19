package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitylistedcomp;
import com.tfzq.ty.model.ty.TyActivitylistedcompBean;
import com.tfzq.ty.provider.TyActivitylistedcompProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivitylistedcompService extends BaseService<TyActivitylistedcompProvider, TyActivitylistedcomp> {
//	@DubboReference
	@Autowired
	public void setProvider(TyActivitylistedcompProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivitylistedcompBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}

}
