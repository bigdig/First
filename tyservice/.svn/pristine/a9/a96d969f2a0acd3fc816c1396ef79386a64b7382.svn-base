package com.tfzq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCfund;
import com.tfzq.ty.model.ty.TyCfundBean;
import com.tfzq.ty.provider.TyCfundProvider;

/**
 * @author pengtao 
 */
@Service
public class TyCfundService extends BaseService<TyCfundProvider, TyCfund> {
	//@DubboReference
	@Autowired
	public void setProvider(TyCfundProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyCfundBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    public List<TyCfundBean> queryByCondition(String fundmanager) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("fundmanager",fundmanager);
    	return provider.queryByCondition(params);
    }

}
