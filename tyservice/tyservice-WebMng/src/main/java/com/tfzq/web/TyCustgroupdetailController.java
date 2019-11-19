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
import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;
import com.tfzq.service.TyCustgroupdetailService;

/**
 * 客户分组明细控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "客户分组明细管理", description = "客户分组明细管理")
@RequestMapping(value = "ty/tyCustgroupdetail", method = RequestMethod.POST)
public class TyCustgroupdetailController extends BaseController {
	@Autowired
	private TyCustgroupdetailService tyCustgroupdetailService;
	// 查询客户分组明细
	@ApiOperation(value = "查询客户分组明细")
	//@RequiresPermissions("ty.tyCustgroupdetail.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyCustgroupdetailBean> list = tyCustgroupdetailService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "客户分组明细详情")
	//@RequiresPermissions("ty.tyCustgroupdetail.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyCustgroupdetailBean tyCustgroupdetailBean = new TyCustgroupdetailBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyCustgroupdetail record = tyCustgroupdetailService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyCustgroupdetailBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyCustgroupdetailBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyCustgroupdetailBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyCustgroupdetailBean,dicts);
	}

	
	// 新增客户分组明细
	@ApiOperation(value = "添加客户分组明细")
	//@RequiresPermissions("ty.tyCustgroupdetail.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyCustgroupdetail record = Request2ModelUtil.covert(TyCustgroupdetail.class, request);
		tyCustgroupdetailService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改客户分组明细
	@ApiOperation(value = "修改客户分组明细")
	//@RequiresPermissions("ty.tyCustgroupdetail.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyCustgroupdetail record = Request2ModelUtil.covert(TyCustgroupdetail.class, request);
		tyCustgroupdetailService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除客户分组明细
	@ApiOperation(value = "删除客户分组明细")
	//@RequiresPermissions("ty.tyCustgroupdetail.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyCustgroupdetail record = tyCustgroupdetailService.queryById(id);
		if(record!=null){
			tyCustgroupdetailService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
