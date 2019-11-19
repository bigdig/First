package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.SysUser;

/**
 * 由MyBatis Generator工具自动生成，请不要手动修改
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	
	List<SysUser> selectAll();
}