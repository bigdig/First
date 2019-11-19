package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoNotice;
import com.tfzq.pr.model.pr.GoNoticeBean;
import com.tfzq.pr.provider.GoNoticeProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoNoticeService extends BaseService<GoNoticeProvider, GoNotice> {
	//@DubboReference
	@Autowired
	public void setProvider(GoNoticeProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoNoticeBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
