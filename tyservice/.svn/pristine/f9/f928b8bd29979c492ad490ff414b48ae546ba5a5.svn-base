package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustgroup;
import com.tfzq.ty.model.ty.TyCustgroupBean;
import com.tfzq.ty.provider.TyCustgroupProvider;

/**
 * @author pengtao 
 */
@Service
public class TyCustgroupService extends BaseService<TyCustgroupProvider, TyCustgroup> {
	@Autowired
	public void setProvider(TyCustgroupProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyCustgroupBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
