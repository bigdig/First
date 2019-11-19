package org.ibase4j.service.sys;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysEvent;
import org.ibase4j.provider.SysEventProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:16:07
 */
@Service
public class SysEventManageService extends BaseService<SysEventProvider, SysEvent> {
	@Autowired
	public void setProvider(SysEventProvider provider) {
		this.provider = provider;
	}
	
}
