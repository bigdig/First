package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyPreserviceorg;
import com.tfzq.ty.model.ty.TyPreserviceorgBean;
import com.tfzq.ty.provider.TyPreserviceorgProvider;

/**
 * @author pengtao 
 */
@Service
public class TyPreserviceorgService extends BaseService<TyPreserviceorgProvider, TyPreserviceorg> {
	@Autowired
	public void setProvider(TyPreserviceorgProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyPreserviceorgBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyPreserviceorgBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

	public List<TyPreserviceorgBean> getRecordByOrgName(String orgName) {
		return provider.getRecordByOrgName(orgName);
	}

	public PageInfo<TyPreserviceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		// TODO Auto-generated method stub
		return provider.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
	}

}
