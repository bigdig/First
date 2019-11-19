package com.tfzq.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyServiceorglabel;
import com.tfzq.ty.model.ty.TyServiceorglabelBean;
import com.tfzq.ty.provider.TyServiceorglabelProvider;

@Service
public class TyServiceorglabelService extends BaseService<TyServiceorglabelProvider, TyServiceorglabel> {

	@Autowired
	public void setProvider(TyServiceorglabelProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyServiceorglabelBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyServiceorglabelBean> queryByOrgId(String orgId) {
    	return provider.queryByOrgId(orgId);
    }
	public int deleteByOrgId(String orgId) {
		return provider.deleteByOrgId(orgId);
	}

	public int deleteByLabelId(String labelId) {
		return provider.deleteByLabelId(labelId);
	}

	@Override
	public void update(TyServiceorglabel record) {
		record.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
//		record.setValidCount(record.getValidCount()+1);
		super.update(record);
	}

	@Override
	public TyServiceorglabel add(TyServiceorglabel record) {
		TyServiceorglabel orgLabel = this.queryById(record.getOrgId()+StringUtils.leftPad(record.getLabelId(), 22,'0'));
		if(orgLabel == null){
			record.setId(record.getOrgId()+StringUtils.leftPad(record.getLabelId(), 22,'0'));
			record.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
			record.setValidCount(1);
			return super.add(record);
		}else{
			this.update(orgLabel);
			return orgLabel;
		}
	}
}
