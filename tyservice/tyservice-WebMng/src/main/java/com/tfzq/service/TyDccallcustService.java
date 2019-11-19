package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDccallcust;
import com.tfzq.ty.model.ty.TyDccallcustBean;
import com.tfzq.ty.provider.TyDccallcustProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDccallcustService extends BaseService<TyDccallcustProvider, TyDccallcust> {
	@Autowired
	public void setProvider(TyDccallcustProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDccallcustBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyDccallcustBean> queryByCallId(String callId) {
		return provider.queryByCallId(callId);
	}

	public int deleteByCallId(String callId) {
		return provider.deleteByCallId(callId);
	}

}
