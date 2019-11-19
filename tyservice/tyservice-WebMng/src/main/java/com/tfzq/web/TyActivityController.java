package com.tfzq.web;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyActivityexpertService;
import com.tfzq.service.TyActivitylistedcompService;
import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyActivitystaffService;
import com.tfzq.service.TyActivitystockService;
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyDcrecostockService;
import com.tfzq.service.TyExpertService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyListedcompanyService;
import com.tfzq.service.TyMessagelogService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivityexpert;
import com.tfzq.ty.model.generator.TyActivitylistedcomp;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.generator.TyActivitystock;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
import com.tfzq.ty.model.ty.TyMessagelogBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.excel.WriteExcelUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 活动控制类--通用活动控制
 * 
 * @author pengtao
 */
@RestController
@Api(value = "活动管理", description = "活动管理")
@RequestMapping(value = "ty/tyActivity", method = RequestMethod.POST)
public class TyActivityController extends BaseController {
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyActivitystaffService tyActivitystaffService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private TyDcrecostockService tyDcrecostockService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private TyMessagelogService tyMessagelogService;
	@Autowired
	private TyListedcompanyService tyListedcompanyService;
	@Autowired
	private TyActivitylistedcompService tyActivitylistedcompService;
	@Autowired
	private TyExpertService tyExpertService;
	@Autowired
	private TyActivityexpertService tyActivityexpertService;

	@Autowired
	private TyActivitystockService tyActivitystockService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private TyLabelService tyLabelService;

