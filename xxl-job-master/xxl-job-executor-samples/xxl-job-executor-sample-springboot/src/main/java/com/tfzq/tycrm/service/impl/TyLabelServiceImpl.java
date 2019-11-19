package com.tfzq.tycrm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyLabel;
import com.tfzq.tycrm.mapper.TyLabelMapper;
import com.tfzq.tycrm.service.TyLabelService;

@Service
public class TyLabelServiceImpl extends BaseServiceImpl<TyLabel> implements TyLabelService {

	@Override
	public List<TyLabel> getAllSubRecords() {
		return ((TyLabelMapper)getMapper()).getAllSubRecords();
	}

	@Override
	public TyLabel add(TyLabel lb) {
		this.insert(lb);
		return lb;
	}


}
