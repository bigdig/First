package com.tfzq.service;

import org.ibase4j.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfzq.pb.model.generator.PbFile;
import com.tfzq.pb.provider.PbFileProvider;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:16:07
 */
@Service
public class PbFileService extends BaseService<PbFileProvider, PbFile> {
	@Autowired
	public void setProvider(PbFileProvider provider) {
		this.provider = provider;
	}

}
