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
import org.ibase4j.model.generator.SysPositionrole;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.service.sys.SysPositionroleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * 职务角色关系控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "职务角色关系管理", description = "职务角色关系管理")
@RequestMapping(value = "sys/sysPositionrole", method = RequestMethod.POST)
public class SysPositionroleController extends BaseController {
	@Autowired
	private SysPositionroleService sysPositionroleService;
	// 查询职务角色关系
	@ApiOperation(value = "查询职务角色关系")
	@RequiresPermissions("sys.sysPositionrole.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<SysPositionroleBean> list = sysPositionroleService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "职务角色关系详情")
	@RequiresPermissions("sys.sysPositionrole.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysPositionroleBean sysPositionroleBean = new SysPositionroleBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			SysPositionrole record = sysPositionroleService.queryById(id);
			try {
	            BeanUtils.copyProperties(sysPositionroleBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				sysPositionroleBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(sysPositionroleBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, sysPositionroleBean,dicts);
	}

	
	// 新增职务角色关系
	@ApiOperation(value = "添加职务角色关系")
	@RequiresPermissions("sys.sysPositionrole.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysPositionrole record = Request2ModelUtil.covert(SysPositionrole.class, request);
		sysPositionroleService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改职务角色关系
	@ApiOperation(value = "修改职务角色关系")
	@RequiresPermissions("sys.sysPositionrole.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysPositionrole record = Request2ModelUtil.covert(SysPositionrole.class, request);
		sysPositionroleService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除职务角色关系
	@ApiOperation(value = "删除职务角色关系")
	@RequiresPermissions("sys.sysPositionrole.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		SysPositionrole record = sysPositionroleService.queryById(id);
		if(record!=null){
			sysPositionroleService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
