package org.ibase4j.service.sys;

import org.ibase4j.provider.SysCacheProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysCacheService {
	@Autowired
	private SysCacheProvider sysCacheProvider;

	public void flushCache() {
		sysCacheProvider.flush();
	}
	public void flushAuthCaches() {
		sysCacheProvider.flushAuthCaches();
	}
	
}
