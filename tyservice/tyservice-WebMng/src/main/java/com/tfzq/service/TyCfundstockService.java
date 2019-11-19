package com.tfzq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCfundstock;
import com.tfzq.ty.model.ty.TyCfundstockBean;
import com.tfzq.ty.provider.TyCfundstockProvider;

/**
 * @author pengtao 
 */
@Service
public class TyCfundstockService extends BaseService<TyCfundstockProvider, TyCfundstock> {
	//@DubboReference
	@Autowired
	public void setProvider(TyCfundstockProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyCfundstockBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    public List<TyCfundstockBean> queryByCondition(String windcode) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("windcode",windcode);
    	return provider.queryByCondition(params);
    }

}
