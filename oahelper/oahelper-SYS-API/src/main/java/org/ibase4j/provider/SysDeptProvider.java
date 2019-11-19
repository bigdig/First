package org.ibase4j.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.sys.SysDeptBean;

import com.github.pagehelper.PageInfo;

public interface SysDeptProvider extends BaseProvider<SysDept> {
	public List<SysDept> getAllList();
	
	public String getComapnyIdByDeptId(String deptId);
	
	public PageInfo<SysDeptBean> queryBeans(Map<String, Object> params);

	public List<SysDept> getResearchRecords();
}
