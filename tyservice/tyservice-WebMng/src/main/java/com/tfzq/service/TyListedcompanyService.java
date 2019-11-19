package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.ty.TyListedcompanyBean;
import com.tfzq.ty.provider.TyListedcompanyProvider;

/**
 * @author pengtao 
 */
@Service
public class TyListedcompanyService extends BaseService<TyListedcompanyProvider, TyListedcompany> {
	@Autowired
	public void setProvider(TyListedcompanyProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyListedcompanyBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public PageInfo<TyListedcompanyBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		// TODO Auto-generated method stub
		return provider.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
	}

	public List<TyListedcompany> queryByActId(String actId) {
		return provider.queryByActId(actId);
	}

}
