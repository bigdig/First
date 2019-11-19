package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoUserFavorite;
import com.tfzq.pr.model.pr.GoUserFavoriteBean;
import com.tfzq.pr.provider.GoUserFavoriteProvider;

/**
 * @author yuzhitao 
 */
@Service
public class GoUserFavoriteService extends BaseService<GoUserFavoriteProvider, GoUserFavorite> {
	//@DubboReference
	@Autowired
	public void setProvider(GoUserFavoriteProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<GoUserFavoriteBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public List<GoUserFavoriteBean> queryByCondition(Map<String, Object> params) {
        return provider.queryByCondition(params);
    }

}
