package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.model.ty.TyDctopiccustBean;
import com.tfzq.ty.provider.TyActivitysignProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivitysignService extends BaseService<TyActivitysignProvider, TyActivitysign> {
	//@DubboReference
	@Autowired
	public void setProvider(TyActivitysignProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivitysignBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<TyActivitysignBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

	public int deleteByActId(String actId) {
		return provider.deleteByActId(actId);
	}

	public List<TyActivitysignBean> queryByActId(String topicId) {
		return provider.queryByActId(topicId);
	}

	@Override
	public TyActivitysign add(TyActivitysign record) {
		record.setId(record.getActivityId()+StringUtils.leftPad(record.getCustId(), 22,'0'));
		return super.add(record);
	}
	
}
