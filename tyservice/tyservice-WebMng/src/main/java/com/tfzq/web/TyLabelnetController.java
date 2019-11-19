package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;

import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyLabelnet;
import com.tfzq.ty.model.ty.TyLabelnetBean;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyLabelnetService;
import com.tfzq.util.PbFileUtils;

/**
 * 活动控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "标签管理", description = "标签关联关系管理")
@RequestMapping(value = "ty/tyLabelnet", method = RequestMethod.POST)
public class TyLabelnetController extends BaseController {
	@Autowired
	private TyLabelnetService tyLabelnetService;
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private SysDicService sysDicService;

	// 查询活动
	@ApiOperation(value = "查询关联标签列表")
//	@RequiresPermissions("ty.tyLabelnet.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyLabelnetBean> list = tyLabelnetService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "查询对应标签的关联标签列表")
//	@RequiresPermissions("ty.tyLabelnet.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "labelId", required = false) String labelId) {
		Map<String,List<DictItem>> dicts = null;
		List<TyLabelnetBean> labelList = null;
		if(StringUtils.isNotEmpty(labelId)){
			Map<String,Object> params = new HashMap<>();
			params.put("deleteFlag", "0");
			params.put("labelIdFrom", labelId);
			labelList  = tyLabelnetService.queryByCondition(params);
	    }
	    
		return setSuccessModelMap(modelMap, labelList,dicts);
	}

	
	// 新增活动
	@ApiOperation(value = "添加标签关联关系")
//	@RequiresPermissions("ty.tyLabelnet.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyLabelnet record = Request2ModelUtil.covert(TyLabelnet.class, request);
		record.setDeleteFlag("0");
		if(StringUtils.isBlank(record.getLabelIdFrom())) {
			throw new BusinessException("关联的标签不能为空！");
		}
		if(StringUtils.isBlank(record.getLabelIdTo())) {
			throw new BusinessException("被关联的标签不能为空！");
		}
		if(StringUtils.equalsIgnoreCase(record.getLabelIdFrom(), record.getLabelIdTo())) {
			throw new BusinessException("不能关联自身标签！");
		}
		//查询是否已在标签关联关系
		Map<String,Object> params = new HashMap<>();
		params.put("deleteFlag", "0");
		params.put("labelIdFrom", record.getLabelIdFrom());
		List<TyLabelnetBean> bindLabels = tyLabelnetService.queryByCondition(params);
		for (TyLabelnetBean tyLabelnetBean : bindLabels) {
			if(StringUtils.equalsIgnoreCase(tyLabelnetBean.getId(), record.getLabelIdTo())) {
				throw new BusinessException("已存在该关联标签");
			}
		}
		tyLabelnetService.add(record);
		return setSuccessModelMap(modelMap);
		
	}

	// 修改活动
	@ApiOperation(value = "修改标签关联关系")
//	@RequiresPermissions("ty.tyLabelnet.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyLabelnet record = Request2ModelUtil.covert(TyLabelnet.class, request);
		tyLabelnetService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除活动
	@ApiOperation(value = "删除标签关联关系")
//	@RequiresPermissions("ty.tyLabelnet.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
//		TyLabelnet record = tyLabelnetService.queryById(id);
//		if(record!=null){
//			record.setDeleteFlag("1");
//			tyLabelnetService.update(record);
//		}
		tyLabelnetService.deletePhysical(id);
		return setSuccessModelMap(modelMap);
	}
}
