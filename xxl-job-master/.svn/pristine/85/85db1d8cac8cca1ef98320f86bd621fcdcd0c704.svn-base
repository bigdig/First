package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.TyActivity;
import com.tfzq.tycrm.entity.TyActivitysign;

/**
 *   @author pengtao 
 */
public interface TyActivityMapper extends BaseMapper<TyActivity> {
	
	List<TyActivity> findNotifyRecords(Map<String,Object> params);

	public void updateSignByActivityId(TyActivitysign record);

	List<String> getCustIdsByActivityId(String id);
}