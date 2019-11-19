package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.provider.TyActivityProvider;

/**
 * @author pengtao 
 */
@Service
public class TyActivityService extends BaseService<TyActivityProvider, TyActivity> {
	//@DubboReference
	@Autowired
	public void setProvider(TyActivityProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyActivityBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<TyActivityBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }
    
    public int queryMyActCount(String staffId){
        return provider.queryMyActCount(staffId);

    }
    public TyActivity add(TyActivity record) {
    	record.setDeleteFlag("0");
    	return super.add(record);
    }


}
