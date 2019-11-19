package com.tfzq.tycrm.service;

import com.ddfc.base.BaseService;
import com.github.pagehelper.PageInfo;
import com.tfzq.tycrm.entity.TyCrmListedcompany;
/**
 *   @author pengtao 
 */

public interface TyCrmListedcompanyService extends BaseService<TyCrmListedcompany> {
	
	public PageInfo<TyCrmListedcompany> queryLatestModifiedRecords(String lastestTime,String pageNum, String pageSize);
	
	//其他自定义的方法
	TyCrmListedcompany selectMaxUpdateTime(String id);

}
