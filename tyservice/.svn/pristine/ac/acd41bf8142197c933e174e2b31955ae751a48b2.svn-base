package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyDctopiccust;
import com.tfzq.ty.model.ty.TyDctopiccustBean;
import com.tfzq.ty.provider.TyDctopiccustProvider;

/**
 * @author pengtao 
 */
@Service
public class TyDctopiccustService extends BaseService<TyDctopiccustProvider, TyDctopiccust> {
	@Autowired
	public void setProvider(TyDctopiccustProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyDctopiccustBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyDctopiccustBean> queryByTopicId(String topicId) {
		return provider.queryByTopicId(topicId);
	}

	public int deleteByTopicId(String topicId) {
		return provider.deleteByTopicId(topicId);
	}

}
