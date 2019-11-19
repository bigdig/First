package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
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

import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.generator.GoNotice;
import com.tfzq.pr.model.pr.GoNoticeBean;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.service.GoGroupService;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;
import com.tfzq.service.GoNoticeService;
import com.tfzq.service.GoUserGroupService;
import com.tfzq.util.PbFileUtils;

/**
 * 通知控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "通知管理", description = "通知管理")
@RequestMapping(value = "pr/goNotice", method = RequestMethod.POST)
public class GoNoticeController extends BaseController {
	@Autowired
	private GoNoticeService goNoticeService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private GoGroupService goGroupService;
	@Autowired
	private GoUserGroupService goUserGroupService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private GoInformService goInformService;
	@Autowired
	private GoInformUserService goInformUserService;

	// 查询通知
	@ApiOperation(value = "查询通知")
	// @RequiresPermissions("pr.goNotice.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoNoticeBean> list = goNoticeService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> notice_typeMap = sysDicService.queryDicByDicIndexKey("NOTICE_TYPE");
		addDictFromMap(dicts, "NOTICE_TYPE", notice_typeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		for (GoNoticeBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getNoticeType())) {
				singleBean.setNoticeTypeText(notice_typeMap.get(singleBean.getNoticeType().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsInform())) {
				singleBean.setIsInformText((yes_noMap.get(singleBean.getIsInform().toString())));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	@RequestMapping(value = "/read/listCatalog")
	public Object getCatalog(HttpServletRequest request, ModelMap modelMap) {

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> notice_typeMap = sysDicService.queryDicByDicIndexKey("NOTICE_TYPE");
		addDictFromMap(dicts, "NOTICE_TYPE", notice_typeMap);

		return setSuccessModelMap(modelMap, null, dicts);
	}

	// 详细信息
	@ApiOperation(value = "通知详情")
	// @RequiresPermissions("pr.goNotice.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoNoticeBean goNoticeBean = new GoNoticeBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoNotice record = goNoticeService.queryById(id);
			try {
				BeanUtils.copyProperties(goNoticeBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goNoticeBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> notice_typeMap = sysDicService.queryDicByDicIndexKey("NOTICE_TYPE");
		addDictFromMap(dicts, "NOTICE_TYPE", notice_typeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		if (goNoticeBean != null) {
			if (StringUtils.isNotEmpty(goNoticeBean.getNoticeType())) {
				goNoticeBean.setNoticeTypeText(notice_typeMap.get(goNoticeBean.getNoticeType().toString()));
			}
			if (StringUtils.isNotEmpty(goNoticeBean.getIsInform())) {
				goNoticeBean.setIsInformText((yes_noMap.get(goNoticeBean.getIsInform().toString())));
			}
		}

		return setSuccessModelMap(modelMap, goNoticeBean, dicts);
	}

	// 新增通知
	@ApiOperation(value = "添加通知")
	// @RequiresPermissions("pr.goNotice.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoNotice s_notice = Request2ModelUtil.covert(GoNotice.class, request);

		GoNotice notice = goNoticeService.add(s_notice);

		if (notice.getIsInform().equals("YES")) {
			GoInform s_inform = new GoInform();
			s_inform.setInformTitle(notice.getNoticeTitle());
			s_inform.setInformContent(notice.getNoticeContent());
			s_inform.setSrcType("NOTICE");
			s_inform.setSrcId(notice.getId());
			s_inform.setIsReply("NO");
			s_inform.setInformGroup(notice.getInformGroup());

			GoInform inform = goInformService.add(s_inform);
			notice.setInformId(inform.getId());
			goNoticeService.update(notice);

			HashSet<String> userIDs = new HashSet<String>();
			// 发给分组的消息
			String informGroupId = request.getParameter("informGroupId");
			if (informGroupId != null && informGroupId.length() != 0) {
				String tmp[] = informGroupId.split(",");
				processGroupInfrom(tmp, userIDs);
			}

			for (String uid : userIDs) {
				if (uid.length() == 0) {
					continue;
				}
				SysUser user = sysUserService.queryById(uid);
				GoInformUser giu = new GoInformUser();
				giu.setInformId(inform.getId());
				giu.setUserId(uid);
				giu.setUserName(user.getUserName());
				giu.setIsRead("NO");
				giu.setIsReplied("NO");

				goInformUserService.add(giu);
			}
		}

		return setSuccessModelMap(modelMap);
	}

	private void processGroupInfrom(String[] groups, HashSet<String> userIDs) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		for (String s : groups) {
			if (s.length() == 0) {
				continue;
			}
			GoGroup group = goGroupService.queryById(s);
			if (group.getGroupType().equals("DEPARTMENT_GROUP")) {// 部门分组
				params.put("rootNode", group.getDepartmentId());
				userIDs.addAll(sysUserService.queryDepUsers(params));
			} else {// 自定义分组
				params.put("groupId", s);
				List<GoUserGroupBean> us = goUserGroupService.queryByCondition(params);

				for (GoUserGroupBean g : us) {
					userIDs.add(g.getUserId());
				}

			}

		}
	}

	// 修改通知
	@ApiOperation(value = "修改通知")
	// @RequiresPermissions("pr.goNotice.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoNotice record = Request2ModelUtil.covert(GoNotice.class, request);
		goNoticeService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除通知
	@ApiOperation(value = "删除通知")
	// @RequiresPermissions("pr.goNotice.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoNotice record = goNoticeService.queryById(id);
		if (record != null) {
			goNoticeService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
