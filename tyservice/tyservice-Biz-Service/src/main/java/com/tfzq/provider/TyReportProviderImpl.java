package com.tfzq.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.dao.generator.TyReportMapper;
import com.tfzq.ty.model.generator.TyCustListReport;
import com.tfzq.ty.model.generator.TyReport;
import com.tfzq.ty.provider.TyReportProvider;

/**
 * 
 * @author lujun
 * @Date   2018年3月21日
 */
@CacheConfig(cacheNames = "tyReport")
@Service
public class TyReportProviderImpl implements TyReportProvider{
	@Autowired
	private TyReportMapper tyReportMapper ;

	@Override
	public List<TyReport> queryCustAreaDist(Map<String, Object> params) {
		return tyReportMapper.queryCustAreaDist(params);
	}

	@Override
	public List<TyReport> queryCustSaleDist(Map<String, Object> params) {
		return tyReportMapper.queryCustSaleDist(params);
	}

	@Override
	public List<TyReport> queryCustTradeDist(Map<String, Object> params) {
		return tyReportMapper.queryCustTradeDist(params);
	}

	@Override
	public Integer queryCustNum(Map<String, Object> params) {
		return tyReportMapper.queryCustNum(params);
	}

	@Override
	public List<TyReport> queryCustLabelDist(Map<String, Object> params) {
		return tyReportMapper.queryCustLabelDist(params);
	}

	@Override
	public PageInfo<TyCustListReport> custList(Map<String, Object> params) {
		startPage(params);
		Page<TyCustListReport> result = tyReportMapper.custList(params);
		return result.toPageInfo();
	}
	
	  /** 启动分页查询 */
    protected void startPage(Map<String, Object> params) {
        if (DataUtil.isEmpty(params.get("pageNum"))) {
            params.put("pageNum", 1);
        }
        if (DataUtil.isEmpty(params.get("pageSize"))) {
            params.put("pageSize", 10);
        }
        if (DataUtil.isEmpty(params.get("orderBy"))) {
            params.put("orderBy", "id desc");
        }
        PageHelper.startPage(params);
    }

	@Override
	public List<TyReport> queryCustActivityNum(String custId) {
		return tyReportMapper.queryCustActivityNum(custId);
	}

	@Override
	public Integer queryMyCustTotal(Map<String, Object> params) {
		return tyReportMapper.queryMyCustTotal(params);
	}

	@Override
	public Integer queryNewAddCustNum(Map<String, Object> params) {
		return tyReportMapper.queryNewAddCustNum(params);
	}

	@Override
	public Integer queryJoinActiCustNum(Map<String, Object> params) {
		return tyReportMapper.queryJoinActiCustNum(params);
	}

	@Override
	public List<TyReport> queryActivityNum(Map<String, Object> params) {
		return tyReportMapper.queryActivityNum(params);
	}

	@Override
	public List<TyReport> queryActivityLike(Map<String, Object> params) {
		return tyReportMapper.queryActivityLike(params);
	}

	@Override
	public Integer queryDeadlineOrgs(Map<String, Object> params) {
		return tyReportMapper.queryDeadlineOrgs(params);
	}
}
