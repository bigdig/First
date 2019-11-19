package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfzq.service.TyActivitystockService;
import com.tfzq.service.TyDcrecostockService;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.generator.TyDcrecostock;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 股票池控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "股票池管理", description = "股票池管理")
@RequestMapping(value = "ty/tyDcrecostock", method = RequestMethod.POST)
public class TyDcrecostockController extends BaseController {
	@Autowired
	private TyDcrecostockService tyDcrecostockService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyActivitystockService tyActivitystockService;
	// @Autowired
	// private TyDcmorningstockService tyDcmorningstockService;
	@Autowired
	private SysDeptService sysDeptService;

	// 查询股票池
	@ApiOperation(value = "查询股票池推荐")
	@RequiresPermissions("ty.tyDcmorningcon.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "actId", required = false) String speakId) {
		List<TyDcrecostockBean> list = tyDcrecostockService.queryBySpeakId(speakId);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysDept> sysDepts = sysDeptService.getResearchRecords();
		addDictFromModel(dicts, "Researchdept", sysDepts, "id", "deptName");
		Map<String, String> recommendTypeMap = sysDicService.queryDicByDicIndexKey("RECOMMENDTYPE");
		addDictFromMap(dicts, "recommendTypes", recommendTypeMap);

		if (list != null && list.size() > 0) {
			for (TyDcrecostockBean tyDcrecostockBean : list) {
				// 添加字典翻译
				if (StringUtils.isNotEmpty(tyDcrecostockBean.getRecommendType())) {
					tyDcrecostockBean.setRecommendTypeName(
							recommendTypeMap.get(tyDcrecostockBean.getRecommendType().toString()));
				}
				if (StringUtils.isNotEmpty(tyDcrecostockBean.getTeamId())) {
					SysDept dept = sysDeptService.queryById(tyDcrecostockBean.getTeamId());
					if (dept != null && StringUtils.isNotEmpty(dept.getDeptName())) {
						tyDcrecostockBean.setTeam(dept.getDeptName());
					}
				}
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "股票池详情")
	@RequiresPermissions("ty.tyDcmorningcon.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyDcrecostockBean tyDcrecostockBean = new TyDcrecostockBean();
		Map<String, List<DictItem>> dicts = null;
		if (StringUtils.isNotEmpty(id)) {
			TyDcrecostock record = tyDcrecostockService.queryById(id);
			try {
				BeanUtils.copyProperties(tyDcrecostockBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyDcrecostockBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		if (tyDcrecostockBean != null) {
		}

		return setSuccessModelMap(modelMap, tyDcrecostockBean, dicts);
	}

	// 新增股票池
	@ApiOperation(value = "添加股票池")
	@RequiresPermissions("ty.tyDcmorningcon.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "actId", required = false) String speakId) {

		TyDcrecostock record = Request2ModelUtil.covert(TyDcrecostock.class, request);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("marketNo", record.getMarketNo());
		params.put("stockCode", record.getStockCode());
		List<TyDcrecostockBean> list = tyDcrecostockService.queryByCondition(params);
		TyDcrecostock newStock = null;
		if (list.size() == 0) {
			record.setDeleteFlag("0");
			newStock = tyDcrecostockService.add(record);
		} else {
			newStock = list.get(0);
			if ("1".equals(newStock.getDeleteFlag())) {
				try {
					BeanUtils.copyProperties(newStock, record);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newStock.setDeleteFlag("0");
				tyDcrecostockService.update(newStock);
			}
		}

		TyActivitystock morStock = new TyActivitystock();
		morStock.setActivityId(speakId);
		morStock.setStockId(newStock.getId());
		morStock.setActType(Constants.MORNING_CON);
		tyActivitystockService.add(morStock);

		return setSuccessModelMap(modelMap);
	}

	// // 修改股票池
	@ApiOperation(value = "修改股票池")
	@RequiresPermissions("ty.tyDcmorningcon.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyDcrecostock record = Request2ModelUtil.covert(TyDcrecostock.class, request);
		tyDcrecostockService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除股票池
	@ApiOperation(value = "删除股票池")
	@RequiresPermissions("ty.tyDcmorningcon.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "speakId", required = false) String activityId) {

		TyDcrecostock record = tyDcrecostockService.queryById(id);
		if (record != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("marketNo", record.getMarketNo());
			params.put("stockCode", record.getStockCode());
			List<TyDcrecostockBean> list = tyDcrecostockService.queryByCondition(params);
			if (list.size() == 1) {
				tyDcrecostockService.delete(id);
			}
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("activityId", activityId);
			params2.put("stockId", id);
			tyActivitystockService.deleteRecords(params2);
		}

		return setSuccessModelMap(modelMap);
	}
}
