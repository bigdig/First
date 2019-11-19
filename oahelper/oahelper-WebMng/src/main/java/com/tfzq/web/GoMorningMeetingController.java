package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.tfzq.pr.model.generator.GoMorningMeeting;
import com.tfzq.pr.model.pr.GoMorningMeetingBean;
import com.tfzq.pr.model.pr.GoUserMorningMeetingBean;
import com.tfzq.service.GoMorningMeetingService;
import com.tfzq.service.GoUserMorningMeetingService;
import com.tfzq.util.PbFileUtils;

/**
 * 晨会控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "晨会管理", description = "晨会管理")
@RequestMapping(value = "pr/goMorningMeeting", method = RequestMethod.POST)
public class GoMorningMeetingController extends BaseController {
	@Autowired
	private GoMorningMeetingService goMorningMeetingService;
	@Autowired
	private GoUserMorningMeetingService goUserMorningMeetingService;
	@Autowired
	private SysDicService sysDicService;

	// 查询晨会
	@ApiOperation(value = "查询晨会")
	// @RequiresPermissions("pr.goMorningMeeting.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoMorningMeetingBean> list = goMorningMeetingService.queryBeans(params);

		Map<String, List<DictItem>> dicts = null;
		// 添加字典翻译
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "晨会详情")
	// @RequiresPermissions("pr.goMorningMeeting.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoMorningMeetingBean goMorningMeetingBean = new GoMorningMeetingBean();
		Map<String, List<DictItem>> dicts = null;
		if (StringUtils.isNotEmpty(id)) {
			GoMorningMeeting record = goMorningMeetingService.queryById(id);
			try {
				BeanUtils.copyProperties(goMorningMeetingBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goMorningMeetingBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		if (goMorningMeetingBean != null) {
		}

		return setSuccessModelMap(modelMap, goMorningMeetingBean, dicts);
	}

	// 新增晨会
	@ApiOperation(value = "添加晨会")
	// @RequiresPermissions("pr.goMorningMeeting.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoMorningMeeting record = Request2ModelUtil.covert(GoMorningMeeting.class, request);
		goMorningMeetingService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改晨会
	@ApiOperation(value = "修改晨会")
	// @RequiresPermissions("pr.goMorningMeeting.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoMorningMeeting record = Request2ModelUtil.covert(GoMorningMeeting.class, request);
		goMorningMeetingService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除晨会
	@ApiOperation(value = "删除晨会")
	// @RequiresPermissions("pr.goMorningMeeting.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoMorningMeeting record = goMorningMeetingService.queryById(id);
		if (record != null) {
			goMorningMeetingService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}

}
