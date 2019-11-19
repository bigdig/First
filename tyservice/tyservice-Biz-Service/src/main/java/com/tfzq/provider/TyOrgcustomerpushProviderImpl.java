package com.tfzq.provider;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseProviderImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyOrgcustomerpush;
import com.tfzq.ty.provider.TyOrgcustomerpushProvider;

/**
 * @author pengtao
 */
@CacheConfig(cacheNames = "tyOrgcustomer")
// @DubboService(interfaceClass = TyOrgcustomerProvider.class)
@Service
public class TyOrgcustomerpushProviderImpl extends BaseProviderImpl<TyOrgcustomerpush>
		implements TyOrgcustomerpushProvider {
	// @Autowired
	// private TyOrgcustomerExpandMapper tyOrgcustomerExpandMapper;

	public PageInfo<TyOrgcustomerpush> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}

	public TyOrgcustomerpush add(TyOrgcustomerpush record) {
		try {
			record.setEnable(true);
			record.setUpdateTime(new Date());
			if (StringUtils.isBlank(record.getId())) {
				String key = record.getClass().getSimpleName();
				record.setId(createId(key));
			}
			record.setCreateTime(new Date());
			getMapper().insert(record);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return record;
	}
}
