package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.core.support.DictItem;
import com.tfzq.ty.model.generator.TyMsgtemplate;
import com.tfzq.ty.model.ty.TyMsgtemplateBean;
import com.tfzq.service.TyMsgtemplateService;

/**
 * 信息模板控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "信息模板管理", description = "信息模板管理")
@RequestMapping(value = "ty/tyMsgtemplate", method = RequestMethod.POST)
public class TyMsgtemplateController extends BaseController {
	@Autowired
	private TyMsgtemplateService tyMsgtemplateService;
	// 查询信息模板
	@ApiOperation(value = "查询信息模板")
	//@RequiresPermissions("ty.tyMsgtemplate.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyMsgtemplateBean> list = tyMsgtemplateService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "信息模板详情")
	@RequiresPermissions("ty.tyMsgtemplate.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyMsgtemplateBean tyMsgtemplateBean = new TyMsgtemplateBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyMsgtemplate record = tyMsgtemplateService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyMsgtemplateBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyMsgtemplateBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyMsgtemplateBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyMsgtemplateBean,dicts);
	}

	
	// 新增信息模板
	@ApiOperation(value = "添加信息模板")
	@RequiresPermissions("ty.tyMsgtemplate.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyMsgtemplate record = Request2ModelUtil.covert(TyMsgtemplate.class, request);
		tyMsgtemplateService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改信息模板
	@ApiOperation(value = "修改信息模板")
	@RequiresPermissions("ty.tyMsgtemplate.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyMsgtemplate record = Request2ModelUtil.covert(TyMsgtemplate.class, request);
		tyMsgtemplateService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除信息模板
	@ApiOperation(value = "删除信息模板")
	@RequiresPermissions("ty.tyMsgtemplate.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyMsgtemplate record = tyMsgtemplateService.queryById(id);
		if(record!=null){
			tyMsgtemplateService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
