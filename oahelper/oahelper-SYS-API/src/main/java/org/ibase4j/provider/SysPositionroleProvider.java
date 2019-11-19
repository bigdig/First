package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.model.generator.SysPositionrole;
import org.ibase4j.model.sys.SysPositionroleBean;

import com.github.pagehelper.PageInfo;
/**
 *   @author pengtao 
 */

public interface SysPositionroleProvider extends BaseProvider<SysPositionrole> {
	
	public PageInfo<SysPositionroleBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
