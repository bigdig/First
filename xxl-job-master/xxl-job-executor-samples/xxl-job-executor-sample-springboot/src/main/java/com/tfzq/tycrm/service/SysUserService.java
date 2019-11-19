package com.tfzq.tycrm.service;

import java.util.List;
import java.util.Map;

import com.ddfc.base.BaseService;
import com.tfzq.tycrm.entity.GoInformUser;
import com.tfzq.tycrm.entity.SysUser;
/**
 *   @author pengtao 
 */

public interface SysUserService extends BaseService<SysUser> {
	
	List<SysUser> selectAll();
}
