package com.tfzq.tycrm.service;


import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyCustomerlabel;

@Service
public class TyCustomerlabelServiceImpl extends BaseServiceImpl<TyCustomerlabel> implements TyCustomerlabelService {

	@Override
	public void add(TyCustomerlabel label) {
		this.insert(label);
	}


}