	// 我的活动
	@ApiOperation(value = "我的活动")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/myActivityList")
	@Deprecated
	public Object myActivityList(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userId", required = false) String userId) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("createById", userId);
		if (params.get("orderBy") == null || StringUtils.isBlank(params.get("orderBy").toString())) {
			params.put("orderBy", "id desc");
		}
		params.put("deleteFlag", "0");
		params.put("subActFlag","0"); //顶级活动
		List<TyActivityBean> list = tyActivityService.queryByCondition(params);
		Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
		if (list.size() > 0) {
			for (TyActivityBean singleBean : list) {
				if (StringUtils.isNotEmpty(singleBean.getActivityType())) {
					singleBean.setActivityTypeText(activitytypeMap.get(singleBean.getActivityType().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list);
	}

	// 我参与的活动
	@ApiOperation(value = "我参与的活动")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/myActivitySignList")
	@Deprecated
	public Object myActivitySignList(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "signId", required = false) String signId) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		// 添加字典翻译
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		params.put("signId", signId);
		List<TyActivitysignBean> list = tyActivitysignService.queryByCondition(params);
		Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
		addDictFromMap(dicts, "ACTIVITYTYPE", activitytypeMap);
		if (list.size() > 0) {
			for (TyActivitysignBean singleBean : list) {
				if (StringUtils.isNotEmpty(singleBean.getActivityId())) {
					TyActivity record = tyActivityService.queryById(singleBean.getActivityId());
					TyStafflist staff = tyStafflistService.queryById(record.getCreateBy());
					singleBean.setPriorityLevel(record.getPriorityLevel());
					singleBean.setActCreatePosi(staff != null ? staff.getPosition() : "");
					singleBean.setActCreateName(record.getCreateName());
					singleBean.setTitle(record.getTitle());
					singleBean.setPerLimit(record.getPerLimit());
					if (StringUtils.isNotEmpty(record.getActivityType())) {
						singleBean.setActivityTypeText(activitytypeMap.get(record.getActivityType().toString()));
					}
				}

			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 活跃客户
	@ApiOperation(value = "活跃客户")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/activeCustomer")
	public Object activeCustomer(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userId", required = false) String userId) {
		long timeStamp = toTimeStamp();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleteFlag", "0");
		params.put("pageSize", 1000);
		params.put("salerId", userId);
		params.put("activeDatetime", timeStamp);
		List<TyOrgcustomerBean> list = tyOrgcustomerService.queryCustActive(params);

		return setSuccessModelMap(modelMap, list);
	}

	private long toTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -100);
		Date finalDate = calendar.getTime();
		long res = Long.parseLong(sdf.format(finalDate));
		return res;
	}

	// 查询活动
	@ApiOperation(value = "查询活动")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "activityType[]", required = false) List<String> activityTypes) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		
		if(!CollectionUtils.isEmpty(activityTypes)) {
			params.put("activityTypes", activityTypes);
//			params.put("createBy", WebUtil.getCurrentUser());  //限制只查看自己的个人服务记录
		}
		
		Map<String, Object> data = new HashMap<>();
		params.put("subActFlag","0"); //顶级活动
		params.put("deleteFlag", "0");
		if (params.get("orderBy") == null || StringUtils.isBlank(params.get("orderBy").toString())) {
			params.put("orderBy", "begin_date desc ,update_time desc,id desc");
		}
		PageInfo<TyActivityBean> list = tyActivityService.queryBeans(params);

		// 添加字典翻译
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<TyStafflist> researcherList = tyStafflistService.getResearcherRecords();
		addDictFromModel(dicts, "Researchers", researcherList, "id", "staffName");
		for (TyStafflist researcher : researcherList) {
			researcher.setStaffName(researcher.getStaffName() + "(" + researcher.getDeptName() + ")");
		}
		addDictFromModel(dicts, "ResearchersEx", researcherList, "id", "staffName");

		List<SysDept> sysDepts = sysDeptService.getResearchRecords();
		addDictFromModel(dicts, "Researchdept", sysDepts, "id", "deptName");
		Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
		addDictFromMap(dicts, "ACTIVITYTYPE", activitytypeMap);
		Map<String, String> prioritylevelMap = sysDicService.queryDicByDicIndexKey("PRIORITYLEVEL");
		addDictFromMap(dicts, "PRIORITYLEVEL", prioritylevelMap);
		Map<String, String> activitystatusMap = sysDicService.queryDicByDicIndexKey("ACTIVITYSTATUS");
		addDictFromMap(dicts, "ACTIVITYSTATUS", activitystatusMap);
		Map<String, String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
		addDictFromMap(dicts, "TOPICTYPE", topictypeMap);

		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
		Map<String, String> recommendTypeMap = sysDicService.queryDicByDicIndexKey("RECOMMENDTYPE");
		addDictFromMap(dicts, "recommendTypes", recommendTypeMap);

		if (list.getList().size() > 0) {
			for (TyActivityBean singleBean : list.getList()) {
				String actId = singleBean.getId();
				List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
				singleBean.setStaffList(staffList);

				List<TyActivitysignBean> custList = tyActivitysignService.queryByActId(actId);
				singleBean.setCustList(custList);
				// 股票list
				List<TyDcrecostockBean> stockList = tyDcrecostockService.queryBySpeakId(actId);
				if (stockList != null && stockList.size() > 0) {
					for (TyDcrecostockBean stockBean : stockList) {
						if (StringUtils.isNotEmpty(stockBean.getRecommendType())) {
							stockBean.setRecommendTypeName(
									recommendTypeMap.get(stockBean.getRecommendType().toString()));
						}
					}
					singleBean.setStockList(stockList);
				}
				if (StringUtils.isNotEmpty(singleBean.getActivityType())) {
					singleBean.setActivityTypeText(activitytypeMap.get(singleBean.getActivityType().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getPriorityLevel())) {
					singleBean.setPriorityLevelText(prioritylevelMap.get(singleBean.getPriorityLevel().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getActivityStatus())) {
					singleBean.setActivityStatusText(activitystatusMap.get(singleBean.getActivityStatus().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getTopicType())) {
					singleBean.setTopicTypeText(topictypeMap.get(singleBean.getTopicType().toString()));
				}
				if (StringUtils.isNotBlank(singleBean.getCreateBy())) {
					SysUser creater = sysUserService.queryById(singleBean.getCreateBy());
					singleBean.setCreateAvatar(creater.getAvatar());
				}
				//查询当前沙龙的子活动
				if(StringUtils.isNotBlank(singleBean.getId())) {
					Map<String,Object>params1 = new HashMap<>();     
					params1.put("deleteFlag", "0");
					params1.put("parentActId", singleBean.getId());
					params1.put("activityType",Constants.SALON);
					params1.put("orderBy","begin_date desc,update_time desc,id desc");
					List<TyActivityBean> sublist = tyActivityService.queryByCondition(params1);
					singleBean.setSubActList(sublist);
				}
			}
		}

		return setSuccessModelMap(modelMap, list, dicts);
	}
	
	// 查询活动的子活动
	@ApiOperation(value = "查询当前活动的子活动")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/subActlist")
	public Object getsubActlist(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivity record = tyActivityService.queryById(id);
		if(record != null && StringUtils.equals(record.getSubActFlag(), "0")){
			TyActivityBean tyActivityBean = new TyActivityBean();
			SysUser user = sysUserService.queryById(record.getCreateBy());
			try {
				BeanUtils.copyProperties(tyActivityBean, record);
				if (user != null) {
					tyActivityBean.setCreateAvatar(user.getAvatar() == null ? "" : user.getAvatar());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyActivityBean = null;
				e.printStackTrace();
			}
			//查找当前活动的子活动
			Map<String,Object> params = new HashMap<>();
			params.put("deleteFlag", "0");
			if (params.get("orderBy") == null || StringUtils.isBlank(params.get("orderBy").toString())) {
				params.put("orderBy", "update_time desc,begin_date desc");
			}
			params.put("parentActId",id);
			List<TyActivityBean> subActList = tyActivityService.queryByCondition(params);
			// 添加字典翻译
			Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
			List<TyStafflist> researcherList = tyStafflistService.getResearcherRecords();
			addDictFromModel(dicts, "Researchers", researcherList, "id", "staffName");
			for (TyStafflist researcher : researcherList) {
				researcher.setStaffName(researcher.getStaffName() + "(" + researcher.getDeptName() + ")");
			}
			addDictFromModel(dicts, "ResearchersEx", researcherList, "id", "staffName");
	
			List<SysDept> sysDepts = sysDeptService.getResearchRecords();
			addDictFromModel(dicts, "Researchdept", sysDepts, "id", "deptName");
			Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
			addDictFromMap(dicts, "ACTIVITYTYPE", activitytypeMap);
			Map<String, String> prioritylevelMap = sysDicService.queryDicByDicIndexKey("PRIORITYLEVEL");
			addDictFromMap(dicts, "PRIORITYLEVEL", prioritylevelMap);
			Map<String, String> activitystatusMap = sysDicService.queryDicByDicIndexKey("ACTIVITYSTATUS");
			addDictFromMap(dicts, "ACTIVITYSTATUS", activitystatusMap);
			Map<String, String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
			addDictFromMap(dicts, "TOPICTYPE", topictypeMap);
	
			List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
			addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
			Map<String, String> recommendTypeMap = sysDicService.queryDicByDicIndexKey("RECOMMENDTYPE");
			addDictFromMap(dicts, "recommendTypes", recommendTypeMap);
			if (tyActivityBean != null) {
				if (StringUtils.isNotEmpty(tyActivityBean.getActivityType())) {
					tyActivityBean.setActivityTypeText(activitytypeMap.get(tyActivityBean.getActivityType().toString()));
				}
				if (StringUtils.isNotEmpty(tyActivityBean.getPriorityLevel())) {
					tyActivityBean.setPriorityLevelText(prioritylevelMap.get(tyActivityBean.getPriorityLevel().toString()));
				}
				if (StringUtils.isNotEmpty(tyActivityBean.getActivityStatus())) {
					tyActivityBean
							.setActivityStatusText(activitystatusMap.get(tyActivityBean.getActivityStatus().toString()));
				}
				if (StringUtils.isNotEmpty(tyActivityBean.getTopicType())) {
					tyActivityBean.setTopicTypeText(topictypeMap.get(tyActivityBean.getTopicType().toString()));
				}
				if (StringUtils.isNotEmpty(tyActivityBean.getContactId())) {
					TyStafflist staff = tyStafflistService.queryById(tyActivityBean.getContactId());
					tyActivityBean.setContactStaff(staff);
				}
			}
			if (subActList.size() > 0) {
				for (TyActivityBean singleBean : subActList) {
					if (StringUtils.isNotEmpty(singleBean.getActivityType())) {
						singleBean.setActivityTypeText(activitytypeMap.get(singleBean.getActivityType().toString()));
					}
					if (StringUtils.isNotEmpty(singleBean.getPriorityLevel())) {
						singleBean.setPriorityLevelText(prioritylevelMap.get(singleBean.getPriorityLevel().toString()));
					}
					if (StringUtils.isNotEmpty(singleBean.getActivityStatus())) {
						singleBean.setActivityStatusText(activitystatusMap.get(singleBean.getActivityStatus().toString()));
					}
					if (StringUtils.isNotEmpty(singleBean.getTopicType())) {
						singleBean.setTopicTypeText(topictypeMap.get(singleBean.getTopicType().toString()));
					}
					if (StringUtils.isNotBlank(singleBean.getCreateBy())) {
						SysUser creater = sysUserService.queryById(singleBean.getCreateBy());
						singleBean.setCreateAvatar(creater.getAvatar());
					}
				}
			}
			tyActivityBean.setSubActList(subActList);
			return setSuccessModelMap(modelMap, tyActivityBean, dicts);
		}
		return setModelMap(modelMap, HttpCode.BAD_REQUEST);

	}

	// 详细信息（包含参与活动的人员）
	@ApiOperation(value = "活动详情")
	// @RequiresPermissions("ty.tyActivity.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		Map<String, Object> data = new HashMap<>();
		TyActivityBean tyActivityBean = new TyActivityBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyActivity record = tyActivityService.queryById(id);
			SysUser user = sysUserService.queryById(record.getCreateBy());
			try {
				BeanUtils.copyProperties(tyActivityBean, record);
				if (user != null) {
					tyActivityBean.setCreateAvatar(user.getAvatar() == null ? "" : user.getAvatar());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyActivityBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
		addDictFromMap(dicts, "ACTIVITYTYPE", activitytypeMap);
		Map<String, String> prioritylevelMap = sysDicService.queryDicByDicIndexKey("PRIORITYLEVEL");
		addDictFromMap(dicts, "PRIORITYLEVEL", prioritylevelMap);
		Map<String, String> activitystatusMap = sysDicService.queryDicByDicIndexKey("ACTIVITYSTATUS");
		addDictFromMap(dicts, "ACTIVITYSTATUS", activitystatusMap);
		Map<String, String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
		addDictFromMap(dicts, "TOPICTYPE", topictypeMap);
		Map<String, String> signStatusMap = sysDicService.queryDicByDicIndexKey("SIGNSTATUS");
		addDictFromMap(dicts, "SIGNSTATUS", signStatusMap);
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
		if (tyActivityBean != null) {
			if (StringUtils.isNotEmpty(tyActivityBean.getActivityType())) {
				tyActivityBean.setActivityTypeText(activitytypeMap.get(tyActivityBean.getActivityType().toString()));
			}
			if (StringUtils.isNotEmpty(tyActivityBean.getPriorityLevel())) {
				tyActivityBean.setPriorityLevelText(prioritylevelMap.get(tyActivityBean.getPriorityLevel().toString()));
			}
			if (StringUtils.isNotEmpty(tyActivityBean.getActivityStatus())) {
				tyActivityBean
						.setActivityStatusText(activitystatusMap.get(tyActivityBean.getActivityStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyActivityBean.getTopicType())) {
				tyActivityBean.setTopicTypeText(topictypeMap.get(tyActivityBean.getTopicType().toString()));
			}
			if (StringUtils.isNotEmpty(tyActivityBean.getContactId())) {
				TyStafflist staff = tyStafflistService.queryById(tyActivityBean.getContactId());
				tyActivityBean.setContactStaff(staff);
			}
			if (StringUtils.isNotEmpty(id)) {
				String actId = tyActivityBean.getId();
				List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
				tyActivityBean.setStaffList(staffList);
			}
			if (StringUtils.isNotEmpty(id)) {
				String actId = tyActivityBean.getId();
				List<TyExpert> expertList = tyExpertService.queryByActId(actId);
				tyActivityBean.setExpertList(expertList);
				;
			}
			if (StringUtils.isNotEmpty(id)) {
				String actId = tyActivityBean.getId();
				List<TyListedcompany> listedCompList = tyListedcompanyService.queryByActId(actId);
				tyActivityBean.setListedCompList(listedCompList);
			}
		}

		// 查询参会的人员
		if (StringUtils.isNotEmpty(id)) {
			List<TyActivitysignBean> activitysignList = tyActivitysignService.queryByActId(id);
			tyActivityBean.setJoinNum(activitysignList.size());
			if(activitysignList.size() > 0){
				for (TyActivitysignBean tyActivitysignBean : activitysignList) {
					tyActivitysignBean.setSignStatusText(signStatusMap.get(tyActivitysignBean.getSignStatus()));
				}
			}
			data.put("activitysignData", activitysignList);
		}
		if (StringUtils.isNotEmpty(id) && !StringUtils.equals(tyActivityBean.getActivityStatus(), "0")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activityCust", tyActivityBean.getId());
			List<TyMessagelogBean> messges = tyMessagelogService.queryByCondition(params);
			data.put("smsMessges", messges);
		} else {
			data.put("smsMessges", new ArrayList<TyMessagelogBean>());
		}

		data.put("tyActivityBean", tyActivityBean);
		return setSuccessModelMap(modelMap, data, dicts);
	}

	// 新增活动
	@ApiOperation(value = "添加活动")
	// @RequiresPermissions("ty.tyActivity.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds", required = false) List<String> userIds,
			@RequestParam(value = "userIds[]", required = false) List<String> userIdsFromPc,
			@RequestParam(value = "custIds", required = false) List<String> custIds,
			@RequestParam(value = "expertIds", required = false) List<String> expertIds,
			@RequestParam(value = "listedCompIds", required = false) List<String> listedCompIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		if (record.getTotalLimit() != null) {
			record.setPerLimit(record.getTotalLimit() + 2);
		}
		if ("0".equals(record.getActivityCate())) {
			record.setNotifyFlag("0");
		} else {
			record.setNotifyFlag("1");
		}
		record.setSubActFlag("0");
		record.setDataSrc("0");
		TyActivity newCon = tyActivityService.add(record);
		//将联系人添加到参与员工中
		if(userIds == null){
			userIds = new ArrayList<String>();
		}
		addContactstaffIntoActStaff(record,userIds);
		//为委托课题冗余orgId,pc端委托课题的客户来自同一机构，保存在活动表的orgId字段中，小程序保持一致
		saveOrgIdForWTKT(record,custIds);
		
		addActivitystaff(newCon.getId(), userIds,userIdsFromPc);
		addActivityExpert(newCon.getId(), expertIds);
		addActivityListedComps(newCon.getId(), listedCompIds);
		if (custIds != null && custIds.size() > 0) {
			if (newCon.getPerLimit() != null && custIds.size() < newCon.getPerLimit()) {
				addCustomerOfActivity(newCon, custIds);
				return setSuccessModelMap(modelMap);
			} else if (newCon.getPerLimit() == null) {
				addCustomerOfActivity(newCon, custIds);
				return setSuccessModelMap(modelMap);
			} else {
				throw new IllegalArgumentException("添加失败，请检查参数是否正常，添加的客户数量是否小于限定的人数");
			}
		} else {
			return setSuccessModelMap(modelMap);
		}
	}

	
	//为委托课题冗余orgId,pc端委托课题的客户来自同一机构，保存在活动表的orgId字段中，小程序保持一致
	private void saveOrgIdForWTKT(TyActivity record, List<String> custIds) {
		if(StringUtils.equals(record.getActivityType(), "2") && !CollectionUtils.isEmpty(custIds)) {
			TyOrgcustomer customer = tyOrgcustomerService.queryById(custIds.get(0)); //暂取第一个客户
			record.setOrgId(customer.getOrgId());
			tyActivityService.update(record);
		}
	}

	//添加联系人到员工列表
	private void addContactstaffIntoActStaff(TyActivity record, List<String> userIds) {
		//Java调用方法时传入对象引用（指针），但由于一开始传入的是null,被调用方法里面的变量做赋值后，无法改变调用方法里面的引用
		//		if(userIds == null){
		//			userIds = new ArrayList<String>();
		//		}
		Set<String> userIdSet = new HashSet<String>();
		userIdSet.addAll(userIds);
		if(StringUtils.equals(record.getActivityCate(), "0") && StringUtils.isNotBlank(record.getContactId())) {
			userIdSet.add(record.getContactId());
		}else if(StringUtils.equals(record.getActivityCate(), "1")) {    
			userIdSet.add(WebUtil.getCurrentUser());   //个人活动添加自己为研究员
		}
		userIds.clear();
		userIds.addAll(userIdSet);
	}

	private void addActivityListedComps(String actId, List<String> listedCompIds) {
		if (listedCompIds != null && listedCompIds.size() > 0) {
			for (String listedCompId : listedCompIds) {
				TyActivitylistedcomp tyActivitylistedcomp = new TyActivitylistedcomp();
				tyActivitylistedcomp.setActivityId(actId);
				TyListedcompany listedComp = tyListedcompanyService.queryById(listedCompId);
				if (listedComp != null) {
					tyActivitylistedcomp.setListedcompId(listedComp.getId());
					tyActivitylistedcomp.setStockCode(listedComp.getStockCode());
					tyActivitylistedcomp.setStockName(listedComp.getStockName());
				}
				tyActivitylistedcompService.add(tyActivitylistedcomp);
			}
		}
	}

	private void addActivityExpert(String actId, List<String> expertIds) {
		if (expertIds != null && expertIds.size() > 0) {
			for (String expertId : expertIds) {
				TyActivityexpert tyActivityexpert = new TyActivityexpert();
				tyActivityexpert.setActivityId(actId);
				TyExpert tyExpert = tyExpertService.queryById(expertId);
				if (tyExpert != null) {
					tyActivityexpert.setExpertId(tyExpert.getId());
					tyActivityexpert.setExpertName(tyExpert.getExpertName());
					tyActivityexpert.setOrgId(tyExpert.getOrgId());
				}
				tyActivityexpertService.add(tyActivityexpert);
			}
		}
	}

	private void addCustomerOfActivity(TyActivity activity, List<String> custIds) {
		if (custIds != null && custIds.size() > 0) {
			String staffId = WebUtil.getCurrentUser();
			TyStafflist staff = tyStafflistService.queryById(staffId);
			TyActivitysign actSign = null;
			Date c = new Date();
			List<TyActivitysignBean> signs = tyActivitysignService.queryByActId(activity.getId());
			Map<String, TyActivitysignBean> signMap = toSignMap(signs);
			for (String custId : custIds) {
				if (signMap.containsKey(custId)) {
					continue;
				}
				TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
				if (customer != null) {
					actSign = new TyActivitysign();
					actSign.setActivityId(activity.getId());
					actSign.setCustName(customer.getCustName() == null ? "" : customer.getCustName());
					actSign.setOrgName(customer.getOrgName() == null ? "" : customer.getOrgName());
					actSign.setOrgSimpleName(customer.getOrgName() == null ? "" : customer.getOrgName());
					actSign.setCustId(customer.getId());
					actSign.setOrgId(customer.getOrgId());
					actSign.setCustMobile(customer.getCustMobile() == null ? "" : customer.getCustMobile());
					actSign.setInWhitelist(""); // 是否是白名单客户
					actSign.setSignId(staff.getId());
					actSign.setSignName(staff.getStaffName());
					actSign.setSignStatus("0");
					actSign.setSignDate(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.YYYYMMDD)));
					actSign.setSignTime(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.HHMMSS)));
					actSign.setDeptId(staff.getDeptId());
					tyActivitysignService.add(actSign);
				}
			}
		}

	}

	private Map<String, TyActivitysignBean> toSignMap(List<TyActivitysignBean> signs) {
		Map<String, TyActivitysignBean> results = new HashMap<String, TyActivitysignBean>();
		for (TyActivitysignBean sign : signs) {
			results.put(sign.getCustId(), sign);
		}
		return results;
	}

	/**
	 * 添加参加活动的员工信息
	 * 
	 * @param actId
	 * @param userIds
	 * @param userIdsFromPc   pc端和小程序端对象序列化键值不一致
	 */
	private void addActivitystaff(String actId, List<String> userIds, List<String> userIdsFromPc) {
		if (userIds != null && userIds.size() > 0) {
			for (String userId : userIds) {
				TyActivitystaff act = new TyActivitystaff();
				act.setActivityId(actId);
				TyStafflist staff = tyStafflistService.queryById(userId);
				if (staff != null) {
					act.setStaffId(staff.getId());
					act.setStaffName(staff.getStaffName());
					act.setDeptId(staff.getDeptId());
				}
				tyActivitystaffService.add(act);
			}
		}
		if (userIdsFromPc != null && userIdsFromPc.size() > 0) {
			for (String userId : userIdsFromPc) {
				TyActivitystaff act = new TyActivitystaff();
				act.setActivityId(actId);
				TyStafflist staff = tyStafflistService.queryById(userId);
				if (staff != null) {
					act.setStaffId(staff.getId());
					act.setStaffName(staff.getStaffName());
					act.setDeptId(staff.getDeptId());
				}
				tyActivitystaffService.add(act);
			}
		}
	}

	// 修改活动
	@ApiOperation(value = "修改活动")
	// @RequiresPermissions("ty.tyActivity.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds", required = false) List<String> userIds,
			@RequestParam(value = "userIds[]", required = false) List<String> userIdsFromPc,
			@RequestParam(value = "custIds", required = false) List<String> custIds,
			@RequestParam(value = "expertIds", required = false) List<String> expertIds,
			@RequestParam(value = "listedCompIds", required = false) List<String> listedCompIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		if(record.getTotalLimit() != null && record.getTotalLimit() > 0) {
			record.setPerLimit(record.getTotalLimit()+2);
		}
		TyActivity oldRecord = tyActivityService.queryById(record.getId());
		//将前台的数据复制到原数据上来
		fillValueToOldRecord(oldRecord,record);
		//取新纪录中的人数限制值
		Integer perLimit = oldRecord.getPerLimit();
//		if (record.getPerLimit() == null) {
//			perLimit = oldCon.getPerLimit();
//		}
		if (custIds != null && custIds.size() > 0) {
			List<TyActivitysignBean> list2 = tyActivitysignService.queryByActId(oldRecord.getId());
			if (list2 != null && perLimit != null && list2.size() > perLimit - custIds.size()) {
				throw new BusinessException("活动剩余人数不足！");
			}
		}
		TyActivityBean tyDcsalonBean = new TyActivityBean();
		tyActivityService.update(oldRecord);
		//TyActivity newCon = tyActivityService.queryById(oldRecord.getId());
		try {
			BeanUtils.copyProperties(tyDcsalonBean, oldRecord);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			tyDcsalonBean = null;
			e.printStackTrace();
		}
		//将联系人添加到参与员工中
		if(userIds == null){
			userIds = new ArrayList<String>();
		}
		addContactstaffIntoActStaff(record,userIds);

		if (!CollectionUtils.isEmpty(userIds) || !CollectionUtils.isEmpty(userIdsFromPc)) {
			tyActivitystaffService.deleteByActId(oldRecord.getId());
			addActivitystaff(oldRecord.getId(), userIds,userIdsFromPc);
		}
		if (expertIds != null && expertIds.size() > 0) {
			tyActivityexpertService.deleteByActId(oldRecord.getId());
			addActivityExpert(oldRecord.getId(), expertIds);
		}
		if (listedCompIds != null && listedCompIds.size() > 0) {
			tyActivitylistedcompService.deleteByActId(oldRecord.getId());
			addActivityListedComps(oldRecord.getId(), listedCompIds);
		}

		// 添加客户
		if (custIds != null && custIds.size() > 0) {
			addCustomerOfActivity(oldRecord, custIds);
		}

		String actId = oldRecord.getId();
		List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
		tyDcsalonBean.setStaffList(staffList);

		return setSuccessModelMap(modelMap, tyDcsalonBean);
	}
	
