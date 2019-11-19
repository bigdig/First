package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMeeting;
import com.tfzq.ty.model.ty.TyMeetingBean;
import com.tfzq.ty.provider.TyMeetingProvider;

/**
 * @author pengtao 
 */
@Service
public class TyMeetingService extends BaseService<TyMeetingProvider, TyMeeting> {
	@Autowired
	public void setProvider(TyMeetingProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyMeetingBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
