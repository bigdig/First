package org.ibase4j.provider.impl;

import java.util.Iterator;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.dao.generator.PbPublishmentMapper;
import org.ibase4j.model.generator.PbPublishment;
import org.ibase4j.provider.PushPublishmentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
public class PushPublishmentProviderImpl extends BaseProviderImpl<PbPublishment> implements PushPublishmentProvider{
	@Autowired
	private PbPublishmentMapper pbPublishmentMapper;

	public void doPushTask() {
		List<PbPublishment> list = this.getPushDatas();
		
		pbPublishmentMapper.doPushTask();
		
		for (Iterator<PbPublishment> iterator = list.iterator(); iterator.hasNext();) {
			PbPublishment pbPublishment = (PbPublishment) iterator.next();
			RedisUtil.del(getCacheKey("pbPublishment", pbPublishment.getId()));
		}
	}
	
	public List<PbPublishment> getPushDatas(){
		return pbPublishmentMapper.getPushDatas();
	}

}
