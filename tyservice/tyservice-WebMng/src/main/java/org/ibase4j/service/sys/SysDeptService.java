package org.ibase4j.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public Map<String,String> getSysDeptMap(){
		List<SysDept> list = provider.getAllList();
		Map<String,String> result = new HashMap<String,String>();
		for(SysDept dept:list){
			if(dept.getEnable()){
				result.put(dept.getId(), dept.getDeptName());
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