	private void fillValueToOldRecord(TyActivity oldRecord,TyActivity record){
		oldRecord.setTitle(record.getTitle()==null?oldRecord.getTitle():record.getTitle());
		oldRecord.setContent(record.getContent()==null?oldRecord.getContent():record.getContent());
		oldRecord.setBeginDate(record.getBeginDate()==null?oldRecord.getBeginDate():record.getBeginDate());
		oldRecord.setBeginTime(record.getBeginTime()==null?oldRecord.getBeginTime():record.getBeginTime());
		oldRecord.setEndDate(record.getEndDate()==null?oldRecord.getEndDate():record.getEndDate());
		oldRecord.setEndTime(record.getEndTime()==null?oldRecord.getEndTime():record.getEndTime());
		oldRecord.setTotalDuration(record.getTotalDuration()==null?oldRecord.getTotalDuration():record.getTotalDuration());
		oldRecord.setAvgDuration(record.getAvgDuration()==null?oldRecord.getAvgDuration():record.getAvgDuration());
		oldRecord.setActivityType(record.getActivityType()==null?oldRecord.getActivityType():record.getActivityType());
		oldRecord.setPriorityLevel(record.getPriorityLevel()==null?oldRecord.getPriorityLevel():record.getPriorityLevel());
		oldRecord.setTotalLimit(record.getTotalLimit()==null?oldRecord.getTotalLimit():record.getTotalLimit());
		oldRecord.setPerLimit(record.getPerLimit()==null?oldRecord.getPerLimit():record.getPerLimit());
		oldRecord.setLocale(record.getLocale()==null?oldRecord.getLocale():record.getLocale());
		oldRecord.setLabels(record.getLabels()==null?oldRecord.getLabels():record.getLabels());
		oldRecord.setActivityStatus(record.getActivityStatus()==null?oldRecord.getActivityStatus():record.getActivityStatus());
		oldRecord.setTopicType(record.getTopicType()==null?oldRecord.getTopicType():record.getTopicType());
		oldRecord.setAttachs(record.getAttachs()==null?oldRecord.getAttachs():record.getAttachs());
		oldRecord.setAttachsName(record.getAttachsName()==null?oldRecord.getAttachsName():record.getAttachsName());
		oldRecord.setDataSrc(record.getDataSrc()==null?oldRecord.getDataSrc():record.getDataSrc());
		oldRecord.setDataSrcname(record.getDataSrcname()==null?oldRecord.getDataSrcname():record.getDataSrcname());
		oldRecord.setOrgId(record.getOrgId()==null?oldRecord.getOrgId():record.getOrgId());
		oldRecord.setCreateName(record.getCreateName()==null?oldRecord.getCreateName():record.getCreateName());
		oldRecord.setContactId(record.getContactId()==null?oldRecord.getContactId():record.getContactId());
		oldRecord.setDetailAddr(record.getDetailAddr()==null?oldRecord.getDetailAddr():record.getDetailAddr());
		oldRecord.setContactName(record.getContactName()==null?oldRecord.getContactName():record.getContactName());
		oldRecord.setActivityCate(record.getActivityCate()==null?oldRecord.getActivityCate():record.getActivityCate());
		oldRecord.setCreateDeptId(record.getCreateDeptId()==null?oldRecord.getCreateDeptId():record.getCreateDeptId());
		oldRecord.setContactDeptId(record.getContactDeptId()==null?oldRecord.getContactDeptId():record.getContactDeptId());
		oldRecord.setSubActFlag(record.getSubActFlag()==null?oldRecord.getSubActFlag():record.getSubActFlag());
		oldRecord.setParentActId(record.getParentActId()==null?oldRecord.getParentActId():record.getParentActId());
	}

