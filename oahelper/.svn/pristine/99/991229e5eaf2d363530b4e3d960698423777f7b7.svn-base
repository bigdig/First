package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoAudityRecord;
import com.tfzq.pr.model.pr.GoAudityRecordBean;
import com.tfzq.pr.provider.GoAudityRecordProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoAudityRecordService extends BaseService<GoAudityRecordProvider, GoAudityRecord> {
	//@DubboReference
	@Autowired
	public void setProvider(GoAudityRecordProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoAudityRecordBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoAudityRecordBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
