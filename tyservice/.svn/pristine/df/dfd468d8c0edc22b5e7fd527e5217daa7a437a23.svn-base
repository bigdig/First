package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.model.ty.TyActivitystaffBean;
import com.tfzq.ty.provider.TyActivitystaffProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivitystaffService extends BaseService<TyActivitystaffProvider, TyActivitystaff> {
	//@DubboReference
	@Autowired
	public void setProvider(TyActivitystaffProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivitystaffBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
	public List<TyActivitystaffBean> queryByActId(String actId) {
		return provider.queryByActId(actId);
	}

	public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}

}