	// 修改活动
	@ApiOperation(value = "关闭活动")
	// @RequiresPermissions("ty.tyActivity.update")
	@RequestMapping(value = "/closeActivity", method = RequestMethod.POST)
	public Object closeActivity(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "activityId", required = true) String activityId) {
		if (StringUtils.isBlank(activityId)) {
			throw new IllegalArgumentException("活动id不能为空，请检查参数!");
		}
		TyActivity activity = tyActivityService.queryById(activityId);
		String userId = WebUtil.getCurrentUser();
		if (StringUtils.equals(userId, activity.getCreateBy())
				&& StringUtils.equals(activity.getActivityStatus(), "0")) {
			activity.setActivityStatus("1");
			tyActivityService.update(activity);
		}
		return setSuccessModelMap(modelMap);
	}

	// 删除活动
	@ApiOperation(value = "删除活动")
	// @RequiresPermissions("ty.tyActivity.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivity record = tyActivityService.queryById(id);
		if (record != null) {
			tyActivityService.deletePhysical(id);
			tyActivitysignService.deleteByActId(id);
			tyActivitystaffService.deleteByActId(id);
		}
		return setSuccessModelMap(modelMap);
	}

	// 批量导入活动
	@ApiOperation(value = "批量导入活动")
	// @RequiresPermissions("ty.tyActivity.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile,
			@RequestParam(value = "actId", required = false) String actId) {
		String fileName = multipartFile.getOriginalFilename();
		if (StringUtils.isBlank(fileName) || !isExcel(fileName)) {
			throw new IllegalParameterException("无法识别该文件");
		}
		String rootFilePath = HtmlUtils.getExcelTmpFilePath();
		File rootFile = new File(rootFilePath);
		if (!rootFile.isDirectory()) {
			rootFile.mkdirs();
		}
		String filePath = rootFilePath + File.separator + fileName;
		File file = new File(filePath);
		file.setWritable(true, false);
		try {
			multipartFile.transferTo(file);
		} catch (Exception e) {
			logger.error(fileName + "保存失败", e);
			throw new BusinessException("文件保存失败");
		}
		List<String> list = new ArrayList<String>();
		int successNum = 0;
		if (StringUtils.isBlank(actId)) {
			successNum = importActivityExcel(file, list);
		} else {
			successNum = importActCustExcel(file, list, actId);
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("errorLogList", list);
		m.put("successNum", successNum);
		if(successNum == 0){
			return setModelMap(modelMap, HttpCode.BAD_REQUEST,m);
		}
		return setSuccessModelMap(modelMap, m);
	}

	// 活动导入Excel
	private int importActivityExcel(File excel, List<String> list) {
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean readColFinished = false;
		SysUser sysUser = null;
		TyActivity result = null;
		Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKeyContrary("ACTIVITYTYPE");
		Map<String, String> prioritylevelMap = sysDicService.queryDicByDicIndexKeyContrary("PRIORITYLEVEL");
		Map<String, String> activitystatusMap = sysDicService.queryDicByDicIndexKeyContrary("ACTIVITYSTATUS");
		Map<String, String> topictypeMap = sysDicService.queryDicByDicIndexKeyContrary("TOPICTYPE");
		try {
			// 获取Excel的绝对路径，生成Workbook
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						currColNum = 0;
						readColFinished = false;
						// Excel列的数据
						String title = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String content = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String activity_type = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						activity_type = activity_type == null ? null : activitytypeMap.get(activity_type);
						if (activity_type==null) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列活动类型不符合规定,请手动修改;");
						}
						currColNum = currColNum + 1;
						if (StringUtils.isBlank(title) && StringUtils.isNotBlank(content)) {
							title = StringUtils.left(content, 1000);
						}
						if (StringUtils.isBlank(title)) {
							title = "-";
						}
						String s_begin_date = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_begin_date = StringUtils.remove(s_begin_date, "-");
						if (s_begin_date.length() > 8 && s_begin_date.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列开始日期不符合规定,请手动新增;");
						}
						int begin_date = Integer.parseInt(s_begin_date);
						currColNum = currColNum + 1;
						String s_begin_time = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_begin_time = StringUtils.remove(s_begin_time, ":");
						if (StringUtils.isBlank(s_begin_time)) {
							s_begin_time = "0";
						} else if (s_begin_time.length() > 6 && s_begin_time.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列开始时间不符合规定,请手动新增;");
						}
						int begin_time = Integer.parseInt(s_begin_time);
						currColNum = currColNum + 1;
						String s_end_date = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_end_date = StringUtils.remove(s_end_date, "-");
						if (StringUtils.isBlank(s_end_date)) {
							if (StringUtils.equals(activity_type, "1")) {
								s_end_date = s_begin_date;
							} else {
								s_end_date = "0";
							}

						} else if (s_end_date.length() > 8 && s_end_date.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列结束日期不符合规定,请手动新增;");
						}
						int end_date = Integer.parseInt(s_end_date);
						currColNum = currColNum + 1;
						String s_end_time = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_end_time = StringUtils.remove(s_end_time, ":");
						if (StringUtils.isBlank(s_end_time)) {
							s_end_time = "0";
						} else if (s_end_time.length() > 6 && s_end_time.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列结束时间不符合规定,请手动新增;");
						}
						int end_time = Integer.parseInt(s_end_time);
						currColNum = currColNum + 1;
						int total_duration = Integer
								.parseInt(getTimeInterval(s_begin_date, s_begin_time, s_end_date, s_end_time));
						currColNum = currColNum + 1;
						String s_avg_duration = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isBlank(s_avg_duration)) {
							s_avg_duration = "0";
						} else if (s_avg_duration.length() > 8 && s_avg_duration.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列不符合规定,请手动新增;");
						}
						int avg_duration = Integer.parseInt(s_avg_duration);
						currColNum = currColNum + 1;

						String priority_level = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						priority_level = priority_level == null ? null : prioritylevelMap.get(priority_level);
						currColNum = currColNum + 1;
						String s_total_limit = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isBlank(s_total_limit)) {
							s_total_limit = "0";
						} else if (s_total_limit.length() > 8 && s_total_limit.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列不符合规定,请手动新增;");
						}
						int total_limit = Integer.parseInt(s_total_limit);
						currColNum = currColNum + 1;
						String s_per_limit = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isBlank(s_per_limit)) {
							s_per_limit = "0";
						} else if (s_per_limit.length() > 8 && s_per_limit.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列不符合规定,请手动新增;");
						}
						int per_limit = Integer.parseInt(s_per_limit);
						currColNum = currColNum + 1;
						String locale = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String labels = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String remark = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String activity_status = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						activity_status = activity_status == null ? null : activitystatusMap.get(activity_status);
						currColNum = currColNum + 1;
						String topic_type = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						topic_type = topic_type == null ? null : topictypeMap.get(topic_type);
						currColNum = currColNum + 1;
						String attachs = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String attachs_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String data_src = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if(StringUtils.isBlank(data_src)){
//							data_src="1";//文件导入
						}
						data_src="1";//写死为[文件导入]
						currColNum = currColNum + 1;
						String data_srcname = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String create_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String researcher_list = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String cust_list = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String stock_list = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;

						readColFinished = true;

						// 判断创建人是否是当前用户
						if (StringUtils.isNotBlank(create_name)) {
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("userName", create_name);
							List<SysUser> user = sysUserService.queryByCondition(params);
							if (user.size() > 0) {
								sysUser = user.get(0);
							} else {
								String userId = WebUtil.getCurrentUser();
								sysUser = sysUserService.queryById(userId);
							}
						}

						// 添加活动表
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("deleteFlag", "0");
						param.put("titleAccurate", title);
						param.put("beginDateAccurate", s_begin_date);
						param.put("activityType", activity_type);
						param.put("dataSrc", "1");//1表示文件导入,写死
						param.put("createBy", sysUser.getId());//
						List<TyActivityBean> activity = tyActivityService.queryByCondition(param);
						if (activity.size() > 0) {
							result = activity.get(0);
							// 更新
							if (title != null) {
								result.setTitle(title);
							}
							if (content != null) {
								result.setContent(content);
							}
							if (begin_date != 0) {
								result.setBeginDate(begin_date);
							}
							if (begin_time != 0) {
								result.setBeginTime(begin_time);
							}
							if (end_date != 0) {
								result.setEndDate(end_date);
							}
							if (end_time != 0) {
								result.setEndTime(end_time);
							}
							if (total_duration != 0) {
								result.setTotalDuration(total_duration);
							}
							if (StringUtils.equals(activity_type, "1")) {
								String[] custLists = cust_list.split(",");
								avg_duration = total_duration / custLists.length;
							}
							if (avg_duration != 0) {
								result.setAvgDuration(avg_duration);
							}
							if (activity_type != null) {
								result.setActivityType(activity_type);
							}
							if (priority_level != null) {
								result.setPriorityLevel(priority_level);
							}
							if (total_limit != 0) {
								result.setTotalLimit(total_limit);
							}
							if (per_limit != 0) {
								result.setPerLimit(per_limit);
							}
							if (locale != null) {
								result.setLocale(locale);
							}
							if (StringUtils.isNotBlank(labels)) {
								Set<String> labelSet = new LinkedHashSet<>();
//								result.setLabels(labels);
								labels = labels.replaceAll("　", Constants.LABEL_SEPERATOR);// 全角空格替换为英文空格
								String[] labelArray2 = labels.split(" +");// 支持多空格
								for (String l : labelArray2) {
									labelSet.add(l);
								}
								StringBuffer sb = new StringBuffer();
								for (String labelName : labelSet) {
									sb.append(labelName + Constants.LABEL_SEPERATOR);
								}
								result.setLabels(sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "");
							}
							if (remark != null) {
								result.setRemark(remark);
							}
							if (activity_status != null) {
								result.setActivityStatus(activity_status);
							}
							if (topic_type != null) {
								result.setTopicType(topic_type);
							}
							if (attachs != null) {
								result.setAttachs(attachs);
							}
							if (attachs_name != null) {
								result.setAttachsName(attachs_name);
							}
							//result.setDataSrc("0");该字段不做更新
							if (data_srcname != null) {
								result.setDataSrcname(data_srcname);
							}
							if (StringUtils.isNotBlank(org_name)) {
								Map<String, Object> n_param = new HashMap<String, Object>();
								n_param.put("deleteFlag", "0");
								n_param.put("orgNameAccurate", org_name);
								List<TyServiceorgBean> org = tyServiceorgService.queryByCondition(n_param);
								if (org.size() > 0) {
									TyServiceorgBean serviceOrg = org.get(0);
									if (serviceOrg.getId() != null) {
										result.setOrgId(serviceOrg.getId());
									}
								} else {
									throw new BusinessException("添加机构名称报错，没有此机构名称，请手动导入Excel");
								}
							}
							if (sysUser.getUserName() != null) {
								result.setCreateName(sysUser.getUserName());
							}
							if (sysUser.getId() != null) {
								result.setCreateBy(sysUser.getId());
							}
							if (sysUser.getId() != null) {
								result.setUpdateBy(sysUser.getId());
							}

							result.setNotifyFlag("1");
							result.setActivityCate("0");
							if (sysUser.getDeptId() != null) {
								result.setCreateDeptId(sysUser.getDeptId());
							}
							tyActivityService.update(result);
						} else {
							// 新建
							TyActivity record = new TyActivity();
							readColFinished = true;
							record.setTitle(title);
							record.setContent(content);
							record.setBeginDate(begin_date);
							record.setBeginTime(begin_time);
							record.setEndDate(end_date);
							record.setEndTime(end_time);
							record.setTotalDuration(total_duration);
							if (StringUtils.equals(activity_type, "1")) {
								String[] custLists = cust_list.split(",");
								avg_duration = total_duration / custLists.length;
							}
							record.setAvgDuration(avg_duration);
							record.setActivityType(activity_type);
							record.setPriorityLevel(priority_level);
							record.setTotalLimit(total_limit);
							record.setPerLimit(per_limit);
							record.setLocale(locale);
							record.setLabels(labels);
							record.setRemark(remark);
							record.setActivityStatus(activity_status);
							record.setTopicType(topic_type);
							record.setAttachs(attachs);
							record.setAttachsName(attachs_name);
							record.setDataSrc("1");
							record.setDataSrcname(data_srcname);
							if (StringUtils.isNotBlank(org_name)) {
								Map<String, Object> n_param = new HashMap<String, Object>();
								n_param.put("deleteFlag", "0");
								n_param.put("orgNameAccurate", org_name);
								List<TyServiceorgBean> org = tyServiceorgService.queryByCondition(n_param);
								if (org.size() > 0) {
									TyServiceorgBean serviceOrg = org.get(0);
									record.setOrgId(serviceOrg.getId());
								} else {
									throw new BusinessException("添加机构名称报错，没有此机构名称，请手动导入Excel");
								}
							}
							record.setCreateName(sysUser.getUserName());
							record.setCreateBy(sysUser.getId());
							record.setUpdateBy(sysUser.getId());
							record.setNotifyFlag("1");
							record.setActivityCate("0");
							record.setSubActFlag("0");
							record.setCreateDeptId(sysUser.getDeptId());
							result = tyActivityService.add(record);
						}

						//
						boolean excepFlag = false;
						StringBuffer excepMsg = new StringBuffer();
						// 添加参与客户
						if (StringUtils.isNotBlank(cust_list)) {
							tyActivitysignService.deleteByActId(result.getId());
							String[] custLists = cust_list.split(",");
							for (String custName : custLists) {
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("deleteFlag", "0");
								params.put("custNameAccurate", custName);
								List<TyOrgcustomerBean> cust = tyOrgcustomerService.queryByCondition(params);
								if (cust.size() == 1) {
									TyOrgcustomerBean customer = cust.get(0);
									TyActivitysign sign = new TyActivitysign();
									sign.setCreateBy(sysUser.getId());
									sign.setActivityId(result.getId());
									sign.setCustId(customer.getId());
									sign.setCustName(customer.getCustName());
									sign.setCustMobile(customer.getCustMobile());
									sign.setOrgId(customer.getOrgId());
									sign.setOrgName(customer.getOrgName());
									sign.setSignStatus("1");
									tyActivitysignService.add(sign);
								}else if (cust.size() >1) {
									excepFlag = true;
									excepMsg = excepMsg.append("添加客户报错:["+custName+"]有多条重名记录，请手动调整数据。");
								}else {
									excepFlag = true;
									excepMsg = excepMsg.append("添加客户报错:["+custName+"]没有此客户，请手动调整数据。");
								}
							}
						}
						// 添加研究员
						if (StringUtils.isNotBlank(researcher_list)) {
							
							tyActivitystaffService.deleteByActId(result.getId());
							
							String[] researcherLists = researcher_list.split(",");
							for (String researcherName : researcherLists) {
								Map<String, Object> params = new HashMap<String, Object>();
//								params.put("deleteFlag", "0");
								params.put("staffNameAccurate", researcherName);
								List<TyStafflistBean> staffList = tyStafflistService.queryByCondition(params);
								if (staffList.size() ==1) {
									TyStafflistBean staff = staffList.get(0);
									TyActivitystaff activityStaff = new TyActivitystaff();
									activityStaff.setCreateBy(sysUser.getId());
									activityStaff.setActivityId(result.getId());
									activityStaff.setStaffId(staff.getId());
									activityStaff.setStaffName(staff.getStaffName());
									activityStaff.setDeptId(staff.getDeptId());
									tyActivitystaffService.add(activityStaff);
								}else if (staffList.size() >1) {
									excepFlag = true;
									excepMsg = excepMsg.append("添加研究员报错:["+researcherName+"]有多条重名记录，请手动调整数据。");
								} else {
									excepFlag = true;
									excepMsg = excepMsg.append("添加研究员报错:["+researcherName+"]没有此研究员，请手动调整数据。");
								}
							}
						}
						// 添加推荐股票
						if (StringUtils.isNotBlank(stock_list)) {
							
							tyActivitystockService.deleteByActId(result.getId());
							
							String[] stockLists = stock_list.split(",");
							for (String stockList : stockLists) {
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("stockName", stockList);
								List<TyDcrecostockBean> dcrecostockList = tyDcrecostockService.queryByCondition(params);
								if (dcrecostockList.size() > 0) {
									TyDcrecostockBean stock = dcrecostockList.get(0);
									TyActivitystock activityStock = new TyActivitystock();
									activityStock.setCreateBy(sysUser.getId());
									activityStock.setActivityId(result.getId());
									activityStock.setStockId(stock.getId());
									activityStock.setActType(result.getActivityType());
									tyActivitystockService.add(activityStock);
								} else {
									excepFlag = true;
									excepMsg = excepMsg.append("添加推荐股票报错:["+stockList+"]没有此股票，请手动调整数据。");
								}
							}
						}
						//统一抛出后续的异常
						if(excepFlag){
							throw new BusinessException(excepMsg.toString());
						}
						
						successNum++;
					} catch (BusinessException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						if (readColFinished) {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行数据处理异常;");
						} else {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列解析异常;");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalParameterException("解析Excel文件内容失败");
		} finally {
			try {
				hssfWorkbook.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalParameterException("解析Excel文件内容失败");
			}
		}
		list.add(0, "成功处理" + successNum + "条数据;");
		return successNum;
	}

	// 电话会议活动的参与客户导入Excel
	private int importActCustExcel(File excel, List<String> list, String actId) {
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean readColFinished = false;
		SysUser sysUser = null;
		try {
			tyActivitysignService.deleteByActId(actId);
			// 获取Excel的绝对路径，生成Workbook
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						currColNum = 0;
						readColFinished = false;
						// Excel列的数据
						String cust_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String create_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String s_arrive_time = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_arrive_time = StringUtils.remove(s_arrive_time, ":");
						if (StringUtils.isBlank(s_arrive_time)) {
							s_arrive_time = "0";
						} else if (s_arrive_time.length() > 6 && s_arrive_time.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列不符合规定,请手动新增;");
						}
						int cust_arrive_time = Integer.parseInt(s_arrive_time);
						currColNum = currColNum + 1;
						String s_leave_time = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						s_leave_time = StringUtils.remove(s_leave_time, ":");
						if (StringUtils.isBlank(s_leave_time)) {
							s_leave_time = "0";
						} else if (s_leave_time.length() > 6 && s_leave_time.length() < 0) {
							throw new BusinessException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列不符合规定,请手动新增;");
						}
						int cust_leave_time = Integer.parseInt(s_leave_time);
						currColNum = currColNum + 1;
						readColFinished = true;

						// 判断创建人是否是当前用户
						if (StringUtils.isNotBlank(create_name)) {
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("userName", create_name);
							List<SysUser> user = sysUserService.queryByCondition(params);
							if (user.size() > 0) {
								sysUser = user.get(0);
							} else {
								String userId = WebUtil.getCurrentUser();
								sysUser = sysUserService.queryById(userId);
							}
						}

						// 添加参与客户
						if (StringUtils.isNotBlank(cust_name)) {
							
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("deleteFlag", "0");
							params.put("custNameAccurate", cust_name);
							List<TyOrgcustomerBean> cust = tyOrgcustomerService.queryByCondition(params);
							if (cust.size() > 0) {
								TyOrgcustomerBean customer = cust.get(0);
								TyActivitysign sign = new TyActivitysign();
								sign.setCreateBy(sysUser.getId());
								sign.setActivityId(actId);
								sign.setCustId(customer.getId());
								sign.setCustName(customer.getCustName());
								sign.setCustMobile(customer.getCustMobile());
								sign.setOrgId(customer.getOrgId());
								sign.setOrgName(customer.getOrgName());
								sign.setArriveTime(cust_arrive_time);
								sign.setLeaveTime(cust_leave_time);
								sign.setTotalDuration(
										Integer.parseInt(getCustTimeInterval(s_arrive_time, s_leave_time)));
								tyActivitysignService.add(sign);
							} else {
								throw new BusinessException("添加客户报错，没有此客户，请手动导入Excel");
							}

						} else {
							throw new BusinessException("添加客户报错，参与客户姓名为空，请手动导入Excel");
						}
						successNum++;

					} catch (BusinessException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						if (readColFinished) {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行数据处理异常;");
						} else {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列解析异常;");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalParameterException("解析Excel文件内容失败");
		} finally {
			try {
				hssfWorkbook.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalParameterException("解析Excel文件内容失败");
			}
		}
		list.add(0, "成功添加" + successNum + "条数据;");
		return successNum;
	}

	/**
	 * 是否是Excel文件
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isExcel(String fileName) {
		if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取时间间隔
	 */
	private String getTimeInterval(String s_begin_date, String s_begin_time, String s_end_date, String s_end_time) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMdd HHmmss");
		Date begin = null;
		Date end = null;
		long minute = 0;
		try {
			begin = dfs.parse(s_begin_date + " " + s_begin_time);
			end = dfs.parse(s_end_date + " " + s_end_time);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			minute = between / 60;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Long.toString(minute);
	}

	/**
	 * 获取客户到达离开的时间间隔
	 */
	private String getCustTimeInterval(String s_arrive_time, String s_leave_time) {
		SimpleDateFormat dfs = new SimpleDateFormat("HHmmss");
		Date begin = null;
		Date end = null;
		long between = 0;
		try {
			begin = dfs.parse(s_arrive_time);
			end = dfs.parse(s_leave_time);
			between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Long.toString(between);
	}
}
