package com.tfzq.tycrm.mapper;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.TyServiceorg;

public interface TyServiceorgMapper extends BaseMapper<TyServiceorg> {

	void updateServiceOrgMarkByProc();

	List<TyServiceorg> findByUnionCondition(Map<String, Object> params);

	int updateByPushId(TyServiceorg result);
	
}
