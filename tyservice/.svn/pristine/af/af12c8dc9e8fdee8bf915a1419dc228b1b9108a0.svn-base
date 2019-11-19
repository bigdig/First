package com.tfzq.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.ty.provider.TyStafflistProvider;

/**
 * @author pengtao 
 */
@Service
public class TyStafflistService extends BaseService<TyStafflistProvider, TyStafflist> {
	@Autowired
	public void setProvider(TyStafflistProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyStafflistBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<TyStafflist> getSalerRecords() {
		return provider.getSalerRecords();
	}

	public List<TyStafflist> getRecordBySalerName(String saler) {
		return provider.getRecordBySalerName(saler);
	}

	public List<TyStafflist> queryByActId(String actId) {
		return provider.queryByActId(actId);
	}

	public List<TyStafflist> getResearcherRecords() {
		return provider.getResearcherRecords();
	}

	public PageInfo<TyStafflistBean> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize) {
		// TODO Auto-generated method stub
		return provider.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
	}
	
	public TyStafflistBean queryByUserId(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return provider.queryByCondition(params).get(0);
	}
	public List<TyStafflistBean> queryByCondition(Map<String, Object> params){
		return provider.queryByCondition(params);
	}

	public boolean isSeniorseller(List<SysUserRoleBean> surb) {
		boolean result = false;
		for (Iterator<SysUserRoleBean> iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if ("seniorseller".equals(sysUserRoleBean.getRoleId())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean isSeller(List<SysUserRoleBean> surb) {
		boolean result = false;
		for (Iterator<SysUserRoleBean> iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if ("seller".equals(sysUserRoleBean.getRoleId())) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public boolean isSysmng(List<SysUserRoleBean> surb) {
		boolean result = false;
		for (Iterator<SysUserRoleBean> iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if ("sysmng".equals(sysUserRoleBean.getRoleId())) {
				result = true;
				break;
			}
		}
		return result;
	}
}
