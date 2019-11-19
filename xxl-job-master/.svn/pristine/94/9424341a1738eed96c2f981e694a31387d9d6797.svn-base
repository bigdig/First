package com.tfzq.tycrm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyServiceorg;
import com.tfzq.tycrm.mapper.TyServiceorgMapper;
import com.tfzq.tycrm.service.TyServiceorgService;

@Service
public class TyServiceorgServiceImpl extends BaseServiceImpl<TyServiceorg> implements TyServiceorgService{


	@Override
	public void updateServiceOrgMarkByProc() {
		((TyServiceorgMapper)getMapper()).updateServiceOrgMarkByProc();
		
	}

	@Override
	public List<TyServiceorg> findByUnionCondition(Map<String, Object> params) {
		return ((TyServiceorgMapper)getMapper()).findByUnionCondition(params);
	}

	@Override
	public void updateByPushId(TyServiceorg result) {
		((TyServiceorgMapper)getMapper()).updateByPushId(result);
	}


}
