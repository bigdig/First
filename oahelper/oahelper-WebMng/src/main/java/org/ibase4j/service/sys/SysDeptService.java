package org.ibase4j.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.sys.SysDeptBean;
import org.ibase4j.provider.SysDeptProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:16:07
 */
@Service
public class SysDeptService extends BaseService<SysDeptProvider, SysDept> {
	@Autowired
	public void setProvider(SysDeptProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * 获取某结点以及以下机构
	 * @param deptId
	 * @return
	 */
	public Map<String,String> getSysDeptMap(String deptId){
		Set<String> parents = new HashSet<String>();
		boolean needAll = true;
		if(StringUtils.isNotBlank(deptId)){
			parents.add(deptId);
			needAll = false;
		}
		List<SysDept> list = provider.getAllList();
		Map<String,String> result = new HashMap<String,String>();
		for(SysDept dept:list){
			if(dept.getEnable()){
				if(needAll){
					result.put(dept.getId(), dept.getDeptName());					
				}else{
					if(dept.getId().equals(deptId) || parents.contains(dept.getParentId())){
						result.put(dept.getId(), dept.getDeptName());					
						parents.add(dept.getId());
					}
				}

			}
		}
		return result;
	}
	/**
	 * 获取某结点以及以下机构
	 * @param companyId
	 * @return
	 */
	public List<SysDept> getSysDeptList(String companyId){
		Set<String> parents = new HashSet<String>();
		boolean needAll = true;
		if(StringUtils.isNotBlank(companyId)){
			parents.add(companyId);
			needAll = false;
		}
		List<SysDept> list = provider.getAllList();
		List<SysDept> result = new ArrayList<SysDept>();
		for(SysDept dept:list){
			if(dept.getEnable()){
				if(needAll){
					result.add(dept);					
				}else{
					if(dept.getId().equals(companyId) || parents.contains(dept.getParentId())){
						result.add(dept);
						parents.add(dept.getId());
					}
				}
			}
		}
		return result;
	}
	public String getComapnyIdByDeptId(String deptId){
		return provider.getComapnyIdByDeptId(deptId);
	}
	
	public PageInfo<SysDeptBean> queryBeans(Map<String, Object> params){
		return provider.queryBeans(params);
	}

	public List<SysDept> getResearchRecords() {
		return provider.getResearchRecords();
	}
}
