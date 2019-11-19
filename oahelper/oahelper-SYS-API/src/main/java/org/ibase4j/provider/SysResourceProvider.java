package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;
import org.ibase4j.model.generator.SysResource;
import org.ibase4j.model.sys.SysResourceBean;

import com.github.pagehelper.PageInfo;
/**
 *   @author pengtao 
 */

public interface SysResourceProvider extends BaseProvider<SysResource> {
	
	public PageInfo<SysResourceBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
