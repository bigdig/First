package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.ibase4j.core.support.DictItem;

import com.tfzq.pr.model.generator.GoVote;
import com.tfzq.pr.model.generator.GoVoteUser;
import com.tfzq.pr.model.pr.GoVoteUserBean;
import com.tfzq.service.GoVoteService;
import com.tfzq.service.GoVoteUserService;
import com.tfzq.service.MyService;

/**
 * 投票情况控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "投票情况管理", description = "投票情况管理")
@RequestMapping(value = "pr/goVoteUser", method = RequestMethod.POST)
public class GoVoteUserController extends BaseController {
	@Autowired
	private GoVoteUserService goVoteUserService;
	@Autowired
	private GoVoteService goVoteService;
	@Autowired
	private MyService myService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;

	// 查询投票情况
	@ApiOperation(value = "查询投票情况")
	// @RequiresPermissions("pr.goVoteUser.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoVoteUserBean> list = goVoteUserService.queryBeans(params);

		Map<String, List<DictItem>> dicts = null;
		// 添加字典翻译
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "投票情况详情")
	// @RequiresPermissions("pr.goVoteUser.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoVoteUserBean goVoteUserBean = new GoVoteUserBean();
		Map<String, List<DictItem>> dicts = null;
		if (StringUtils.isNotEmpty(id)) {
			GoVoteUser record = goVoteUserService.queryById(id);
			try {
				BeanUtils.copyProperties(goVoteUserBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goVoteUserBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		if (goVoteUserBean != null) {
		}

		return setSuccessModelMap(modelMap, goVoteUserBean, dicts);
	}

	// 新增投票情况
	@ApiOperation(value = "添加投票情况")
	// @RequiresPermissions("pr.goVoteUser.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoVoteUser record = Request2ModelUtil.covert(GoVoteUser.class, request);
		goVoteUserService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 批量新增投票情况
	@ApiOperation(value = "批量添加投票情况")
	// @RequiresPermissions("pr.goVoteUser.add")
	@RequestMapping(value = "/addAll", method = RequestMethod.POST)
	public Object addAll(HttpServletRequest request, ModelMap modelMap) {
		String data = request.getParameter("data");
		List<GoVoteUser> arr = JSON.parseArray(data, GoVoteUser.class);
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());

		// 先删除用户已经投票信息
		if (arr.size() > 0) {
			GoVoteUser vu = arr.get(0);
			String voteId = vu.getVoteId();
			GoVote vote = goVoteService.queryById(voteId);
			myService.deleteUserVote(vote.getVoteGroupId(), vu.getUserId());
		}

		for (GoVoteUser record : arr) {
			record.setUserId(user.getId());
			record.setUserName(user.getUserName());
			goVoteUserService.add(record);
		}

		return setSuccessModelMap(modelMap);
	}

	// 修改投票情况
	@ApiOperation(value = "修改投票情况")
	// @RequiresPermissions("pr.goVoteUser.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoVoteUser record = Request2ModelUtil.covert(GoVoteUser.class, request);
		goVoteUserService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除投票情况
	@ApiOperation(value = "删除投票情况")
	// @RequiresPermissions("pr.goVoteUser.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoVoteUser record = goVoteUserService.queryById(id);
		if (record != null) {
			goVoteUserService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
