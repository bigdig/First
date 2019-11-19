package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustListReport;
import com.tfzq.ty.model.generator.TyReport;
import com.tfzq.ty.provider.TyReportProvider;

/**
 * 
 * @author lujun
 * @Date   2018年3月21日
 */
@Service
public class TyReportService {
	@Autowired
	public TyReportProvider provider;
		
	public List<TyReport> queryCustAreaDist(Map<String, Object> params) {
		return provider.queryCustAreaDist(params);
	}

	public List<TyReport> queryCustSaleDist(Map<String, Object> params) {
		return provider.queryCustSaleDist(params);
	}

	public List<TyReport> queryCustTradeDist(Map<String, Object> params) {
		return provider.queryCustTradeDist(params);
	}

	public Integer queryCustNum(Map<String, Object> params) {
		return provider.queryCustNum(params);
	}

	public List<TyReport> queryCustLabelDist(Map<String, Object> params) {
		return provider.queryCustLabelDist(params);
	}

	public PageInfo<TyCustListReport> custList(Map<String, Object> params) {
		return provider.custList(params);
	}

	public List<TyReport> queryCustActivityNum(String custId) {
		return provider.queryCustActivityNum(custId);
	}

	public Integer queryMyCustTotal(Map<String, Object> params) {
		return provider.queryMyCustTotal(params);
	}

	public Integer queryNewAddCustNum(Map<String, Object> params) {
		return provider.queryNewAddCustNum(params);
	}

	public Integer queryJoinActiCustNum(Map<String, Object> params) {
		return provider.queryJoinActiCustNum(params);
	}

	public List<TyReport> queryActivityNum(Map<String, Object> params) {
		return provider.queryActivityNum(params);
	}

	public List<TyReport> queryActivityLike(Map<String, Object> params) {
		return provider.queryActivityLike(params);
	}


	public Integer queryDeadlineOrgs(Map<String, Object> params) {
		return provider.queryDeadlineOrgs(params);
	}

}
