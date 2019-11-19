package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.ty.TyActivitystockBean;
import com.tfzq.ty.provider.TyActivitystockProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivitystockService extends BaseService<TyActivitystockProvider, TyActivitystock> {
	//@DubboReference
	@Autowired
	public void setProvider(TyActivitystockProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivitystockBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}
    
	public int deleteRecords(Map<String, Object> params) {
		return provider.deleteRecords(params);
	}

}
