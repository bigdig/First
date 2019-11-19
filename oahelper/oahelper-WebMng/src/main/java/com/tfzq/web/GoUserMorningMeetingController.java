package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.UnauthorizedException;
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
import com.tfzq.pr.model.generator.GoUserMorningMeeting;
import com.tfzq.pr.model.generator.MyVote;
import com.tfzq.pr.model.pr.GoUserMorningMeetingBean;
import com.tfzq.service.GoUserMorningMeetingService;
import com.tfzq.util.PbFileUtils;

/**
 * 晨会参加控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "晨会参加管理", description = "晨会参加管理")
@RequestMapping(value = "pr/goUserMorningMeeting", method = RequestMethod.POST)
public class GoUserMorningMeetingController extends BaseController {
	@Autowired
	private GoUserMorningMeetingService goUserMorningMeetingService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;

	// 查询晨会参加
	@ApiOperation(value = "查询晨会参加")
	// @RequiresPermissions("pr.goUserMorningMeeting.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoUserMorningMeetingBean> list = goUserMorningMeetingService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> meeting_wayMap = sysDicService.queryDicByDicIndexKey("MEETING_WAY");
		addDictFromMap(dicts, "MEETING_WAY", meeting_wayMap);
		Map<String, String> present_typeMap = sysDicService.queryDicByDicIndexKey("PRESENT_WAY");
		addDictFromMap(dicts, "PRESENT_WAY", present_typeMap);
		Map<String, String> absence_wayMap = sysDicService.queryDicByDicIndexKey("ABSENCE_WAY");
		addDictFromMap(dicts, "ABSENCE_WAY", absence_wayMap);
		for (GoUserMorningMeetingBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getAttendenceWay())) {
				singleBean.setAttendenceWayText(meeting_wayMap.get(singleBean.getAttendenceWay().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getPresentWay())) {
				singleBean.setPresentWayText(present_typeMap.get(singleBean.getPresentWay().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getAbsenceWay())) {
				singleBean.setAbsenceWayText(absence_wayMap.get(singleBean.getAbsenceWay().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "晨会参加详情")
	// @RequiresPermissions("pr.goUserMorningMeeting.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoUserMorningMeetingBean goUserMorningMeetingBean = new GoUserMorningMeetingBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoUserMorningMeeting record = goUserMorningMeetingService.queryById(id);
			try {
				BeanUtils.copyProperties(goUserMorningMeetingBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goUserMorningMeetingBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> meeting_wayMap = sysDicService.queryDicByDicIndexKey("MEETING_WAY");
		addDictFromMap(dicts, "MEETING_WAY", meeting_wayMap);
		Map<String, String> present_typeMap = sysDicService.queryDicByDicIndexKey("PRESENT_WAY");
		addDictFromMap(dicts, "PRESENT_WAY", present_typeMap);
		Map<String, String> absence_wayMap = sysDicService.queryDicByDicIndexKey("ABSENCE_WAY");
		addDictFromMap(dicts, "ABSENCE_WAY", absence_wayMap);
		if (goUserMorningMeetingBean != null) {
			if (StringUtils.isNotEmpty(goUserMorningMeetingBean.getAttendenceWay())) {
				goUserMorningMeetingBean.setAttendenceWayText(
						meeting_wayMap.get(goUserMorningMeetingBean.getAttendenceWay().toString()));
			}
			if (StringUtils.isNotEmpty(goUserMorningMeetingBean.getPresentWay())) {
				goUserMorningMeetingBean
						.setPresentWayText(present_typeMap.get(goUserMorningMeetingBean.getPresentWay().toString()));
			}
			if (StringUtils.isNotEmpty(goUserMorningMeetingBean.getAbsenceWay())) {
				goUserMorningMeetingBean
						.setAbsenceWayText(absence_wayMap.get(goUserMorningMeetingBean.getAbsenceWay().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goUserMorningMeetingBean, dicts);
	}

	// 新增晨会参加
	@ApiOperation(value = "添加晨会参加")
	// @RequiresPermissions("pr.goUserMorningMeeting.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoUserMorningMeeting record = Request2ModelUtil.covert(GoUserMorningMeeting.class, request);
		goUserMorningMeetingService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 新增晨会参加
	@ApiOperation(value = "参加晨会")
	// @RequiresPermissions("pr.goUserMorningMeeting.add")
	@RequestMapping(value = "/attend", method = RequestMethod.POST)
	public Object userAttend(HttpServletRequest request, ModelMap modelMap) {
		String userId = WebUtil.getCurrentUser();

		SysUser user = sysUserService.queryById(userId);
		GoUserMorningMeeting record = Request2ModelUtil.covert(GoUserMorningMeeting.class, request);
		record.setUserId(userId);
		record.setUserName(user.getUserName());
		goUserMorningMeetingService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改晨会参加
	@ApiOperation(value = "修改晨会参加")
	// @RequiresPermissions("pr.goUserMorningMeeting.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoUserMorningMeeting record = Request2ModelUtil.covert(GoUserMorningMeeting.class, request);
		goUserMorningMeetingService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除晨会参加
	@ApiOperation(value = "删除晨会参加")
	// @RequiresPermissions("pr.goUserMorningMeeting.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoUserMorningMeeting record = goUserMorningMeetingService.queryById(id);
		if (record != null) {
			goUserMorningMeetingService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ApiOperation(value = "导出晨会参加详情")
	public Object export(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		List<GoUserMorningMeetingBean> umm = goUserMorningMeetingService.queryByCondition(params);

		if (umm.size() <= 0) {
			return null;
		}

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> meeting_wayMap = sysDicService.queryDicByDicIndexKey("MEETING_WAY");
		addDictFromMap(dicts, "MEETING_WAY", meeting_wayMap);
		Map<String, String> present_typeMap = sysDicService.queryDicByDicIndexKey("PRESENT_WAY");
		addDictFromMap(dicts, "PRESENT_WAY", present_typeMap);
		Map<String, String> absence_wayMap = sysDicService.queryDicByDicIndexKey("ABSENCE_WAY");
		addDictFromMap(dicts, "ABSENCE_WAY", absence_wayMap);

		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("晨会情况表");
			HSSFRow row = sheet.createRow((short) (0));
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("晨会日期");
			cell = row.createCell(1);
			cell.setCellValue("参会人");
			cell = row.createCell(2);
			cell.setCellValue("出席方式");
			cell = row.createCell(3);
			cell.setCellValue("现场参加晨会地点");
			cell = row.createCell(4);
			cell.setCellValue("请假原因");

			int i = 0;
			for (GoUserMorningMeetingBean singleBean : umm) {
				i++;
				if (StringUtils.isNotEmpty(singleBean.getAttendenceWay())) {
					singleBean.setAttendenceWayText(meeting_wayMap.get(singleBean.getAttendenceWay().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getPresentWay())) {
					singleBean.setPresentWayText(present_typeMap.get(singleBean.getPresentWay().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getAbsenceWay())) {
					singleBean.setAbsenceWayText(absence_wayMap.get(singleBean.getAbsenceWay().toString()));
				}

				row = sheet.createRow((short) (i));
				cell = row.createCell(0);
				if (!"".equals(singleBean.getMeetingDate()) && singleBean.getMeetingDate() != null) {
					cell.setCellValue(singleBean.getMeetingDate());
				}

				cell = row.createCell(1);
				if (!"".equals(singleBean.getUserName()) && singleBean.getUserName() != null) {
					cell.setCellValue(singleBean.getUserName());
				}

				cell = row.createCell(2);
				if (!"".equals(singleBean.getAttendenceWayText()) && singleBean.getAttendenceWayText() != null) {
					cell.setCellValue(singleBean.getAttendenceWayText());
				}

				cell = row.createCell(3);
				if (!"".equals(singleBean.getPresentWayText()) && singleBean.getPresentWayText() != null) {
					cell.setCellValue(singleBean.getPresentWayText());
				}

				cell = row.createCell(4);
				if (!"".equals(singleBean.getAbsenceWayText()) && singleBean.getAbsenceWayText() != null) {
					cell.setCellValue(singleBean.getAbsenceWayText());
				}
			}

			// response.setContentType("application/octet-stream");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=meeting_detail.xls");

			OutputStream os = response.getOutputStream();

			wb.write(os);
			wb.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return null;

	}
}
