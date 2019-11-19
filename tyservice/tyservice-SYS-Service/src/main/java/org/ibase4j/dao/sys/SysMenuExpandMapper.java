package org.ibase4j.dao.sys;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseExpandMapper;
import org.ibase4j.model.generator.SysMenu;

public interface SysMenuExpandMapper extends BaseExpandMapper {
    /** 获取所有权限 */
    public List<Map<String, String>> getPermissions();

	public List<SysMenu> getAllListSorted();

}
