package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcsaloncust;
import com.tfzq.ty.model.ty.TyDcsaloncustBean;
import com.tfzq.ty.provider.TyDcsaloncustProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcsaloncustService extends BaseService<TyDcsaloncustProvider, TyDcsaloncust> {
	@Autowired
	public void setProvider(TyDcsaloncustProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcsaloncustBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyDcsaloncustBean> queryBySalonId(String salonId) {
		return provider.queryBySalonId(salonId);
	}

	public int deleteBySalonId(String salonId) {
		return provider.deleteBySalonId(salonId);
	}

}
