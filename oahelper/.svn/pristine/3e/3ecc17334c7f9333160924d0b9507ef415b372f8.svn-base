package org.ibase4j.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.dao.generator.SysDeptMapper;
import org.ibase4j.dao.sys.SysDeptExpandMapper;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.sys.SysDeptBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@CacheConfig(cacheNames = "sysDept")
@DubboService(interfaceClass = SysDeptProvider.class)
@Service
public class SysDeptProviderImpl extends BaseProviderImpl<SysDept> implements SysDeptProvider {
	@Autowired
	private SysDeptExpandMapper syDeptExpandMapper;

	public PageInfo<SysDept> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(syDeptExpandMapper.query(params));
	}
	
	public PageInfo<SysDeptBean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<SysDeptBean> pageInfo = getPage(userIds, SysDeptBean.class);
		return pageInfo;
	}

	public List<SysDept> getAllList() {
		return getMapper().selectAll();
	}
	public String getComapnyIdByDeptId(String deptId){
		return syDeptExpandMapper.getComapnyIdByDeptId(deptId);
	}

	@Override
	public List<SysDept> getResearchRecords() {
		return ((SysDeptMapper)getMapper()).getResearchRecords();
	}
}
