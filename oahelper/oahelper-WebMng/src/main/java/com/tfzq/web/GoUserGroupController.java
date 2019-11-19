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
import com.tfzq.pr.model.generator.GoUserGroup;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.service.GoUserGroupService;
import com.tfzq.service.GoGroupService;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.util.PbFileUtils;

/**
 * 用户分组控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "用户分组管理", description = "用户分组管理")
@RequestMapping(value = "pr/goUserGroup", method = RequestMethod.POST)
public class GoUserGroupController extends BaseController {
	@Autowired
	private GoUserGroupService goUserGroupService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private GoGroupService goGroupService;

	// 查询用户分组
	@ApiOperation(value = "查询用户分组")
	//@RequiresPermissions("pr.goUserGroup.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<GoUserGroupBean> list = goUserGroupService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		for (GoUserGroupBean singleBean : list.getList()) {
			if(StringUtils.isNotEmpty(singleBean.getGroupId())){
				GoGroup temp = goGroupService.queryById(singleBean.getGroupId());
				singleBean.setGroupName(temp==null?"":temp.getGroupName());
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}
	

	// 查询用户分组
	@ApiOperation(value = "条件查询用户分组")
	//@RequiresPermissions("pr.goUserGroup.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		List<GoUserGroupBean> list = goUserGroupService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		for (GoUserGroupBean singleBean : list) {
			if(StringUtils.isNotEmpty(singleBean.getGroupId())){
				GoGroup temp = goGroupService.queryById(singleBean.getGroupId());
				singleBean.setGroupName(temp==null?"":temp.getGroupName());
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "用户分组详情")
	//@RequiresPermissions("pr.goUserGroup.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoUserGroupBean goUserGroupBean = new GoUserGroupBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			GoUserGroup record = goUserGroupService.queryById(id);
			try {
	            BeanUtils.copyProperties(goUserGroupBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goUserGroupBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		List<GoGroup> goGroups = goGroupService.getAllRecords();
		addDictFromModel(dicts,"GoGroups", goGroups,"id","groupName");
	    if(goUserGroupBean!=null){
			if(StringUtils.isNotEmpty(goUserGroupBean.getGroupId())){
				GoGroup temp = goGroupService.queryById(goUserGroupBean.getGroupId());
				goUserGroupBean.setGroupName(temp==null?"":temp.getGroupName());
			}
	    }
	    
		return setSuccessModelMap(modelMap, goUserGroupBean,dicts);
	}

	
	// 新增用户分组
	@ApiOperation(value = "添加用户分组")
	//@RequiresPermissions("pr.goUserGroup.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoUserGroup record = Request2ModelUtil.covert(GoUserGroup.class, request);
		goUserGroupService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改用户分组
	@ApiOperation(value = "修改用户分组")
	//@RequiresPermissions("pr.goUserGroup.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoUserGroup record = Request2ModelUtil.covert(GoUserGroup.class, request);
		goUserGroupService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除用户分组
	@ApiOperation(value = "删除用户分组")
	//@RequiresPermissions("pr.goUserGroup.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoUserGroup record = goUserGroupService.queryById(id);
		if(record!=null){
			goUserGroupService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
