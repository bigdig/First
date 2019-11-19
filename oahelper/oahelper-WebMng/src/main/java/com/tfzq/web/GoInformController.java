package com.tfzq.web;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoGroup;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.generator.MyInform;
import com.tfzq.pr.model.pr.GoInformBean;
import com.tfzq.pr.model.pr.GoUserGroupBean;
import com.tfzq.service.GoGroupService;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;
import com.tfzq.service.GoUserGroupService;
import com.tfzq.service.MyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 通知消息控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "通知消息管理", description = "通知消息管理")
@RequestMapping(value = "pr/goInform", method = RequestMethod.POST)
public class GoInformController extends BaseController {
	@Autowired
	private GoInformService goInformService;
	@Autowired
	private GoInformUserService goInformUserService;
	@Autowired
	private GoGroupService goGroupService;
	@Autowired
	private GoUserGroupService goUserGroupService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private MyService myService;

	// 查询通知消息
	@ApiOperation(value = "查询通知消息")
	// @RequiresPermissions("pr.goInform.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoInformBean> list = goInformService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> inform_typeMap = sysDicService.queryDicByDicIndexKey("INFORM_TYPE");
		addDictFromMap(dicts, "INFORM_TYPE", inform_typeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		for (GoInformBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getSrcType())) {
				singleBean.setSrcTypeText(inform_typeMap.get(singleBean.getSrcType().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsReply())) {
				singleBean.setIsReplyText(yes_noMap.get(singleBean.getIsReply().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 查询通知消息
	@ApiOperation(value = "条件查询用户")
	// @RequiresPermissions("pr.goInform.read")
	@RequestMapping(value = "/selectUserByCondition")
	public Object selectUserByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		// params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		List<SysUser> list = sysUserService.queryByCondition(params);

		return setSuccessModelMap(modelMap, list);
	}

	// 查询通知消息
	@ApiOperation(value = "导出消息")
	// @RequiresPermissions("pr.goInform.read")
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		// params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		List<MyInform> informs = myService.selectInformByCondition(params);
		Map<String, String> inform_typeMap = sysDicService.queryDicByDicIndexKey("INFORM_TYPE");
		// 添加字典翻译
		Map<String, String> isRepliedMap = sysDicService.queryDicByDicIndexKey("ISREPLIED");

		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("投票情况");
			HSSFRow row = sheet.createRow((short) (0));
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("消息标题");
			cell = row.createCell(1);
			cell.setCellValue("消息类型");
			cell = row.createCell(2);
			cell.setCellValue("接收人");
			cell = row.createCell(3);
			cell.setCellValue("是否已回应");
			cell = row.createCell(4);
			cell.setCellValue("用户反馈");
			cell = row.createCell(5);
			cell.setCellValue("外部链接");
			cell = row.createCell(6);
			cell.setCellValue("发送时间");
			cell = row.createCell(7);
			cell.setCellValue("消息内容");

			for (int i = 0; i < informs.size(); i++) {
				MyInform inform = informs.get(i);
				inform.setSrcType(inform_typeMap.get(inform.getSrcType().toString()));
				row = sheet.createRow((short) (i + 1));
				cell = row.createCell(0);
				if (!"".equals(inform.getInformTitle()) && inform.getInformTitle() != null) {
					cell.setCellValue(inform.getInformTitle());
				}
				cell = row.createCell(1);
				if (!"".equals(inform.getSrcType()) && inform.getSrcType() != null) {
					cell.setCellValue(inform.getSrcType());
				}
				cell = row.createCell(2);
				if (!"".equals(inform.getUserName()) && inform.getUserName() != null) {
					cell.setCellValue(inform.getUserName());
				}
				cell = row.createCell(3);
				if (!"".equals(inform.getIsReplied()) && inform.getIsReplied() != null) {
					cell.setCellValue(isRepliedMap.get(inform.getIsReplied()));
				}
				cell = row.createCell(4);
				if (!"".equals(inform.getReplyRemark()) && inform.getReplyRemark() != null) {
					cell.setCellValue(inform.getReplyRemark());
				}
				cell = row.createCell(5);
				if (!"".equals(inform.getExternalLink()) && inform.getExternalLink() != null) {
					cell.setCellValue(inform.getExternalLink());
				}
				cell = row.createCell(6);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cell.setCellValue(df.format(inform.getCreateTime()));

				cell = row.createCell(7);
				if (!"".equals(inform.getInformContent()) && inform.getInformContent() != null) {
					cell.setCellValue(inform.getInformContent());
				}
			}
			// response.setContentType("application/octet-stream");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=inform_detail.xls");

			OutputStream os = response.getOutputStream();

			wb.write(os);
			wb.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	// 详细信息
	@ApiOperation(value = "通知消息详情")
	// @RequiresPermissions("pr.goInform.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoInformBean goInformBean = new GoInformBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoInform record = goInformService.queryById(id);
			try {
				BeanUtils.copyProperties(goInformBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goInformBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> inform_typeMap = sysDicService.queryDicByDicIndexKey("INFORM_TYPE");
		addDictFromMap(dicts, "INFORM_TYPE", inform_typeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		if (goInformBean != null) {
			if (StringUtils.isNotEmpty(goInformBean.getSrcType())) {
				goInformBean.setSrcTypeText(inform_typeMap.get(goInformBean.getSrcType().toString()));
			}
			if (StringUtils.isNotEmpty(goInformBean.getIsReply())) {
				goInformBean.setIsReplyText(yes_noMap.get(goInformBean.getIsReply().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goInformBean, dicts);
	}

	// 新增通知消息
	@ApiOperation(value = "添加通知消息")
	// @RequiresPermissions("pr.goInform.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoInform record = Request2ModelUtil.covert(GoInform.class, request);
		record.setSrcType("INFO");
		GoInform r = goInformService.add(record);
		// 发给个人的消息
		HashSet<String> userIDs = new HashSet<String>();
		String users = request.getParameter("users");
		if (users != null && users.length() != 0) {
			String tmp[] = users.split(",");
			for (String s : tmp) {
				userIDs.add(s);
			}
		}

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
			giu.setInformId(r.getId());
			giu.setUserId(uid);
			giu.setUserName(user.getUserName());
			giu.setIsRead("NO");
			giu.setIsReplied("NO");

			goInformUserService.add(giu);
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

	// 修改通知消息
	@ApiOperation(value = "修改通知消息")
	// @RequiresPermissions("pr.goInform.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoInform record = Request2ModelUtil.covert(GoInform.class, request);
		goInformService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除通知消息
	@ApiOperation(value = "删除通知消息")
	// @RequiresPermissions("pr.goInform.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoInform record = goInformService.queryById(id);
		if (record != null) {
			goInformService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	// 再次发送通知消息
	@ApiOperation(value = "再次发送通知消息")
	@RequestMapping(value = "/remind", method = RequestMethod.POST)
	public Object remind(ModelMap modelMap, @RequestParam(value = "id", required = true) String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("informId", id);
		goInformUserService.updateByInformId(params);

		return setSuccessModelMap(modelMap);
	}
}
