package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.ty.TyActivitystockBean;
import com.tfzq.ty.provider.TyActivitystockProvider;
import com.tfzq.ty.dao.generator.TyActivitystaffMapper;
import com.tfzq.ty.dao.generator.TyActivitystockMapper;
import com.tfzq.ty.dao.generator.TyDcmorningstockMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyActivitystock")
//@DubboService(interfaceClass = TyActivitystockProvider.class)
@Service
public class TyActivitystockProviderImpl extends BaseProviderImpl<TyActivitystock> implements TyActivitystockProvider {
	//@Autowired
	//private TyActivitystockExpandMapper tyActivitystockExpandMapper;

	public PageInfo<TyActivitystock> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyActivitystockBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyActivitystockBean> pageInfo = getPage(userIds, TyActivitystockBean.class);
		return pageInfo;
	}

	public List<TyActivitystockBean> queryByCondition(Map<String, Object> params) {
		List<TyActivitystockBean> results = ((TyActivitystockMapper)getMapper()).selectByCondition(params);
		return results;
	}
	
	public int deleteByActId(String actId) {
		return ((TyActivitystockMapper)getMapper()).deleteByActId(actId);
	}
	
	@Override
	public int deleteRecords(Map<String, Object> params) {
		return ((TyActivitystockMapper)getMapper()).deleteRecords(params);
	}


}
