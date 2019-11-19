package org.ibase4j.service.sys;

import java.util.HashMap;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysParam;
import org.ibase4j.provider.SysParamProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月31日 上午11:09:40
 */
@Service
public class SysParamService extends BaseService<SysParamProvider, SysParam> {
	@Autowired
	public void setProvider(SysParamProvider provider) {
		this.provider = provider;
	}
	
	public SysParam queryByKey(String paramKey,String companyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paramKey", paramKey);
		params.put("companyId", companyId);
		PageInfo<SysParam> pars = this.query(params);
		return pars.getList().size()>0?pars.getList().get(0):null;
	}
 
}
