package com.tfzq.tycrm.service.impl;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.TyOrgcustomer;
import com.tfzq.tycrm.mapper.TyOrgcustomerMapper;
import com.tfzq.tycrm.service.TyOrgcustomerService;

/**
 * @author pengtao
 */
@Service
public class TyOrgcustomerServiceImpl extends BaseServiceImpl<TyOrgcustomer>
		implements TyOrgcustomerService {

	public int calLabelScore(){
		return ((TyOrgcustomerMapper)getMapper()).calLabelScore();
	}

}
