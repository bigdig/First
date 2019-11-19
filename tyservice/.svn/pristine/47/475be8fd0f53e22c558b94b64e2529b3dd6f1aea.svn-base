package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyServiceorglabelMapper;
import com.tfzq.ty.model.generator.TyServiceorglabel;
import com.tfzq.ty.model.ty.TyServiceorglabelBean;
import com.tfzq.ty.provider.TyServiceorglabelProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "TyServiceorglabel")
//@DubboService(interfaceClass = TyServiceorglabelProvider.class)
@Service
public class TyServiceorglabelProviderImpl extends BaseProviderImpl<TyServiceorglabel> implements TyServiceorglabelProvider {
	//@Autowired
	//private TyServiceorglabelExpandMapper TyServiceorglabelExpandMapper;

	public PageInfo<TyServiceorglabel> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyServiceorglabelBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyServiceorglabelBean> pageInfo = getPage(userIds, TyServiceorglabelBean.class);
		return pageInfo;
	}

	@Override
	public int deleteByOrgId(String orgId) {
		return ((TyServiceorglabelMapper)getMapper()).deleteByOrgId(orgId);
	}

	@Override
	public int deleteByLabelId(String labelId) {
		return ((TyServiceorglabelMapper)getMapper()).deleteByLabelId(labelId);
	}

	@Override
	public List<TyServiceorglabelBean> queryByOrgId(String orgId) {
		return ((TyServiceorglabelMapper)getMapper()).queryByOrgId(orgId);
	}

}
