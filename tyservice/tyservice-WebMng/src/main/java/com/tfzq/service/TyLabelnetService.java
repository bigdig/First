package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyLabelnet;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyLabelnetBean;
import com.tfzq.ty.provider.TyLabelnetProvider;

/**
 * @author pengtao 
 */
@Service
public class TyLabelnetService extends BaseService<TyLabelnetProvider, TyLabelnet> {
	//@DubboReference
	@Autowired
	public void setProvider(TyLabelnetProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyLabelnetBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<TyLabelnetBean> queryByCondition(Map<String,Object> params){
    	return provider.queryByCondition(params);
    }

	public void deleteByLabelId(String id) {
		provider.deleteByLabelId(id);
	}

}
