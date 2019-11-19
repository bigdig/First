package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.model.generator.SysRolebutton;
import org.ibase4j.model.sys.SysRolebuttonBean;

import com.github.pagehelper.PageInfo;
/**
 *   @author pengtao 
 */

public interface SysRolebuttonProvider extends BaseProvider<SysRolebutton> {
	
	public PageInfo<SysRolebuttonBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
