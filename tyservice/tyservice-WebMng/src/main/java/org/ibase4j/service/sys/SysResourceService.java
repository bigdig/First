package org.ibase4j.service.sys;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysResource;
import org.ibase4j.model.sys.SysResourceBean;
import org.ibase4j.provider.SysResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author pengtao 
 */
@Service
public class SysResourceService extends BaseService<SysResourceProvider, SysResource> {
	@Autowired
	public void setProvider(SysResourceProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<SysResourceBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
