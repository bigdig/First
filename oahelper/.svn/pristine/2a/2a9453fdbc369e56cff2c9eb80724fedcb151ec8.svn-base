package org.ibase4j.service.sys;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysPositionrole;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.provider.SysPositionroleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author pengtao 
 */
@Service
public class SysPositionroleService extends BaseService<SysPositionroleProvider, SysPositionrole> {
	@Autowired
	public void setProvider(SysPositionroleProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<SysPositionroleBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
