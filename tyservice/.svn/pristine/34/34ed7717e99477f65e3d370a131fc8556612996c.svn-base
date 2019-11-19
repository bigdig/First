package org.ibase4j.provider;

import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.provider.SysCacheProvider;
import org.springframework.stereotype.Service;

@DubboService(interfaceClass = SysCacheProvider.class)
@Service
public class SysCacheProviderImpl implements SysCacheProvider {

	// 清缓存
	public void flush() {
		RedisUtil.delAll("*:sysDics:*");
		RedisUtil.delAll("*:sysDicMap:*");
		RedisUtil.delAll("*:getAuthorize:*");
		RedisUtil.delAll("*:sysPermission:*");
	}
	public void flushAuthCaches() {
		RedisUtil.delAll("*:getAuthorize:*");
		RedisUtil.delAll("*:sysPermission:*");
	}
}