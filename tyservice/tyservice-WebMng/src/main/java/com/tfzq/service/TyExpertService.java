package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.ty.TyExpertBean;
import com.tfzq.ty.provider.TyExpertProvider;

/**
 * @author pengtao 
 */
@Service
public class TyExpertService extends BaseService<TyExpertProvider, TyExpert> {
	//@DubboReference
	@Autowired
	public void setProvider(TyExpertProvider provider) {
		this.provider = provider;
	}
    
    public PageInfo<TyExpertBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<TyExpertBean> queryIndustryNum(Map<String, Object> params) {
        return provider.queryIndustryNum(params);
    }
    
    public List<TyExpertBean> queryCloseLevelNum(Map<String, Object> params) {
        return provider.queryCloseLevelNum(params);
    }

	public List<TyExpert> queryByActId(String actId) {
		return provider.queryByActId(actId);
	}

}
