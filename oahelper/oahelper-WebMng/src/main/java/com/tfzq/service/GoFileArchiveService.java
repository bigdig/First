package com.tfzq.service;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoFileArchive;
import com.tfzq.pr.model.pr.GoFileArchiveBean;
import com.tfzq.pr.provider.GoFileArchiveProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoFileArchiveService extends BaseService<GoFileArchiveProvider, GoFileArchive> {
	//@DubboReference
	@Autowired
	public void setProvider(GoFileArchiveProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoFileArchiveBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
