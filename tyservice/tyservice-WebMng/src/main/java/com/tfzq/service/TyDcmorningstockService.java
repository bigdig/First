package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDcmorningstock;
import com.tfzq.ty.model.ty.TyDcmorningstockBean;
import com.tfzq.ty.provider.TyDcmorningstockProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDcmorningstockService extends BaseService<TyDcmorningstockProvider, TyDcmorningstock> {
	@Autowired
	public void setProvider(TyDcmorningstockProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDcmorningstockBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<String> getStockBySpeakId(String speakId) {
		return provider.getStockBySpeakId(speakId);
	}

	public int deleteRecords(Map<String, Object> params) {
		return provider.deleteRecords(params);
	}

}
