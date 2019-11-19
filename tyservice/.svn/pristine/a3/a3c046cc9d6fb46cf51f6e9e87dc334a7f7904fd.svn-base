package org.ibase4j.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.dao.sys.SysEventExpandMapper;
import org.ibase4j.model.generator.SysEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

@CacheConfig(cacheNames = "sysEvent")
@DubboService(interfaceClass = SysEventProvider.class)
@Service
public class SysEventProviderImpl extends BaseProviderImpl<SysEvent> implements SysEventProvider {
	@Autowired
	private SysEventExpandMapper sysEventExpandMapper;

	public PageInfo<SysEvent> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(sysEventExpandMapper.query(params));
	}
}
