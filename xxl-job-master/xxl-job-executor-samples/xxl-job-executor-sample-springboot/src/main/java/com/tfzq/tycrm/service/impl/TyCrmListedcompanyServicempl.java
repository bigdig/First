package com.tfzq.tycrm.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ddfc.base.BaseServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tfzq.tycrm.entity.TyCrmListedcompany;
import com.tfzq.tycrm.mapper.TyCrmListedcompanyMapper;
import com.tfzq.tycrm.service.TyCrmListedcompanyService;

/**
 * @author pengtao
 */
@Service
public class TyCrmListedcompanyServicempl extends BaseServiceImpl<TyCrmListedcompany> implements TyCrmListedcompanyService {
	// @Autowired
	// private TyCrmListedcompanyExpandMapper TyCrmListedcompanyExpandMapper;

	@Override
	public PageInfo<TyCrmListedcompany> queryLatestModifiedRecords(String lastestTime, String pageNum, String pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastestTime", lastestTime);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("orderBy", "update_time asc");
		int pageNumI = params.get("pageNum") == null ? 1 : Integer.parseInt((String) params.get("pageNum"));
		int pageSizeI = params.get("pageSize") == null ? 1000 : Integer.parseInt((String) params.get("pageSize"));

		PageHelper.startPage(pageNumI, pageSizeI);
		Page<TyCrmListedcompany> results = ((TyCrmListedcompanyMapper) getMapper()).queryLatestModifiedRecords(params);
		return results.toPageInfo();
	}

	public TyCrmListedcompany selectMaxUpdateTime(String id){
		return ((TyCrmListedcompanyMapper) getMapper()).selectMaxUpdateTime(id);
	}


}
