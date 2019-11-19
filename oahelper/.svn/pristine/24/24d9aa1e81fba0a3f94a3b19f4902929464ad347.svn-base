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
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.ibase4j.core.support.DictItem;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.pr.GoGroupBean;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.service.GoGroupService;
import com.tfzq.service.GoUserGroupService;
import com.tfzq.util.PbFileUtils;

/**
 * 分组控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "分组管理", description = "分组管理")
@RequestMapping(value = "pr/goGroup", method = RequestMethod.POST)
public class GoGroupController extends BaseController {
	@Autowired
	private GoGroupService goGroupService;
	@Autowired
	private GoUserGroupService goUserGroupService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService sysUserService;

	// 查询分组
	@ApiOperation(value = "查询分组")
	// @RequiresPermissions("pr.goGroup.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "GROUP_TYPE asc");
		PageInfo<GoGroupBean> list = goGroupService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> group_typeMap = sysDicService.queryDicByDicIndexKey("GROUP_TYPE");
		addDictFromMap(dicts, "GROUP_TYPE", group_typeMap);
		for (GoGroupBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getGroupType())) {
				singleBean.setGroupTypeText(group_typeMap.get(singleBean.getGroupType().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "分组详情")
	// @RequiresPermissions("pr.goGroup.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoGroupBean goGroupBean = new GoGroupBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoGroup record = goGroupService.queryById(id);
			try {
				BeanUtils.copyProperties(goGroupBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goGroupBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> group_typeMap = sysDicService.queryDicByDicIndexKey("GROUP_TYPE");
		addDictFromMap(dicts, "GROUP_TYPE", group_typeMap);
		if (goGroupBean != null) {
			if (StringUtils.isNotEmpty(goGroupBean.getGroupType())) {
				goGroupBean.setGroupTypeText(group_typeMap.get(goGroupBean.getGroupType().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goGroupBean, dicts);
	}

	@ApiOperation(value = "查看")
	@RequestMapping(value = "/selectByCondition", method = RequestMethod.POST)
	public Object selectAllUsers(HttpServletRequest request, ModelMap modelMap) {

		Map<String, Object> params = WebUtil.getParameterMap(request);
		List<GoGroupBean> users = goGroupService.queryByCondition(params);

		return setSuccessModelMap(modelMap, users, null);
	}

	// 新增分组
	@ApiOperation(value = "添加分组")
	// @RequiresPermissions("pr.goGroup.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoGroup record = Request2ModelUtil.covert(GoGroup.class, request);
		goGroupService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改分组
	@ApiOperation(value = "修改分组")
	// @RequiresPermissions("pr.goGroup.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoGroup record = Request2ModelUtil.covert(GoGroup.class, request);
		goGroupService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除分组
	@ApiOperation(value = "删除分组")
	// @RequiresPermissions("pr.goGroup.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoGroup record = goGroupService.queryById(id);
		if (record != null) {
			goGroupService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
