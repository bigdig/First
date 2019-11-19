package org.ibase4j.service.sys;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysRolebutton;
import org.ibase4j.model.sys.SysRolebuttonBean;
import org.ibase4j.provider.SysRolebuttonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author pengtao 
 */
@Service
public class SysRolebuttonService extends BaseService<SysRolebuttonProvider, SysRolebutton> {
	@Autowired
	public void setProvider(SysRolebuttonProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<SysRolebuttonBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
