package com.tfzq.provider;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyProjectMapper;
import com.tfzq.ty.model.generator.TyProject;
import com.tfzq.ty.model.generator.TyProjectjour;
import com.tfzq.ty.model.ty.TyProjectBean;
import com.tfzq.ty.provider.TyProjectProvider;
import com.tfzq.ty.provider.TyProjectjourProvider;

/**
 *   @author pengtao 
 */
@CacheConfig(cacheNames = "tyProject")
//@DubboService(interfaceClass = TyProjectProvider.class)
@Service
public class TyProjectProviderImpl extends BaseProviderImpl<TyProject> implements TyProjectProvider {
	//@Autowired
	//private TyProjectExpandMapper tyProjectExpandMapper;
	@Autowired
	private TyProjectjourProvider tyProjectjourProvider;

	public PageInfo<TyProject> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<TyProjectBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<TyProjectBean> pageInfo = getPage(userIds, TyProjectBean.class);
		return pageInfo;
	}

	public List<TyProjectBean> queryByCondition(Map<String, Object> params) {
		List<TyProjectBean> results = ((TyProjectMapper)getMapper()).selectByCondition(params);
		return results;
	}

	@Override
	@Transactional
    public TyProject update(TyProject record) {
    	TyProject providerRecord = super.update(record);
    	addJour(providerRecord);
        return providerRecord;
    }
    
	@Override
	@Transactional
    public TyProject add(TyProject record) {
        TyProject providerRecord = super.add(record);
        addJour(providerRecord);
        return providerRecord;
    }

    private void addJour(TyProject record){
        try {
        	//记录修改记录
        	TyProjectjour jour = new TyProjectjour();
			BeanUtils.copyProperties(jour, record);
			jour.setId(null);
			jour.setCreateBy(record.getUpdateBy());
			jour.setCreateTime(record.getUpdateTime());
			jour.setProjectId(record.getId());
			tyProjectjourProvider.add(jour);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public TyProjectjourProvider getTyProjectjourProvider() {
		return tyProjectjourProvider;
	}

	public void setTyProjectjourProvider(TyProjectjourProvider tyProjectjourProvider) {
		this.tyProjectjourProvider = tyProjectjourProvider;
	}

}
