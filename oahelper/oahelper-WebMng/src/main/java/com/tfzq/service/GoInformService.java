package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.pr.GoInformBean;
import com.tfzq.pr.model.pr.GoInformUserBean;
import com.tfzq.pr.provider.GoInformProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoInformService extends BaseService<GoInformProvider, GoInform> {
	//@DubboReference
	@Autowired
	public void setProvider(GoInformProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoInformBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

    public List<GoInformBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}
}
