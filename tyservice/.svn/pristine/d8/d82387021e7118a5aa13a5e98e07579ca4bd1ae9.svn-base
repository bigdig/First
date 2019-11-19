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
import com.tfzq.ty.model.generator.TyDcmorningstock;
import com.tfzq.ty.model.ty.TyDcmorningstockBean;
import com.tfzq.service.TyDcmorningstockService;

/**
 * 晨会股票推荐控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "晨会股票推荐管理", description = "晨会股票推荐管理")
@RequestMapping(value = "ty/tyDcmorningstock", method = RequestMethod.POST)
public class TyDcmorningstockController extends BaseController {
	@Autowired
	private TyDcmorningstockService tyDcmorningstockService;
	// 查询晨会股票推荐
	@ApiOperation(value = "查询晨会股票推荐")
	@RequiresPermissions("ty.tyDcmorningstock.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyDcmorningstockBean> list = tyDcmorningstockService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "晨会股票推荐详情")
	@RequiresPermissions("ty.tyDcmorningstock.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyDcmorningstockBean tyDcmorningstockBean = new TyDcmorningstockBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyDcmorningstock record = tyDcmorningstockService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDcmorningstockBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDcmorningstockBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDcmorningstockBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyDcmorningstockBean,dicts);
	}

	
	// 新增晨会股票推荐
	@ApiOperation(value = "添加晨会股票推荐")
	@RequiresPermissions("ty.tyDcmorningstock.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyDcmorningstock record = Request2ModelUtil.covert(TyDcmorningstock.class, request);
		tyDcmorningstockService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改晨会股票推荐
	@ApiOperation(value = "修改晨会股票推荐")
	@RequiresPermissions("ty.tyDcmorningstock.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyDcmorningstock record = Request2ModelUtil.covert(TyDcmorningstock.class, request);
		tyDcmorningstockService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除晨会股票推荐
	@ApiOperation(value = "删除晨会股票推荐")
	@RequiresPermissions("ty.tyDcmorningstock.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyDcmorningstock record = tyDcmorningstockService.queryById(id);
		if(record!=null){
			tyDcmorningstockService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
