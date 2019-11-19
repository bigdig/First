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
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.provider.TyCustomerlabelProvider;

/**
 * @author pengtao 
 */
@Service
public class TyCustomerlabelService extends BaseService<TyCustomerlabelProvider, TyCustomerlabel> {
	@Autowired
	public void setProvider(TyCustomerlabelProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyCustomerlabelBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyCustomerlabelBean> queryByCustId(String customerId) {
    	return provider.queryByCustId(customerId);
    }
	public int deleteByCustId(String customerId) {
		return provider.deleteByCustId(customerId);
	}

	public int deleteByLabelId(String labelId) {
		return provider.deleteByLabelId(labelId);
	}

	@Override
	public void update(TyCustomerlabel record) {
		record.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
//		record.setValidCount(record.getValidCount()+1);
		super.update(record);
	}

	@Override
	public TyCustomerlabel add(TyCustomerlabel record) {
		TyCustomerlabel custLabel = this.queryById(StringUtils.leftPad(record.getCustomerId(),22,'0')+StringUtils.leftPad(record.getLabelId(), 22,'0'));
		if(custLabel == null){
			record.setId(StringUtils.leftPad(record.getCustomerId(),22,'0')+StringUtils.leftPad(record.getLabelId(), 22,'0'));
			record.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
			record.setValidCount(1);
			return super.add(record);
		}else{
			//this.update(custLabel);
			return custLabel;
		}
	}

	public void transferLableId(Map<String, Object> params1) {
		 provider.transferLableId(params1);
	}
	
	
}
