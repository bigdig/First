package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyOrgcustomerpush;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.provider.TyOrgcustomerpushProvider;
import com.tfzq.ty.provider.TyServiceorgProvider;

/**
 * @author pengtao 
 */
@Service
public class TyServiceorgService extends BaseService<TyServiceorgProvider, TyServiceorg> {
	@Autowired
	private TyOrgcustomerpushProvider tyOrgcustomerpushProvider;

	@Autowired
	public void setProvider(TyServiceorgProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyServiceorgBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyServiceorgBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

	public List<TyServiceorgBean> getRecordByOrgName(String orgName) {
		return provider.getRecordByOrgName(orgName);
	}

	public PageInfo<TyServiceorgBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		// TODO Auto-generated method stub
		return provider.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
	}
	
	//删除机构同样要增加一条push记录
	public void delete(String id) {
		super.delete(id);
    	addServiceorgpush(id,WebUtil.getCurrentUser());
    }
	
	public TyServiceorg add(TyServiceorg record) {
		TyServiceorg result = super.add(record);
		addServiceorgpush(result.getId(),WebUtil.getCurrentUser());
		return result;
	}
	
	public void update(TyServiceorg record) {
		if(StringUtils.equals(record.getCustStatus(), "3")){
			record.setWhiteDeadline(0);//设为null
		}
		super.update(record);
		addServiceorgpush(record.getId(),WebUtil.getCurrentUser());
	}
	
	public void addServiceorgpush(String serviceorgId,String createBy) {
		// 新增因修改所需要的同步记录
		String plats = PropertiesUtil.getString("push.orgPlats");

		if (StringUtils.isNotEmpty(plats)) {
			String[] platArray = plats.split(",");
			for (String plat : platArray) {
				TyOrgcustomerpush push = new TyOrgcustomerpush();
				push.setCustomerId(serviceorgId);
				push.setPlatFlag(plat);
				push.setDealFlag("0");
				push.setCreateBy(createBy);
				tyOrgcustomerpushProvider.add(push);
			}
		}
	}

}
