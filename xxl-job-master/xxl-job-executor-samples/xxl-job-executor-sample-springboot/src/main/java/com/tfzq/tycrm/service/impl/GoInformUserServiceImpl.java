package com.tfzq.tycrm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.GoInformUser;
import com.tfzq.tycrm.entity.GoInformUserBean;
import com.tfzq.tycrm.mapper.GoInformUserMapper;
import com.tfzq.tycrm.service.GoInformUserService;

/**
 * @author pengtao
 */
@Service
public class GoInformUserServiceImpl extends BaseServiceImpl<GoInformUser>
		implements GoInformUserService {

	@Override
	public List<GoInformUser> findNotifyRecords(Map<String, Object> params) {
		return ((GoInformUserMapper)getMapper()).findNotifyRecords(params);
	}
	
	public int update(GoInformUser record) {
		this.setNamespace("go:");
		int ret =super.update(record);
		return ret;
	}

}
