package com.tfzq.provider;

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyLabelnet;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyLabelnetBean;
import com.tfzq.ty.provider.TyLabelnetProvider;
import com.tfzq.ty.dao.generator.TyLabelnetMapper;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyLabelnet")
//@DubboService(interfaceClass = TyLabelnetProvider.class)
@Service
public class TyLabelnetProviderImpl extends BaseProviderImpl<TyLabelnet> implements TyLabelnetProvider {
	//@Autowired
	//private TyLabelnetExpandMapper tyLabelnetExpandMapper;

	public PageInfo<TyLabelnet> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyLabelnetBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyLabelnetBean> pageInfo = getPage(userIds, TyLabelnetBean.class);
		return pageInfo;
	}

	public List<TyLabelnetBean> queryByCondition(Map<String, Object> params) {
		List<TyLabelnetBean> results = ((TyLabelnetMapper)getMapper()).selectByCondition(params);
		return results;
	}

	@Override
	public void deleteByLabelId(String labelId) {
		((TyLabelnetMapper)getMapper()).deleteByLabelId(labelId);
	}
}
