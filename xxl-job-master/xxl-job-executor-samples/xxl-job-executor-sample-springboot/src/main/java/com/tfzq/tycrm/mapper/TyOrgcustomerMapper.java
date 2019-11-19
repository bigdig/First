package com.tfzq.tycrm.mapper;

import com.ddfc.base.BaseMapper;
import com.tfzq.tycrm.entity.TyOrgcustomer;

/**
 *   @author pengtao 
 */
public interface TyOrgcustomerMapper extends BaseMapper<TyOrgcustomer> {
	int calLabelScore();
}