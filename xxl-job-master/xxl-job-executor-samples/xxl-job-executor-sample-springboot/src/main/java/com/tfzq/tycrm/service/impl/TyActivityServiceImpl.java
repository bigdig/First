package com.tfzq.tycrm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyActivity;
import com.tfzq.tycrm.entity.TyActivitysign;
import com.tfzq.tycrm.mapper.TyActivityMapper;
import com.tfzq.tycrm.service.TyActivityService;

/**
 * @author pengtao
 */
@Service
public class TyActivityServiceImpl extends BaseServiceImpl<TyActivity>
		implements TyActivityService {

	@Override
	public List<TyActivity> findNotifyRecords(Map<String, Object> params) {
		return ((TyActivityMapper)getMapper()).findNotifyRecords(params);
	}
	
	public void updateSignByActivityId(TyActivitysign record){
		((TyActivityMapper)getMapper()).updateSignByActivityId(record);
	}

	@Override
	public List<String> getCustIdsByActivityId(String activityId) {
		return ((TyActivityMapper)getMapper()).getCustIdsByActivityId(activityId);
	}

}
