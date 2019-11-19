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
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.ibase4j.core.support.DictItem;

import com.tfzq.pr.model.generator.GoFileArchive;
import com.tfzq.pr.model.generator.GoNotice;
import com.tfzq.pr.model.generator.GoUserFavorite;
import com.tfzq.pr.model.pr.GoUserFavoriteBean;
import com.tfzq.service.GoFileArchiveService;
import com.tfzq.service.GoNoticeService;
import com.tfzq.service.GoUserFavoriteService;
import com.tfzq.util.PbFileUtils;

/**
 * 用户收藏控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "用户收藏管理", description = "用户收藏管理")
@RequestMapping(value = "pr/goUserFavorite", method = RequestMethod.POST)
public class GoUserFavoriteController extends BaseController {
	@Autowired
	private GoUserFavoriteService goUserFavoriteService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private GoNoticeService goNoticeService;
	@Autowired
	private GoFileArchiveService goFileArchiveService;

	// 查询用户收藏
	@ApiOperation(value = "查询用户收藏")
	// @RequiresPermissions("pr.goUserFavorite.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoUserFavoriteBean> list = goUserFavoriteService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> favor_typeMap = sysDicService.queryDicByDicIndexKey("FAVOR_TYPE");
		addDictFromMap(dicts, "FAVOR_TYPE", favor_typeMap);
		for (GoUserFavoriteBean singleBean : list.getList()) {
			if (singleBean.getFavoriteType().equals("NOTICE")) {
				GoNotice notice = goNoticeService.queryById(singleBean.getFavoriteId());
				singleBean.setFavoriteTitle(notice.getNoticeTitle());
				singleBean.setFavoriteTime(notice.getCreateTime());
			} else if (singleBean.getFavoriteType().equals("FILE")) {
				GoFileArchive file = goFileArchiveService.queryById(singleBean.getFavoriteId());
				singleBean.setFavoriteTitle(file.getFileName());
				singleBean.setFavoriteTime(file.getCreateTime());
				singleBean.setFavoritePath(file.getFilePath());
			}
			if (StringUtils.isNotEmpty(singleBean.getFavoriteType())) {
				singleBean.setFavoriteTypeText(favor_typeMap.get(singleBean.getFavoriteType().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 查询用户收藏
	@ApiOperation(value = "查询用户收藏")
	// @RequiresPermissions("pr.goUserFavorite.read")
	@RequestMapping(value = "/selectByUser")
	public Object selectByUser(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		params.put("userId", WebUtil.getCurrentUser());
		List<GoUserFavoriteBean> list = goUserFavoriteService.queryByCondition(params);
		return setSuccessModelMap(modelMap, list, null);
	}

	// 详细信息
	@ApiOperation(value = "用户收藏详情")
	// @RequiresPermissions("pr.goUserFavorite.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoUserFavoriteBean goUserFavoriteBean = new GoUserFavoriteBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoUserFavorite record = goUserFavoriteService.queryById(id);
			try {
				BeanUtils.copyProperties(goUserFavoriteBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goUserFavoriteBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> favor_typeMap = sysDicService.queryDicByDicIndexKey("FAVOR_TYPE");
		addDictFromMap(dicts, "FAVOR_TYPE", favor_typeMap);
		if (goUserFavoriteBean != null) {
			if (StringUtils.isNotEmpty(goUserFavoriteBean.getFavoriteType())) {
				goUserFavoriteBean
						.setFavoriteTypeText(favor_typeMap.get(goUserFavoriteBean.getFavoriteType().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goUserFavoriteBean, dicts);
	}

	// 新增用户收藏
	@ApiOperation(value = "添加用户收藏")
	// @RequiresPermissions("pr.goUserFavorite.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoUserFavorite record = Request2ModelUtil.covert(GoUserFavorite.class, request);
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		record.setUserId(user.getId());
		record.setUserName(user.getUserName());
		goUserFavoriteService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改用户收藏
	@ApiOperation(value = "修改用户收藏")
	// @RequiresPermissions("pr.goUserFavorite.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoUserFavorite record = Request2ModelUtil.covert(GoUserFavorite.class, request);
		goUserFavoriteService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除用户收藏
	@ApiOperation(value = "删除用户收藏")
	// @RequiresPermissions("pr.goUserFavorite.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("userId", WebUtil.getCurrentUser());
		List<GoUserFavoriteBean> list = goUserFavoriteService.queryByCondition(params);
		for (GoUserFavoriteBean uf : list) {
			goUserFavoriteService.deletePhysical(uf.getId());
		}
		return setSuccessModelMap(modelMap);
	}

}
