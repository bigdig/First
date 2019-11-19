package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.core.util.DateUtil.DATE_PATTERN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyLabelMapper;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.provider.TyLabelProvider;

/**
 * @author pengtao 
 */
@Service
public class TyLabelService extends BaseService<TyLabelProvider, TyLabel> {
	@Autowired
	public void setProvider(TyLabelProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyLabelBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public String getId(){
            String redisKey = "REDIS_TBL_" + "TyLabel";
            String dateTime = DateUtil.getDateTime(DATE_PATTERN.YYYYMMDDHHMMSSSSS);
            return dateTime + StringUtils.right(StringUtils.leftPad(String.valueOf(RedisUtil.incr(redisKey)), 5, '0'), 5);
    }

	public List<TyLabelBean> getLabelCat() {
		return provider.getLabelCat();
	}

	public List<TyLabel> getAllSubRecords() {
		return provider.getAllSubRecords();
	}

	public List<TyLabel> selectByLabelName(String LabelName){
		return provider.selectByLabelName(LabelName);

	}

	public List<TyLabelBean> getCatListByCustomerId(String custId) {
		return provider.getCatListByCustomerId(custId);
	}

	public List<TyLabelBean> getCustSubList(Map<String, Object> pa) {
		return provider.getCustSubList(pa);
	}

	public List<TyLabel> getSubRecordsByCondition(Map<String, Object> params) {
		return provider.getSubRecordsByCondition(params);
	}

	public List<TyLabel> getRecordsByCondition(Map<String, Object> params) {
		return provider.getRecordsByCondition(params);
	}

	public PageInfo<TyLabelBean> querySubLabel(Map<String, Object> params) {
		return provider.querySubLabel(params);
	}

	public List<TyLabelBean> getCatListByOrgId(String orgId) {
		return provider.getCatListByOrgId(orgId);
	}

	public List<TyLabelBean> getOrgSubList(Map<String, Object> pa) {
		return provider.getOrgSubList(pa);
	}

	public List<TyLabelBean> getcurrentSubLabels(String id) {
		return provider.getcurrentSubLabels(id);
	}

	public List<TyLabelBean> getBindLabels(String labelId) {
		return provider.getBindLabels(labelId);
	}
	public List<TyLabelBean> getBindRelatedLabels(String labelId) {
		return provider.getBindRelatedLabels(labelId);
	}

	public void updateCustomerlabel(TyLabel oldRecord) {
		// TODO Auto-generated method stub
		provider.updateCustomerlabel(oldRecord);
	}

}
