package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.GoInformUser;
import com.tfzq.tycrm.entity.GoInformUserBean;

/**
 *   @author yuzhitao 
 */
public interface GoInformUserMapper extends BaseMapper<GoInformUser> {
	List<GoInformUser> findNotifyRecords(Map<String,Object> params);

	public void updateSendFlag(GoInformUser record);

}