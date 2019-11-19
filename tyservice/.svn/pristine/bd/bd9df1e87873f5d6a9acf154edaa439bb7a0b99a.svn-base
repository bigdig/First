package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMsgtemplate;
import com.tfzq.ty.model.ty.TyMsgtemplateBean;
import com.tfzq.ty.provider.TyMsgtemplateProvider;

/**
 * @author pengtao 
 */
@Service
public class TyMsgtemplateService extends BaseService<TyMsgtemplateProvider, TyMsgtemplate> {
	@Autowired
	public void setProvider(TyMsgtemplateProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyMsgtemplateBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
