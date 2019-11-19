package com.tfzq.web;

import io.swagger.annotations.Api;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.core.support.DictItem;

/**
 * 分类字典信息类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "分类字典信息管理", description = "分类字典信息管理")
@RequestMapping(value = "pr/myCatalog", method = RequestMethod.POST)
public class MyCatalogController extends BaseController {
	@Autowired
	private SysDicService sysDicService;

	@RequestMapping(value = "/read/catalog")
	public Object getCatalog(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "type", required = true) String type) {

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> typeMap = sysDicService.queryDicByDicIndexKey(type);
		addDictFromMap(dicts, type, typeMap);

		return setSuccessModelMap(modelMap, null, dicts);
	}

}
