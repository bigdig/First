package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcrecostock;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.ty.provider.TyDcrecostockProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcrecostockService extends BaseService<TyDcrecostockProvider, TyDcrecostock> {
	@Autowired
	public void setProvider(TyDcrecostockProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcrecostockBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyDcrecostockBean> queryBySpeakId(String speakId) {
		return provider.queryBySpeakId(speakId);
	}
	
	public List<TyDcrecostockBean> queryByCondition(Map<String, Object> params){
		return provider.queryByCondition(params);
	}

}
