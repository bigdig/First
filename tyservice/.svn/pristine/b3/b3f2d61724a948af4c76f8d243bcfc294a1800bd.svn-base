package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcactivity;
import com.tfzq.ty.model.ty.TyDcactivityBean;
import com.tfzq.ty.provider.TyDcactivityProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcactivityService extends BaseService<TyDcactivityProvider, TyDcactivity> {
	@Autowired
	public void setProvider(TyDcactivityProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcactivityBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}

}
