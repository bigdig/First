package org.ibase4j.provider.impl;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.dao.generator.TyMessagelogMapper;
import org.ibase4j.model.generator.TyMessagelog;
import org.ibase4j.provider.TyMessagelogProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 *   @author pengtao 
 */
@Service
public class TyMessagelogProviderImpl extends BaseProviderImpl<TyMessagelog> implements TyMessagelogProvider {
	@Autowired
	private TyMessagelogMapper tyMessagelogMapper;

	public PageInfo<TyMessagelog> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	

}
