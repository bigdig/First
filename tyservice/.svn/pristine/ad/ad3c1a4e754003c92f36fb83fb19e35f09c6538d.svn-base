package com.tfzq.web;

import java.util.List;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfzq.service.TyCfundService;
import com.tfzq.service.TyCfundstockService;
import com.tfzq.ty.model.ty.TyCfundBean;
import com.tfzq.ty.model.ty.TyCfundstockBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 万得基金控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "万得基金管理", description = "万得基金管理")
@RequestMapping(value = "ty/tyCfund", method = RequestMethod.POST)
public class TyCfundController extends BaseController {
	@Autowired
	private TyCfundService tyCfundService;
	@Autowired
	private TyCfundstockService tyCfundstockService;

	// 查询万得基金
	@ApiOperation(value = "查询基金经理的基金")
	@RequestMapping(value = "/read/fundlist")
	public Object getFundlist(ModelMap modelMap,@RequestParam(value = "fundmanager", required = true) String fundmanager) {
		List<TyCfundBean> list = tyCfundService.queryByCondition(fundmanager);
		//添加字典翻译
		return setSuccessModelMap(modelMap, list);
	}
	// 查询万得基金
	@ApiOperation(value = "查询基金股票持仓")
	@RequestMapping(value = "/read/fundstocklist")
	public Object getFundstocklist(ModelMap modelMap,@RequestParam(value = "windcode", required = true) String windcode) {
		List<TyCfundstockBean> list = tyCfundstockService.queryByCondition(windcode);
		//添加字典翻译
		return setSuccessModelMap(modelMap, list);
	}

}
