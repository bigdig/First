package com.tfzq.tycrm.service;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseService;
import com.tfzq.tycrm.entity.TyActivity;
import com.tfzq.tycrm.entity.TyActivitysign;
/**
 *   @author pengtao 
 */

public interface TyActivityService extends BaseService<TyActivity> {
	/**
	 * 根据条件查询所有记录
	 * @param params
	 * @return
	 */
    List<TyActivity> findNotifyRecords(Map<String, Object> params);

	public void updateSignByActivityId(TyActivitysign record);

	List<String> getCustIdsByActivityId(String activityId);

}
