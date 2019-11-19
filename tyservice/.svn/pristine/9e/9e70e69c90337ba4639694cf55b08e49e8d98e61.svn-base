package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyDcmorningstockMapper;
import com.tfzq.ty.model.generator.TyDcmorningstock;
import com.tfzq.ty.model.ty.TyDcmorningstockBean;
import com.tfzq.ty.provider.TyDcmorningstockProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyDcmorningstock")
//@DubboService(interfaceClass = TyDcmorningstockProvider.class)
@Service
public class TyDcmorningstockProviderImpl extends BaseProviderImpl<TyDcmorningstock> implements TyDcmorningstockProvider {
	//@Autowired
	//private TyDcmorningstockExpandMapper tyDcmorningstockExpandMapper;

	public PageInfo<TyDcmorningstock> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyDcmorningstockBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyDcmorningstockBean> pageInfo = getPage(userIds, TyDcmorningstockBean.class);
		return pageInfo;
	}

	@Override
	public List<String> getStockBySpeakId(String speakId) {
		return ((TyDcmorningstockMapper)getMapper()).getStockBySpeakId(speakId);
	}

	@Override
	public int deleteRecords(Map<String, Object> params) {
		return ((TyDcmorningstockMapper)getMapper()).deleteRecords(params);
	}

}
