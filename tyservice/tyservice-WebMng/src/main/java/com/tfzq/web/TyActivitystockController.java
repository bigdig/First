package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
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
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.core.support.DictItem;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.ty.TyActivitystockBean;
import com.tfzq.service.TyActivitystockService;
import com.tfzq.util.PbFileUtils;

/**
 * 研究所活动参与员工控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "研究所活动参与员工管理", description = "研究所活动参与员工管理")
@RequestMapping(value = "ty/tyActivitystock", method = RequestMethod.POST)
public class TyActivitystockController extends BaseController {
	@Autowired
	private TyActivitystockService tyActivitystockService;
	@Autowired
	private SysDicService sysDicService;

	// 查询研究所活动参与员工
	@ApiOperation(value = "查询研究所活动参与员工")
	@RequiresPermissions("ty.tyActivitystock.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyActivitystockBean> list = tyActivitystockService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "研究所活动参与员工详情")
	@RequiresPermissions("ty.tyActivitystock.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivitystockBean tyActivitystockBean = new TyActivitystockBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivitystock record = tyActivitystockService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyActivitystockBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyActivitystockBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyActivitystockBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyActivitystockBean,dicts);
	}

	
	// 新增研究所活动参与员工
	@ApiOperation(value = "添加研究所活动参与员工")
	@RequiresPermissions("ty.tyActivitystock.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyActivitystock record = Request2ModelUtil.covert(TyActivitystock.class, request);
		tyActivitystockService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改研究所活动参与员工
	@ApiOperation(value = "修改研究所活动参与员工")
	@RequiresPermissions("ty.tyActivitystock.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyActivitystock record = Request2ModelUtil.covert(TyActivitystock.class, request);
		tyActivitystockService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除研究所活动参与员工
	@ApiOperation(value = "删除研究所活动参与员工")
	@RequiresPermissions("ty.tyActivitystock.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivitystock record = tyActivitystockService.queryById(id);
		if(record!=null){
			tyActivitystockService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
