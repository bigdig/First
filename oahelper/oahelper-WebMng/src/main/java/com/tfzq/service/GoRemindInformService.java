package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoRemindInform;
import com.tfzq.pr.model.pr.GoInformUserBean;
import com.tfzq.pr.model.pr.GoRemindInformBean;
import com.tfzq.pr.provider.GoRemindInformProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoRemindInformService extends BaseService<GoRemindInformProvider, GoRemindInform> {
	//@DubboReference
	@Autowired
	public void setProvider(GoRemindInformProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoRemindInformBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
	public List<GoRemindInformBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

}
