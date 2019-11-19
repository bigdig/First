package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyMessageauditService;
import com.tfzq.service.TyMessagelogService;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.ty.TyMessagelogBean;

/**
 * 发送日志控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "发送日志管理", description = "发送日志管理")
@RequestMapping(value = "ty/tyMessagelog", method = RequestMethod.POST)
public class TyMessagelogController extends BaseController {
	@Autowired
	private TyMessagelogService tyMessagelogService;
	@Autowired
	private TyMessageauditService tyMessageauditService;

	// 查询发送日志
	@ApiOperation(value = "查询发送日志")
	@RequiresPermissions("ty.tyMessagelog.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyMessagelogBean> list = tyMessagelogService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		
		for(TyMessagelogBean message:list.getList()){
			TyMessageaudit audit =  tyMessageauditService.queryById(message.getMsgAuditid());
			message.setAudit(audit);
		}
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "发送日志详情")
	@RequiresPermissions("ty.tyMessagelog.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyMessagelogBean tyMessagelogBean = new TyMessagelogBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyMessagelog record = tyMessagelogService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyMessagelogBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyMessagelogBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyMessagelogBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyMessagelogBean,dicts);
	}

	
	// 新增发送日志
	@ApiOperation(value = "添加发送日志")
	@RequiresPermissions("ty.tyMessagelog.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyMessagelog record = Request2ModelUtil.covert(TyMessagelog.class, request);
		tyMessagelogService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改发送日志
	@ApiOperation(value = "修改发送日志")
	@RequiresPermissions("ty.tyMessagelog.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyMessagelog record = Request2ModelUtil.covert(TyMessagelog.class, request);
		tyMessagelogService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除发送日志
	@ApiOperation(value = "删除发送日志")
	@RequiresPermissions("ty.tyMessagelog.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyMessagelog record = tyMessagelogService.queryById(id);
		if(record!=null){
			tyMessagelogService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
