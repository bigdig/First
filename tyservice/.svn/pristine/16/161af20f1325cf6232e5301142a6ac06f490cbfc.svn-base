package org.ibase4j.web.sys;

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
import org.ibase4j.model.generator.SysRolebutton;
import org.ibase4j.model.sys.SysRolebuttonBean;
import org.ibase4j.service.sys.SysRolebuttonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * 角色按钮权限控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "角色按钮权限管理", description = "角色按钮权限管理")
@RequestMapping(value = "sys/sysRolebutton", method = RequestMethod.POST)
public class SysRolebuttonController extends BaseController {
	@Autowired
	private SysRolebuttonService sysRolebuttonService;
	// 查询角色按钮权限
	@ApiOperation(value = "查询角色按钮权限")
	@RequiresPermissions("sys.sysRolebutton.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<SysRolebuttonBean> list = sysRolebuttonService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "角色按钮权限详情")
	@RequiresPermissions("sys.sysRolebutton.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysRolebuttonBean sysRolebuttonBean = new SysRolebuttonBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			SysRolebutton record = sysRolebuttonService.queryById(id);
			try {
	            BeanUtils.copyProperties(sysRolebuttonBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				sysRolebuttonBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(sysRolebuttonBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, sysRolebuttonBean,dicts);
	}

	
	// 新增角色按钮权限
	@ApiOperation(value = "添加角色按钮权限")
	@RequiresPermissions("sys.sysRolebutton.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysRolebutton record = Request2ModelUtil.covert(SysRolebutton.class, request);
		sysRolebuttonService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改角色按钮权限
	@ApiOperation(value = "修改角色按钮权限")
	@RequiresPermissions("sys.sysRolebutton.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysRolebutton record = Request2ModelUtil.covert(SysRolebutton.class, request);
		sysRolebuttonService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除角色按钮权限
	@ApiOperation(value = "删除角色按钮权限")
	@RequiresPermissions("sys.sysRolebutton.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		SysRolebutton record = sysRolebuttonService.queryById(id);
		if(record!=null){
			sysRolebuttonService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
