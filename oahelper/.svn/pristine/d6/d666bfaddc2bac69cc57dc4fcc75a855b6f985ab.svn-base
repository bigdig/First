package org.ibase4j.service.sys;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.sys.SysAreaBean;
import org.ibase4j.provider.SysAreaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author pengtao 
 */
@Service
public class SysAreaService extends BaseService<SysAreaProvider, SysArea> {
	
	@Autowired
	public void setProvider(SysAreaProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<SysAreaBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
