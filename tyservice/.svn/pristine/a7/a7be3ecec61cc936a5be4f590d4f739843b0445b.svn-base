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
import com.tfzq.ty.model.generator.TyAppsecret;
import com.tfzq.ty.model.ty.TyAppsecretBean;
import com.tfzq.service.TyAppsecretService;

/**
 * 应用密钥控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "应用密钥管理", description = "应用密钥管理")
@RequestMapping(value = "ty/tyAppsecret", method = RequestMethod.POST)
public class TyAppsecretController extends BaseController {
	@Autowired
	private TyAppsecretService tyAppsecretService;
	// 查询应用密钥
	@ApiOperation(value = "查询应用密钥")
	@RequiresPermissions("ty.tyAppsecret.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyAppsecretBean> list = tyAppsecretService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "应用密钥详情")
	@RequiresPermissions("ty.tyAppsecret.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyAppsecretBean tyAppsecretBean = new TyAppsecretBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyAppsecret record = tyAppsecretService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyAppsecretBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyAppsecretBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyAppsecretBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyAppsecretBean,dicts);
	}

	
	// 新增应用密钥
	@ApiOperation(value = "添加应用密钥")
	@RequiresPermissions("ty.tyAppsecret.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyAppsecret record = Request2ModelUtil.covert(TyAppsecret.class, request);
		tyAppsecretService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改应用密钥
	@ApiOperation(value = "修改应用密钥")
	@RequiresPermissions("ty.tyAppsecret.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyAppsecret record = Request2ModelUtil.covert(TyAppsecret.class, request);
		tyAppsecretService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除应用密钥
	@ApiOperation(value = "删除应用密钥")
	@RequiresPermissions("ty.tyAppsecret.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyAppsecret record = tyAppsecretService.queryById(id);
		if(record!=null){
			tyAppsecretService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
