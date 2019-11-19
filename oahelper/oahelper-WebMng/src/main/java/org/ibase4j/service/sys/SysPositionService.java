package org.ibase4j.service.sys;

import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.model.sys.SysPositionBean;
import org.ibase4j.provider.SysPositionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author pengtao 
 */
@Service
public class SysPositionService extends BaseService<SysPositionProvider, SysPosition> {
	@Autowired
	public void setProvider(SysPositionProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<SysPositionBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
