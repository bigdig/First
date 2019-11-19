package com.tfzq.tycrm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.tfzq.tycrm.entity.GoInformUser;
import com.tfzq.tycrm.entity.SysUser;
import com.tfzq.tycrm.mapper.GoInformUserMapper;
import com.tfzq.tycrm.mapper.SysUserMapper;
import com.tfzq.tycrm.service.GoInformUserService;
import com.tfzq.tycrm.service.SysUserService;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService{
	
	@Override
	public List<SysUser> selectAll() {
		return  ((SysUserMapper)getMapper()).selectAll();
	}
}
