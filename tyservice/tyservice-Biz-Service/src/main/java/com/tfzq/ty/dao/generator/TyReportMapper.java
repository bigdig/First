package com.tfzq.ty.dao.generator;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.tfzq.ty.model.generator.TyCustListReport;
import com.tfzq.ty.model.generator.TyReport;

/**
 * 
 * @author lujun
 * @Date   2018年3月21日
 */
public interface TyReportMapper {

	List<TyReport> queryCustAreaDist(Map<String, Object> params);

	List<TyReport> queryCustSaleDist(Map<String, Object> params);

	List<TyReport> queryCustTradeDist(Map<String, Object> params);

	Integer queryCustNum(Map<String, Object> params);

	List<TyReport> queryCustLabelDist(Map<String, Object> params);

	Page<TyCustListReport> custList(Map<String, Object> params);

	List<TyReport> queryCustActivityNum(String custId);

	Integer queryMyCustTotal(Map<String, Object> params);

	Integer queryNewAddCustNum(Map<String, Object> params);

	Integer queryJoinActiCustNum(Map<String, Object> params);

	List<TyReport> queryActivityNum(Map<String, Object> params);

	List<TyReport> queryActivityLike(Map<String, Object> params);

	Integer queryDeadlineOrgs(Map<String, Object> params);

}
