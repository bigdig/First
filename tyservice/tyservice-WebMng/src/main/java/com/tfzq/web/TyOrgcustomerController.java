package com.tfzq.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.DateUtil.DATE_PATTERN;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysPositionroleService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyMessageauditService;
import com.tfzq.service.TyMessagelogService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyMessagelogBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.InputValidatorUtil;
import com.tfzq.util.excel.WriteExcelUtil;
import com.tfzq.yjgl.DESPlus;
import com.tfzq.yjgl.MsgSendFlow;
import com.tfzq.yjgl.MsgSendFlowClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.OracleTypes;

/**
 * 机构客户控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "机构客户管理", description = "机构客户管理")
@RequestMapping(value = "ty/tyOrgcustomer", method = RequestMethod.POST)
public class TyOrgcustomerController extends BaseController {
	@Autowired
	private TyMessagelogService tyMessagelogService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private TyMessageauditService tyMessageauditService;
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private SysDicService sysDicService;

	// 查询机构客户
	@ApiOperation(value = "查询机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		if (StringUtils.isNotBlank(request.getParameter("orderBy"))) {
			String orderString = camelToUnderline(request.getParameter("orderBy")) + " " + (StringUtils.isBlank(request.getParameter("rule")) ? "desc" : request.getParameter("rule"));
			// params.put("orderBy", orderString +" , id desc");
		} else {
			params.put("orderBy", "create_time desc, id desc");
		}

		// 添加查询范围条件
		// geneDataScope(params);
		PageInfo<TyOrgcustomerBean> list = null;
		// if(params.get("marks") != null && StringUtils.isNotBlank((CharSequence) params.get("marks"))){
		params.put("userId", WebUtil.getCurrentUser());
		// 设置输出参数为游标类型
		params.put("customerList", OracleTypes.CURSOR);
		list = tyOrgcustomerService.queryCustomerWithMarksByProc(params);
		// }else{
		// //转换标签查询条件
		// transMarks(params);
		// list = tyOrgcustomerService.queryBeans(params);
		// }

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysArea> sysAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysAreas", sysAreas, "id", "areaName");

		List<TyServiceorg> serviceOrgList = tyServiceorgService.getAllRecords();
		Map<String, TyServiceorg> serviceOrgMap = new HashMap<String, TyServiceorg>();
		for (TyServiceorg org : serviceOrgList) {
			serviceOrgMap.put(org.getId(), org);
		}
		addDictFromModel(dicts, "Serviceorgs", serviceOrgList, "id", "orgName");
		//
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts, "TyStafflists", tyStafflists, "id", "staffName");
		Map<String, TyStafflist> tyStaffMap = new HashMap<String, TyStafflist>();
		for (TyStafflist staff : tyStafflists) {
			tyStaffMap.put(staff.getId(), staff);
		}
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		Map<String, String> industryMap = sysDicService.queryDicByDicIndexKey("INDUSTRY");
		addDictFromMap(dicts, "industry", industryMap);
		Map<String, String> isAcceptRptMap = sysDicService.queryDicByDicIndexKey("ISACCEPTRPT");
		addDictFromMap(dicts, "isAcceptRpt", isAcceptRptMap);
		Map<String, String> rptSendGroupMap = sysDicService.queryDicByDicIndexKey("RPTSENDGROUP");
		addDictFromMap(dicts, "rptSendGroup", rptSendGroupMap);

		// 添加字典翻译
		if (null != list && null != list.getList() && list.getList().size() > 0) {
			for (TyOrgcustomerBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getSalerId())) {
					TyStafflist temp = tyStaffMap.get(singleBean.getSalerId());
					singleBean.setServiceSaler(temp == null ? "" : temp.getStaffName());
				}
				if (StringUtils.isNotEmpty(singleBean.getIsAcceptRpt())) {
					singleBean.setIsAcceptRptText(isAcceptRptMap.get(singleBean.getIsAcceptRpt()));
				}

				if (StringUtils.isNotEmpty(singleBean.getOrgId())) {
					TyServiceorg temp = serviceOrgMap.get(singleBean.getOrgId());
					singleBean.setOrgName(temp == null ? "" : temp.getOrgName());
					singleBean.setCustStatus(temp == null ? "" : temp.getCustStatus());
					// 非已签约客户才有白名单截止时间
					singleBean.setWhiteDeadline((temp == null || StringUtils.equals(temp.getCustStatus(), "3")) ? null : temp.getWhiteDeadline());
					if (StringUtils.isNotEmpty(singleBean.getCustStatus())) {
						singleBean.setCustStatusName(custStatusMap.get(singleBean.getCustStatus().toString()));
					}
				}
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	@Deprecated
	private void transMarks2(Map<String, Object> params) {
		String marks = (String) params.get("marks");
		if (StringUtils.isNotBlank(marks)) {
			String[] split = marks.split(",");
			params.remove("marks");
			params.put("marks", Arrays.asList(split));
		} else {
			params.remove("marks");
		}
	}

	/**
	 * 根据不同的角色设置不同的客户数据查询范围 查询客户列表，角色范围下移到存储过程中
	 * 
	 * @param params
	 */
	@Deprecated
	private void geneDataScope2(Map<String, Object> params) {
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		// 已有角色
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(currUser.getId());
		if (tyStafflistService.isSeniorseller(userrole)) {// 如果是高级销售
			// 1 找出所有销售职位
			List<String> roleList = new ArrayList<String>();
			roleList.add("seniorseller");
			roleList.add("seller");
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("roleList", roleList);
			params2.put("pageSize", 1000);
			PageInfo<SysPositionroleBean> spr = sysPositionroleService.queryBeans(params2);
			List<String> positionList = new ArrayList<String>();
			for (Iterator<SysPositionroleBean> iterator = spr.getList().iterator(); iterator.hasNext();) {
				SysPositionroleBean sprbean = iterator.next();
				positionList.add(sprbean.getPositionId());
			}
			// 2 找出所有销售职位对应的销售人员
			Map<String, Object> params3 = new HashMap<String, Object>();
			params3.put("positionList", positionList);
			// params3.put("pageSize", 1000);
			// Map<String, Object> params4 = new HashMap<String, Object>();
			// params4.put("userId", currUser.getAccount());
			TyStafflistBean selfStaff = tyStafflistService.queryByUserId(currUser.getAccount());
			params3.put("workAreaid", selfStaff.getWorkAreaid());

			List<TyStafflistBean> stafflist = tyStafflistService.queryByCondition(params3);
			List<String> salerList = new ArrayList<String>();
			for (Iterator<TyStafflistBean> iterator = stafflist.iterator(); iterator.hasNext();) {
				TyStafflistBean staffBean = iterator.next();
				salerList.add(staffBean.getId());
			}
			params.put("salerList", salerList); // 设置条件
		} else if (tyStafflistService.isSeller(userrole)) {// 如果是一般销售
			// Map<String, Object> params2 = new HashMap<String, Object>();
			// params2.put("userId", currUser.getAccount());
			// PageInfo<TyStafflistBean> stafflist = tyStafflistService.queryBeans(params2);
			TyStafflistBean selfStaff = tyStafflistService.queryByUserId(currUser.getAccount());
			params.put("salerId", selfStaff.getId()); // 设置条件
		}
	}

	// 查询所有机构客户，查询逻辑与get方法相同，结果不翻页
	@ApiOperation(value = "查询机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/allList")
	public Object getAll(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		// 转换标签查询条件
		// transMarks(params);

		// 添加查询范围条件
		// geneDataScope(params);
		// @TODO 需要确认一下在做这个查询的时候是否有触发查询 ，否则使用原方法
		params.put("userId", WebUtil.getCurrentUser());
		List<TyOrgcustomerBean> list = tyOrgcustomerService.queryByConditionFromProcResult(params);

		return setSuccessModelMap(modelMap, list);
	}

	/**
	 * 专家库中根据姓名模糊查询信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "模糊查询机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/searchByCondition")
	public Object searchByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		List<TyOrgcustomerBean> list = tyOrgcustomerService.queryByCondition(params);

		return setSuccessModelMap(modelMap, list);
	}

	// 查询机构客户字典
	@ApiOperation(value = "查询机构客户字典")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/custdict")
	public Object getCustdict(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<DictItem> custdicts = tyOrgcustomerService.queryCustdict(params);
		dicts.put("custdicts", custdicts);
		return setSuccessModelMap(modelMap, dicts);
	}

	// 详细信息
	@ApiOperation(value = "机构客户详情")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyOrgcustomerBean tyOrgcustomerBean = new TyOrgcustomerBean();
		Map<String, List<ExDictItem>> dicts = new HashMap<String, List<ExDictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyOrgcustomer record = tyOrgcustomerService.queryById(id);

			// 更新remark冗余字段
			// Map<String, Object> pm = new HashMap<String, Object>();
			// pm.put("customerId", record.getId());
			// pm.put("pageSize", "1000");
			// pm.put("orderBy", "id desc");
			List<TyCustomerlabelBean> labelBeans = tyCustomerlabelService.queryByCustId(record.getId());
			tyOrgcustomerBean.setCustLabelList(labelBeans);
			StringBuffer sb = new StringBuffer();
			if (labelBeans != null && labelBeans.size() > 0) {
				for (TyCustomerlabelBean labelBean : labelBeans) {
					if (labelBean.getLabelId() != null) {
						TyLabel tl = tyLabelService.queryById(labelBean.getLabelId());
						sb.append(tl.getLabelName() + Constants.LABEL_SEPERATOR);
					}
				}
				String mark = sb.toString();
				record.setMark(mark.substring(0, mark.length() - 1));
				tyOrgcustomerService.sycnMarkAuto(record);
			}

			try {
				BeanUtils.copyProperties(tyOrgcustomerBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyOrgcustomerBean = null;
				e.printStackTrace();
			}
		}

		// 添加字典翻译
		Map<String, String> isAcceptRptMap = sysDicService.queryDicByDicIndexKey("ISACCEPTRPT");
		List<ExDictItem> acceptitems = new ArrayList<ExDictItem>();
		for (String newKey : isAcceptRptMap.keySet()) {
			ExDictItem item = new ExDictItem();
			item.setId(newKey);
			item.setText(isAcceptRptMap.get(newKey));
			acceptitems.add(item);
		}
		dicts.put("isAcceptRpt", acceptitems);
		Map<String, String> rptSendGroupMap = sysDicService.queryDicByDicIndexKey("RPTSENDGROUP");
		List<ExDictItem> sendgroupitems = new ArrayList<ExDictItem>();
		for (String newKey : rptSendGroupMap.keySet()) {
			ExDictItem item = new ExDictItem();
			item.setId(newKey);
			item.setText(rptSendGroupMap.get(newKey));
			sendgroupitems.add(item);
		}
		dicts.put("rptSendGroup", sendgroupitems);

		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords(); // ***
																				// 找销售人员
		List<ExDictItem> staffitems = new ArrayList<ExDictItem>();
		for (Iterator iterator = tyStafflists.iterator(); iterator.hasNext();) {
			TyStafflist tyStafflist = (TyStafflist) iterator.next();
			ExDictItem item = new ExDictItem();
			item.setId(tyStafflist.getId());
			item.setText(tyStafflist.getStaffName());

			staffitems.add(item);
		}
		dicts.put("TyStafflists", staffitems);
		// addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");

		List<TyServiceorg> tyServiceorgs = tyServiceorgService.getAllRecords();
		List<ExDictItem> orgitems = new ArrayList<ExDictItem>();
		for (Iterator iterator = tyServiceorgs.iterator(); iterator.hasNext();) {
			TyServiceorg tyServiceorg = (TyServiceorg) iterator.next();
			ExDictItem item = new ExDictItem();
			item.setId(tyServiceorg.getId());
			item.setText(tyServiceorg.getOrgName());
			item.setRemark(tyServiceorg.getSalerId());
			orgitems.add(item);
		}
		dicts.put("TyServiceorgs", orgitems);

		List<TyLabelBean> catLabelList = new ArrayList<TyLabelBean>();
		if (tyOrgcustomerBean != null) {
			if (StringUtils.isNotEmpty(tyOrgcustomerBean.getSalerId())) {
				TyStafflist temp = tyStafflistService.queryById(tyOrgcustomerBean.getSalerId());
				tyOrgcustomerBean.setServiceSaler(temp == null ? "" : temp.getStaffName());
			}
			if (StringUtils.isNotEmpty(tyOrgcustomerBean.getOrgId())) {
				TyServiceorg temp = tyServiceorgService.queryById(tyOrgcustomerBean.getOrgId());
				tyOrgcustomerBean.setOrgName(temp == null ? "" : temp.getOrgName());
				tyOrgcustomerBean.setCustStatus(temp.getCustStatus());
			}
			if (null != tyOrgcustomerBean.getId()) {

				catLabelList = tyLabelService.getCatListByCustomerId(tyOrgcustomerBean.getId());

				if (catLabelList != null) {
					for (Iterator iterator = catLabelList.iterator(); iterator.hasNext();) {
						TyLabelBean tyLabelBean2 = (TyLabelBean) iterator.next();
						List<TyLabelBean> subList = new ArrayList<TyLabelBean>();
						Map<String, Object> pa = new HashMap<String, Object>();
						pa.put("customerId", tyOrgcustomerBean.getId());
						pa.put("catId", tyLabelBean2.getId());
						subList = tyLabelService.getCustSubList(pa);
						if (subList != null) {
							tyLabelBean2.setSubList(subList);
						}
					}
				}
			}
		}
		// tyOrgcustomerBean.setLabelList(labelList);
		tyOrgcustomerBean.setLabelCatList(catLabelList);

		return setSuccessModelMap(modelMap, tyOrgcustomerBean, dicts);
	}

	// 新增机构客户
	@ApiOperation(value = "添加机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "labels[]", required = false) List<String> labels) {
		TyOrgcustomer record = Request2ModelUtil.covert(TyOrgcustomer.class, request);

		if (!InputValidatorUtil.isEmail(record.getCustEmail())) {
			throw new BusinessException("邮箱格式错误，请重新输入");
		}

		if (!InputValidatorUtil.is263Email(record.getCustEmail())) {
			throw new BusinessException("邮箱格式错误：" + InputValidatorUtil.ERROR_263_INFO);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");

		// record.setCustTel(StringUtils.remove(record.getCustTel(), "-"));
		record.setCustMobile(StringUtils.remove(record.getCustMobile(), "-"));
		if (StringUtils.isNotBlank(record.getCustMobile())) {
			params.put("custMobile", record.getCustMobile());
			custlist = tyOrgcustomerService.queryByCondition(params);
			if (custlist != null && custlist.size() > 0) {
				throw new BusinessException("该手机号已存在于" + custlist.get(0).getOrgName() + ",其对口销售为" + custlist.get(0).getServiceSaler() + ",请联系原销售进行调整");
			}
		} else {
			throw new BusinessException("请输入手机号");
		}

		if (labels != null) {
			// 先去重,冗余mark字段
			removeDuplicate(labels);
			StringBuffer sb = new StringBuffer();
			for (String labelName : labels) {
				sb.append(labelName + Constants.LABEL_SEPERATOR);
			}
			// String mark = sb.toString();
			record.setMark(sb.substring(0, sb.length() - 1));
		}

		if (StringUtils.isNotEmpty(record.getOrgId())) {
			TyServiceorg temp = tyServiceorgService.queryById(record.getOrgId());
			record.setOrgName(temp == null ? "" : temp.getOrgName());
		}
		if (StringUtils.isNotEmpty(record.getSalerId())) {
			TyStafflist staff = tyStafflistService.queryById(record.getSalerId());
			record.setServiceSaler(staff == null ? "" : staff.getStaffName());
		}
		record.setDeleteFlag("0");
		TyOrgcustomer newRecord = tyOrgcustomerService.add(record);

		// 更新客户标签
		Set<String> labelSet = new LinkedHashSet<String>();
		if (labels != null)
			labelSet.addAll(labels);
		updateLabels(labelSet, newRecord.getId());

		// // TODO 调用推送新增客户接口
		// try {
		// tyOrgcustomerService.update31Cust(newRecord);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return setSuccessModelMap(modelMap);
	}

	// 新增机构客户
	@ApiOperation(value = "添加机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/addSimpleInfo", method = RequestMethod.POST)
	public Object addSimpleInfo(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "labels[]", required = false) List<String> labels) {
		TyStafflist staff = tyStafflistService.queryById(WebUtil.getCurrentUser());
		TyOrgcustomer record = Request2ModelUtil.covert(TyOrgcustomer.class, request);
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");

		// record.setCustTel(StringUtils.remove(record.getCustTel(), "-"));
		record.setCustMobile(StringUtils.remove(record.getCustMobile(), "-"));
		if (StringUtils.isNotBlank(record.getCustMobile())) {
			params.put("custMobile", record.getCustMobile());
			custlist = tyOrgcustomerService.queryByCondition(params);
			if (custlist != null && custlist.size() > 0) {
				throw new BusinessException("手机号已存在");
			}
		} else {
			throw new BusinessException("请输入手机号");
		}
		// 填入机构名称
		Map<String, Object> orgParams = new HashMap<String, Object>();
		orgParams.put("orgName", record.getOrgName());
		// List<TyServiceorgBean> orgList = tyServiceorgService.queryByCondition(orgParams);
		TyServiceorgBean org = queryOrgByNameLike(record.getOrgName());
		if (org == null) {
			TyServiceorg serviceOrg = new TyServiceorg();
			serviceOrg.setOrgName(record.getOrgName());
			serviceOrg.setOrgSimpleName(record.getOrgName());
			serviceOrg.setSalerId(staff.getId());
			serviceOrg.setServiceSaler(staff.getStaffName());
			SysArea temp = sysAreaService.queryById(staff.getWorkAreaid());
			serviceOrg.setTeamCat(temp == null ? "" : temp.getAreaName());
			serviceOrg.setOrgLevel("4");
			serviceOrg.setCustStatus("4");
			serviceOrg.setRecEmail("1");
			serviceOrg.setRecSms("1");
			serviceOrg.setDeleteFlag("0");
			TyServiceorg result = tyServiceorgService.add(serviceOrg);
			record.setOrgId(result.getId());
		} else {
			record.setOrgId(org.getId());
		}
		// 填入销售字段
		record.setSalerId(staff.getId());
		record.setServiceSaler(staff.getStaffName());

		if (labels != null) {
			// 先去重,冗余mark字段
			removeDuplicate(labels);
			StringBuffer sb = new StringBuffer();
			for (String labelName : labels) {
				sb.append(labelName + Constants.LABEL_SEPERATOR);
			}
			// String mark = sb.toString();
			record.setMark(sb.substring(0, sb.length() - 1));
		}

		// if (StringUtils.isNotEmpty(record.getOrgId())) {
		// TyServiceorg temp = tyServiceorgService.queryById(record.getOrgId());
		// record.setOrgName(temp == null ? "" : temp.getOrgName());
		// }
		// if (StringUtils.isNotEmpty(record.getSalerId())) {
		// TyStafflist staff = tyStafflistService.queryById(record.getSalerId());
		// record.setServiceSaler(staff == null ? "" : staff.getStaffName());
		// }
		record.setDeleteFlag("0");
		TyOrgcustomer newRecord = tyOrgcustomerService.add(record);

		// 更新客户标签
		Set<String> labelSet = new LinkedHashSet<String>();
		if (labels != null)
			labelSet.addAll(labels);
		updateLabels(labelSet, newRecord.getId());

		return setSuccessModelMap(modelMap);
	}

	private TyServiceorgBean queryOrgByNameLike(String name) {
		Map<String, Object> orgParams = new HashMap<String, Object>();
		orgParams.put("orgName", name);
		List<TyServiceorgBean> orgList = tyServiceorgService.queryByCondition(orgParams);
		if (orgList != null && orgList.size() > 0) {
			return orgList.get(0);
		} else {
			orgParams.clear();
			orgParams.put("orgSimpleName", name);
			orgList = tyServiceorgService.queryByCondition(orgParams);
			if (orgList != null && orgList.size() > 0) {
				return orgList.get(0);
			} else {
				return null;
			}
		}
	}

	// 批量导入客户
	@ApiOperation(value = "批量导入客户")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
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
		int successNum = importCustExcel(file, list);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("errorLogList", list);
		m.put("successNum", successNum);
		if (successNum == 0) {
			return setModelMap(modelMap, HttpCode.BAD_REQUEST, m);
		}
		return setSuccessModelMap(modelMap, m);
	}

	// 批量导出客户
	@ApiOperation(value = "批量导出客户")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/batchExport", method = RequestMethod.GET)
	public Object batchExport(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", WebUtil.getCurrentUser());
		// List<TyOrgcustomerBean> list = new ArrayList<TyOrgcustomerBean>();
		// List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(WebUtil.getCurrentUser());
		// 销售范围
		// if(tyStafflistService.isSeller(userrole) || tyStafflistService.isSeniorseller(userrole)){
		// geneDataScope(params);
		// params.put("deleteFlag", "0");
		// list = getAllOrgcustomesWithMarks(params);
		// list = tyOrgcustomerService.queryByCondition(params);
		// } else {
		// if(tyStafflistService.isSysmng(userrole)){
		// params.clear();
		// 带上参数进行导出操作
		// list = getAllOrgcustomesWithMarks(params);
		// list = tyOrgcustomerService.queryByCondition(params);
		// }
		// }
		// 标签计算、销售范围在存储过程中已经计算完成
		params.put("deleteFlag", "0");
		List<TyOrgcustomerBean> list = getAllOrgcustomesWithMarks(params);
		Map<String, String> isAcceptRptMap = sysDicService.queryDicByDicIndexKey("ISACCEPTRPT");
		for (TyOrgcustomerBean singleBean : list) {
			singleBean.setIsAcceptRptText(isAcceptRptMap.get(singleBean.getIsAcceptRpt()));
		}

		int successNum = exportCustExcel(response, list);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("successNum", successNum);
		return setSuccessModelMap(modelMap, m);
	}

	private List<TyOrgcustomerBean> getAllOrgcustomesWithMarks(Map<String, Object> params) {
		// List<TyOrgcustomerBean> list = new ArrayList<>();
		// if(params.get("marks") != null && StringUtils.isNotBlank((CharSequence) params.get("marks"))){
		// 设置输出参数为游标类型
		// params.put("customerList",OracleTypes.CURSOR);
		// params.put("pageSize", 1000);//采用1000为基础查询单位
		// int pageNum = 1;
		// // 不断翻页处理
		// while (true) {
		// params.put("pageNum", pageNum);
		// List<TyOrgcustomerBean> list1 = tyOrgcustomerService.queryCustomerWithMarksByProc(params).getList();
		// list.addAll(list1);
		// if (list1.size() < 1000) {
		// break;
		// }
		// pageNum++;
		// }
		// }
		// }else{
		// //转换标签查询条件
		// transMarks(params);
		// list = tyOrgcustomerService.queryByCondition(params);
		// }
		List<TyOrgcustomerBean> list = tyOrgcustomerService.queryByConditionFromProcResult(params);
		return list;
	}

	// 修改机构客户
	@ApiOperation(value = "修改机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "labels[]", required = false) List<String> labels) {
		TyOrgcustomer record = Request2ModelUtil.covert(TyOrgcustomer.class, request);

		if (!InputValidatorUtil.isEmail(record.getCustEmail())) {
			throw new BusinessException("邮箱格式错误，请重新输入");
		}

		if (!InputValidatorUtil.is263Email(record.getCustEmail())) {
			throw new BusinessException("邮箱格式错误：" + InputValidatorUtil.ERROR_263_INFO);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");

		// record.setCustTel(StringUtils.remove(record.getCustTel(), "-"));
		record.setCustMobile(StringUtils.remove(record.getCustMobile(), "-"));
		if (StringUtils.isNotBlank(record.getCustMobile())) {
			params.put("custMobile", record.getCustMobile());
			custlist = tyOrgcustomerService.queryByCondition(params);
			if (custlist != null && custlist.size() > 0) {
				if (custlist.size() == 1) {
					if (!(record.getId().equals(custlist.get(0).getId()))) {
						throw new BusinessException("手机号已存在");
					}
				} else {
					throw new BusinessException("手机号已存在");
				}
			}
		} else {
			throw new BusinessException("请输入手机号");
		}

		if (labels != null) {
			// 先去重,冗余mark字段
			removeDuplicate(labels);
			StringBuffer sb = new StringBuffer();
			for (String labelName : labels) {
				sb.append(labelName + Constants.LABEL_SEPERATOR);
			}
			// String mark = sb.toString();
			record.setMark(sb.substring(0, sb.length() - 1));
		} else {
			record.setMark("");
			tyCustomerlabelService.deleteByCustId(record.getId());
		}
		tyOrgcustomerService.update(record);
		// tyCustomerlabelService.deleteByCustId(record.getId());
		// 更新客户标签
		Set<String> labelSet = new LinkedHashSet<String>();
		if (labels != null)
			labelSet.addAll(labels);
		updateLabels(labelSet, record.getId());

		// TODO 调用推送修改接口
		TyOrgcustomer cust = tyOrgcustomerService.queryById(record.getId());
		// try {
		// tyOrgcustomerService.update31Cust(cust);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return setSuccessModelMap(modelMap, record);
	}

	// 删除机构客户
	@ApiOperation(value = "删除机构客户")
	// @RequiresPermissions("ty.tyOrgcustomer.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyOrgcustomer record = tyOrgcustomerService.queryById(id);
		if (record != null) {
			record.setCustName(record.getCustName() + "-d");
			tyOrgcustomerService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}

	// 发送短信邮件 sms短信，email邮件
	@ApiOperation(value = "发送短信邮件")
	// @RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/getAlllEmailList", method = RequestMethod.POST)
	public Object getAlllEmailList(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "groupId", required = false) String groupId) {

		List<String> emailList = new ArrayList<String>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		params.put("pageSize", "100000");
		// 转换标签查询条件
		// transMarks(params);

		// if (StringUtils.isNotBlank(groupId)) {
		// params.put("groupId", groupId);
		// custlist = tyOrgcustomerService.queryByCondition(params);
		// } else {
		// 添加查询范围条件
		// geneDataScope(params);
		// custlist = tyOrgcustomerService.queryByConditionFromProcResult(params);
		// }
		params.put("userId", WebUtil.getCurrentUser());
		custlist = tyOrgcustomerService.queryByConditionFromProcResult(params);

		for (TyOrgcustomerBean customer : custlist) {
			if (StringUtils.isNoneBlank(customer.getCustEmail())) {
				emailList.add(customer.getCustEmail());
			}
		}
		return setSuccessModelMap(modelMap, emailList);
	}

	// 发送短信邮件 sms短信，email邮件
	@ApiOperation(value = "发送短信邮件")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public Object sendMsg(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "custListStr", required = false) String custListStr, @RequestParam(value = "msgType", required = false) String msgType, @RequestParam(value = "msgContent", required = false) String msgContent, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "isAll", required = false) boolean isAll, @RequestParam(value = "groupId", required = false) String groupId, @RequestParam(value = "file", required = false) List<MultipartFile> multiUploadFiles) {
		String fileNames = "";
		String filePaths = "";
		String redirectUrl = "";

		List<String> arrayList = new ArrayList<String>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		params.put("pageSize", "100000");

		// 转换标签查询条件
		// transMarks(params);

		if (isAll) {
			// if (StringUtils.isNotBlank(groupId)) {
			// params.put("groupId", groupId);
			// custlist = tyOrgcustomerService.queryByCondition(params);
			// } else {
			// // 添加查询范围条件
			// geneDataScope(params);
			//
			// custlist = tyOrgcustomerService.queryByCondition(params);
			// }
			custlist = tyOrgcustomerService.queryByConditionFromProcResult(params);
		} else {
			if (custListStr != null && StringUtils.isNotBlank(custListStr)) {
				String[] array = custListStr.split(",");
				for (String string : array) {
					arrayList.add(string);
				}
				// params.put("custidList", arrayList);

				List<String> tempList = new ArrayList<String>();
				for (int i = 0; i < arrayList.size(); i++) {
					tempList.add(arrayList.get(i));
					if (i % 500 == 0) {
						params.put("custidList", tempList);
						custlist.addAll(tyOrgcustomerService.queryByCondition(params));
						tempList.clear();
					}
					if (i == arrayList.size() - 1 && tempList.size() != 0) {
						params.put("custidList", tempList);
						custlist.addAll(tyOrgcustomerService.queryByCondition(params));
						tempList.clear();
					}
				}
			} else {

			}
		}

		if (custlist != null && custlist.size() > 0) {
			TyMessageaudit messageAudit = new TyMessageaudit();
			messageAudit.setMsgType(msgType);
			messageAudit.setMsgContent(msgContent);
			messageAudit.setTitle(title == null ? "-" : title);
			messageAudit.setAuditFlag("0");

			TyMessageaudit auditResult = tyMessageauditService.add(messageAudit);

			if (CollectionUtils.isNotEmpty(multiUploadFiles)) {
				for (Iterator iterator = multiUploadFiles.iterator(); iterator.hasNext();) {
					MultipartFile multipartFile = (MultipartFile) iterator.next();
					String fileName = multipartFile.getOriginalFilename();
					fileNames = fileNames + fileName + ",";
					String rootFilePath = HtmlUtils.getMailAttachFullFilePath() + File.separator + auditResult.getId();
					String relatFilePath = HtmlUtils.getRelativeMailAttachUrlPath();
					relatFilePath = relatFilePath + "/" + auditResult.getId() + "/" + fileName;
					filePaths = filePaths + relatFilePath + ",";
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
				}
				fileNames = fileNames.substring(0, fileNames.length() - 1);
				filePaths = filePaths.substring(0, filePaths.length() - 1);
				auditResult.setFileName(fileNames);
				auditResult.setFilePath(filePaths);
				tyMessageauditService.update(auditResult);
			}

			List<TyMessagelog> mlog = new ArrayList<TyMessagelog>();
			StringBuffer telStrs = new StringBuffer();
			for (TyOrgcustomerBean tyOrgcustomer : custlist) {

				telStrs.append(tyOrgcustomer.getCustMobile()).append(",");
			}
			// tyMessagelogService.batchAdd(mlog);
			// 2018-01-22 短信内容和手机号列表同步到研究管理平台
			if (StringUtils.equals(msgType, "sms")) {
				String requestid = sendSmsMsgToTYPlat(auditResult.getId(), messageAudit.getMsgContent(), telStrs.substring(0, telStrs.length() - 1), currUser.getAccount());
				if (StringUtils.isBlank(requestid)) {
					throw new BusinessException("短信内容推送到研究管理平台失败");
				}
				// http://192.168.41.108:8888/tfzqkm/workflow/request/workflow.jsp?requestid=402981e95c443fdc015c4441aedc0002&fd_receive_login=17a9e5a99bf613f62c9b6f97ef16503091b14c012cb6f9b6
				String host = PropertiesUtil.getString("typlat.host");
				String auditPageUrl = PropertiesUtil.getString("typlat.auditPageUrl");
				String prefix = PropertiesUtil.getString("typlat.dataprefix");
				String key = PropertiesUtil.getString("typlat.desKey");

				try {
					DESPlus des = new DESPlus(key + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					String fd_receive_login = des.encrypt(prefix + currUser.getAccount());
					// 自定义密钥
					auditPageUrl = auditPageUrl + "?requestid=" + requestid + "&fd_receive_login=" + fd_receive_login;
					redirectUrl = host + auditPageUrl;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new IllegalParameterException("生成跳转URL错误");
				}
			}
		}
		return setSuccessModelMap(modelMap, redirectUrl);
	}

	// 发送活动的短信 sms短信
	@ApiOperation(value = "发送活动短信邮件")
	// @RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/sendActMsg", method = RequestMethod.POST)
	public Object sendActMsg(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "activityId", required = false) String activityId, @RequestParam(value = "custListStr", required = false) String custListStr, @RequestParam(value = "msgType", required = false) String msgType, @RequestParam(value = "msgContent", required = false) String msgContent, @RequestParam(value = "title", required = false) String title/*
																																																																																																											 * ,
																																																																																																											 * 
																																																																																																											 * @RequestParam(value = "isAll", required = false) boolean isAll,
																																																																																																											 * 
																																																																																																											 * @RequestParam(value = "groupId", required = false) String groupId,
																																																																																																											 * 
																																																																																																											 * @RequestParam(value = "file", required = false) List<MultipartFile> multiUploadFiles
																																																																																																											 */) {
		Map<String, Object> data = new HashMap<>();

		List<String> arrayList = new ArrayList<String>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		params.put("pageSize", "100000");
		if (custListStr != null && StringUtils.isNotBlank(custListStr)) {
			String[] array = custListStr.split(",");
			for (String string : array) {
				arrayList.add(string);
			}
			// params.put("custidList", arrayList);

			List<String> tempList = new ArrayList<String>();
			for (int i = 0; i < arrayList.size(); i++) {
				tempList.add(arrayList.get(i));
				if (i % 500 == 0) {
					params.put("custidList", tempList);
					custlist.addAll(tyOrgcustomerService.queryByCondition(params));
					tempList.clear();
				}
				if (i == arrayList.size() - 1 && tempList.size() != 0) {
					params.put("custidList", tempList);
					custlist.addAll(tyOrgcustomerService.queryByCondition(params));
					tempList.clear();
				}
			}
		} else {

		}

		if (custlist != null && custlist.size() > 0) {
			// 两种类型的短信消息，一种是活动的邀请信息，一种是群发的普通信息
			if (StringUtils.isNotBlank(activityId)) {
				TyActivity activity = tyActivityService.queryById(activityId);
				if (activity == null) {
					throw new BusinessException("活动不存在，请检查参数！");
				}

				// 查询对应的活动的发送消息记录，
				Map<String, Object> params1 = new HashMap<>();
				params1.put("activityCust", activityId);
				List<TyMessagelogBean> sendMsglogList = tyMessagelogService.queryByCondition(params1);
				Set<String> sendCustIds = getCustIdsFromList(sendMsglogList);

				for (TyOrgcustomerBean tyOrgcustomer : custlist) {
					// 判断是否已发送过,没有发送过的才进行发送
					if (!sendCustIds.contains(tyOrgcustomer.getId())) {
						TyMessagelog messagelog = new TyMessagelog();
						String key = messagelog.getClass().getSimpleName();
						messagelog.setId(tyMessagelogService.createId(key));
						messagelog.setMsgType(msgType);
						try {
							msgContent = replaceActContent(msgContent, tyOrgcustomer, activity);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						messagelog.setMsgContent(msgContent);
						messagelog.setTitle(title == null ? "-" : title);
						messagelog.setReceiver(tyOrgcustomer.getId());
						messagelog.setReceiverName(tyOrgcustomer.getCustName());
						messagelog.setReceiverTel(tyOrgcustomer.getCustMobile());
						messagelog.setReceiverEmail(tyOrgcustomer.getCustEmail());
						messagelog.setSendFlag("0");
						messagelog.setSendDate(DateUtil.format(new Date()));
						messagelog.setCreateBy(currUser.getId());
						messagelog.setCreateTime(new Date());
						messagelog.setUpdateBy(currUser.getId());
						messagelog.setUpdateTime(new Date());
						messagelog.setRemark("待发送");
						messagelog.setActivityCust(activityId); // 客户活动短信发送标记
						// messagelog.setMsgAuditid(auditResult.getId());
						tyMessagelogService.add(messagelog);
						// mlog.add(messagelog);
					}
				}

			} else { // 普通的消息
				for (TyOrgcustomerBean tyOrgcustomer : custlist) {
					TyMessagelog messagelog = new TyMessagelog();
					String key = messagelog.getClass().getSimpleName();
					messagelog.setId(tyMessagelogService.createId(key));
					messagelog.setMsgType(msgType);
					msgContent = replaceContent(msgContent, tyOrgcustomer);
					messagelog.setMsgContent(msgContent);
					messagelog.setTitle(title == null ? "-" : title);
					messagelog.setReceiver(tyOrgcustomer.getId());
					messagelog.setReceiverName(tyOrgcustomer.getCustName());
					messagelog.setReceiverTel(tyOrgcustomer.getCustMobile());
					messagelog.setReceiverEmail(tyOrgcustomer.getCustEmail());
					messagelog.setSendFlag("0");
					messagelog.setSendDate(DateUtil.format(new Date()));
					messagelog.setCreateBy(currUser.getId());
					messagelog.setCreateTime(new Date());
					messagelog.setUpdateBy(currUser.getId());
					messagelog.setUpdateTime(new Date());
					messagelog.setRemark("待发送");
					tyMessagelogService.add(messagelog);
				}

			}
		}
		return setSuccessModelMap(modelMap, data);
	}

	private Set<String> getCustIdsFromList(List<TyMessagelogBean> sendMsglogList) {
		Set<String> custIds = new HashSet<>();
		if (sendMsglogList.size() > 0) {
			for (TyMessagelogBean tyMessagelogBean : sendMsglogList) {
				String custId = tyMessagelogBean.getReceiver();
				if (StringUtils.isNotBlank(custId)) {
					custIds.add(custId);
				}
			}
		}
		return custIds;
	}

	private String replaceActContent(String content, TyOrgcustomerBean tyOrgcustomer, TyActivity activity) throws ParseException {
		content = content.replace("#姓名#", tyOrgcustomer.getCustName());
		if (StringUtils.isNotBlank(activity.getTitle())) {
			content = content.replace("#活动名称#", activity.getTitle());
		}
		if (StringUtils.isNotBlank(activity.getDetailAddr())) {
			content = content.replace("#活动地点#", activity.getDetailAddr());
		}
		if (StringUtils.isNotBlank(activity.getActivityType())) {
			Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
			content = content.replace("#活动类型#", activitytypeMap.get(activity.getActivityType().toString()));
		}
		if (activity.getBeginDate() != null && activity.getBeginTime() != null) {
			String beginTimeStr = activity.getBeginTime().toString();
			if (beginTimeStr.length() < 6) {
				beginTimeStr = StringUtils.leftPad(beginTimeStr, 6, "0"); // 左侧补齐
			}
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(activity.getBeginDate().toString() + beginTimeStr);
			content = content.replace("#活动时间#", DateUtil.format(date, DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		}
		return content;
	}

	/**
	 * 把短信发送到投研平台做审核
	 * 
	 * @param id
	 * @param msgContent
	 * @param receivers
	 * @param msgSubmiter
	 * @return
	 */
	private String sendSmsMsgToTYPlat(String id, String msgContent, String receivers, String msgSubmiter) {
		MsgSendFlow msgSendFlow = new MsgSendFlow();
		msgSendFlow.setWs_wpd(PropertiesUtil.getString("typlat.servicePass"));
		msgSendFlow.setRs_id(id);
		msgSendFlow.setMsg_creater(msgSubmiter);
		msgSendFlow.setReceive_nums(receivers);
		msgSendFlow.setMsg_content(msgContent);

		try {
			String host = PropertiesUtil.getString("typlat.host");
			String createAuditMsgUrl = host + PropertiesUtil.getString("typlat.createAuditMsg");
			MsgSendFlowClient sendClient = new MsgSendFlowClient();
			msgSendFlow = sendClient.sendMsg(createAuditMsgUrl, msgSendFlow);
			// -1 异常；-2 WS_PWD 密码不对
			logger.info("msgSendFlow>>>>>>>>>>>>rs_value=" + msgSendFlow.getRs_value() + "Requestid=" + msgSendFlow.getRequestid());
			return msgSendFlow.getRequestid();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("");
			return null;
		}
	}

	@RequestMapping(value = "/maildown", method = RequestMethod.GET)
	@ApiOperation(value = "下载文件")
	public String downloadFile(String filePath, String fileName, HttpServletResponse response, ModelMap modelMap) throws Exception {
		filePath = PropertiesUtil.getString("ty.file.rootdir") + filePath;
		try {
			byte[] content = FileUtils.readFileToByteArray(new File(filePath));
			OutputStream os = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8"));
			os.write(content);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	private int importCustExcel(File excel, List<String> list) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean isMobileExists = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());

			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
			List<TyServiceorg> allOrgs = tyServiceorgService.getAllRecords();
			Map<String, TyServiceorg> orgMap = new HashMap<String, TyServiceorg>();
			for (TyServiceorg org : allOrgs) {
				orgMap.put(org.getOrgName(), org);
			}
			List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
			Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
			for (TyStafflist stf : allStaffs) {
				staffMap.put(stf.getId(), stf);
			}
			// 查询所有的研究行业数据
			Map<String, String> allIndustry = sysDicService.queryDicByDicIndexKey("INDUSTRY");
			// 查询是否接受研报字典数据
			Map<String, String> acceptRptMap = sysDicService.queryDicByDicIndexKey("ISACCEPTRPT");
			Map<String, String> isAcceptRptMap = new HashMap<String, String>();
			for (String key : acceptRptMap.keySet()) {
				isAcceptRptMap.put(acceptRptMap.get(key), key);
			}
			// 查询所有的报告发送组数据
			Map<String, String> allRptSendGroup = sysDicService.queryDicByDicIndexKey("RPTSENDGROUP");

			// List<TyOrgcustomer> = tyOrgcustomerService.getAllRecords();
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						isMobileExists = false;
						currColNum = 0;
						readColFinished = false;
						String cust_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isBlank(cust_name)) {
							continue;
						}

						currColNum = currColNum + 1;
						String cust_mobile = WriteExcelUtil.trimAllBlankCellStringToNull(hssfRow, currColNum);
						Map<String, Object> pa = new HashMap<String, Object>();
						TyOrgcustomer record = new TyOrgcustomer();
						List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
						pa.put("deleteFlag", "0");
						pa.put("orderBy", "id desc");
						if (StringUtils.isNotBlank(cust_mobile)) {
							if (InputValidatorUtil.isMobile(cust_mobile)) {
								cust_mobile = StringUtils.remove(cust_mobile, "-");
								pa.put("custMobile", cust_mobile);
								custlist = tyOrgcustomerService.queryByCondition(pa);
								if (custlist != null && custlist.size() > 0) {
									isMobileExists = true;
									record = custlist.get(0);
									// throw new
									// IllegalParameterException("Excel文件单元格第"+(rowNum+1)+"行,第"+(currColNum+1)+"手机号已存在;");
								}
							} else {
								throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,手机号不符合规范:大陆手机号11位,香港手机号格式为[00852-11111111]");

							}
						} else {
							throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,手机号为空");
						}

						currColNum = currColNum + 1;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String org_id = null;
						currColNum = currColNum + 1;
						String service_saler = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String saler_id = null;
						TyServiceorg orgBean = orgMap.get(org_name);
						if (orgBean != null) {
							org_id = orgBean.getId();
							String salerIdStr = orgBean.getSalerId();
							String[] salerIdArray = salerIdStr.split(",");
							Boolean ismatch = false;
							for (String sid : salerIdArray) {
								TyStafflist staff = staffMap.get(sid);// tyStafflistService.queryById(s);
								if (staff.getStaffName().equals(service_saler)) {
									saler_id = staff.getId();
									ismatch = true;
									break;
								}
							}
							if (!ismatch) {
								throw new BusinessException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列对口销售名称有误,请手动新增;");
							}
						} else {
							throw new BusinessException("Excel文件单元格第" + (rowNum + 1) + "行,第" + currColNum + "列机构名称有误,请手动新增;");
						}

						currColNum = currColNum + 1;
						String department = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String title = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						currColNum = currColNum + 1;
						String cust_email = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotBlank(cust_email)) {
							if (!InputValidatorUtil.isEmail(cust_email)) {
								throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,邮箱格式不符合规则");
							}

							if (InputValidatorUtil.is263Email(cust_email)) {
								// cust_tel = StringUtils.remove(cust_tel, "-");
							} else {
								throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,邮箱格式不符合规则," + InputValidatorUtil.ERROR_263_INFO);
							}
						}

						currColNum = currColNum + 1;
						String idNo = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						currColNum = currColNum + 1;
						String cust_tel = WriteExcelUtil.trimAllBlankCellStringToNull(hssfRow, currColNum);
						if (!StringUtils.isBlank(cust_tel)) {
							if (InputValidatorUtil.isTel(cust_tel)) {
								// cust_tel = StringUtils.remove(cust_tel, "-");
							} else {
								throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,座机号不符合规则");
							}
						}

						currColNum = currColNum + 1;
						String area = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						currColNum = currColNum + 1;
						String domain = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						currColNum = currColNum + 1;
						String isAcceptRpt = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotEmpty(isAcceptRpt)) {
							isAcceptRpt = isAcceptRptMap.get(isAcceptRpt);
						}

						currColNum = currColNum + 1;
						String rptSendGroupName = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String rptSendGroupNameStr = null;
						Set<String> rptSendGroupNames = new LinkedHashSet<String>();
						if (record.getRptSendGroupName() != null) {
							String[] rptSendGroupNameArr = record.getRptSendGroupName().split(Constants.COMMA_SEPERATOR);
							for (String ind : rptSendGroupNameArr) {
								rptSendGroupNames.add(ind);
							}
						}
						if (StringUtils.isNotBlank(rptSendGroupName)) {
							rptSendGroupName = rptSendGroupName.replaceAll("，", Constants.COMMA_SEPERATOR);// 全角逗号替换为英文逗号
							String[] rptSendGroupNameArr2 = rptSendGroupName.split(",+");// 支持多逗号
							for (String rsgn : rptSendGroupNameArr2) {
								if (allRptSendGroup.containsKey(rsgn)) {
									rptSendGroupNames.add(rsgn);
								} else {
									throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,报告发送组不存在");
								}
							}
							StringBuffer sb = new StringBuffer();
							for (String rsgn : rptSendGroupNames) {
								sb.append(rsgn + Constants.COMMA_SEPERATOR);
							}
							// markStr = sb.toString();
							rptSendGroupNameStr = sb.substring(0, sb.length() - 1);
						}

						currColNum = currColNum + 1;
						String industry = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String industryStr = null;
						Set<String> industries = new LinkedHashSet<String>();
						if (record.getIndustry() != null) {
							String[] industryArr = record.getIndustry().split(Constants.COMMA_SEPERATOR);
							for (String ind : industryArr) {
								industries.add(ind);
							}
						}
						if (StringUtils.isNotBlank(industry)) {
							industry = industry.replaceAll("，", Constants.COMMA_SEPERATOR);// 全角逗号替换为英文逗号
							String[] industryArr2 = industry.split(",+");// 支持多逗号
							for (String ind : industryArr2) {
								if (allIndustry.containsKey(ind)) {
									industries.add(ind);
								} else {
									throw new IllegalParameterException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,研究行业不存在");
								}
							}
							StringBuffer sb = new StringBuffer();
							for (String ind : industries) {
								sb.append(ind + Constants.COMMA_SEPERATOR);
							}
							// markStr = sb.toString();
							industryStr = sb.substring(0, sb.length() - 1);
						}

						currColNum = currColNum + 1;
						String mark = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String markStr = null;
						Set<String> labels = new LinkedHashSet<String>();
						if (record.getMark() != null) {
							String[] labelArray = record.getMark().split(Constants.LABEL_SEPERATOR);
							for (String l : labelArray) {
								labels.add(l);
							}
						}
						if (StringUtils.isNotBlank(mark)) {
							mark = mark.replaceAll("　", Constants.LABEL_SEPERATOR);// 全角空格替换为英文空格
							String[] labelArray2 = mark.split(" +");// 支持多空格
							for (String l : labelArray2) {
								labels.add(l);
							}
							// 先去重,冗余mark字段
							// removeDuplicate(labels);
							StringBuffer sb = new StringBuffer();
							for (String labelName : labels) {
								sb.append(labelName + Constants.LABEL_SEPERATOR);
							}
							// markStr = sb.toString();
							markStr = sb.substring(0, sb.length() - 1);
						}

						TyOrgcustomer newRecord = new TyOrgcustomer();
						readColFinished = true;
						if (isMobileExists) {// 手机号存在，更新
							if (!StringUtils.contains(record.getSalerId(), userId)) {
								throw new BusinessException("Excel文件单元格第" + (rowNum + 1) + "行客户不属于当前登录用户，不允许修改。");
							}

							if (cust_name != null)
								record.setCustName(cust_name);
							if (org_name != null)
								record.setOrgName(org_name);
							if (org_id != null)
								record.setOrgId(org_id);
							// if (saler_id != null)
							// record.setSalerId(saler_id);
							// if (service_saler != null)
							// record.setServiceSaler(service_saler);
							if (department != null)
								record.setDepartment(department);
							if (title != null)
								record.setTitle(title);
							if (cust_mobile != null)
								record.setCustMobile(cust_mobile);
							if (cust_tel != null)
								record.setCustTel(cust_tel);
							if (cust_email != null)
								record.setCustEmail(cust_email);
							if (area != null)
								record.setArea(area);
							if (industryStr != null)
								record.setIndustry(industryStr);
							if (domain != null)
								record.setDomain(domain);
							if (markStr != null)
								record.setMark(markStr);
							if (idNo != null)
								record.setIdNo(idNo);
							if (isAcceptRpt != null)
								record.setIsAcceptRpt(isAcceptRpt);
							if (rptSendGroupNameStr != null)
								record.setRptSendGroupName(rptSendGroupNameStr);
							tyOrgcustomerService.update(record);
							// tyCustomerlabelService.deleteByCustId(record.getId());
							// 更新客户标签
							updateLabels(labels, record.getId());
							newRecord = tyOrgcustomerService.queryById(record.getId());
						} else {// 手机号不存在，新增
							record.setCustName(cust_name);
							record.setOrgName(org_name);
							record.setOrgId(org_id);
							record.setSalerId(saler_id);
							record.setServiceSaler(service_saler);
							record.setDepartment(department);
							record.setTitle(title);
							record.setCustMobile(cust_mobile);
							record.setCustTel(cust_tel);
							record.setCustEmail(cust_email);
							record.setArea(area);
							record.setIndustry(industryStr);
							record.setDomain(domain);
							record.setMark(markStr);
							record.setIdNo(idNo);
							record.setIsAcceptRpt(isAcceptRpt);
							record.setRptSendGroupName(rptSendGroupNameStr);
							record.setDeleteFlag("0");
							newRecord = tyOrgcustomerService.add(record);
							// 更新客户标签
							updateLabels(labels, newRecord.getId());
							newRecord = tyOrgcustomerService.queryById(newRecord.getId());
						}
						successNum++;
						// TODO 调用推送新增客户接口
						// try {
						// tyOrgcustomerService.update31Cust(newRecord);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
					} catch (IllegalParameterException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (BusinessException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						if (readColFinished) {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行数据处理异常;");
						} else {
							list.add("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列,解析异常;");
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
		list.add(0, "成功" + (isMobileExists ? "更新" : "新增") + successNum + "条数据;");
		return successNum;
	}

	private String replaceContent(String content, TyOrgcustomer tyOrgcustomer) {
		return content.replace("#姓名#", tyOrgcustomer.getCustName()).replace("#日期#", DateUtil.formatCN(new Date()));
	}

	private void removeDuplicate(List<String> list) {
		List<String> tempList = new ArrayList<String>();
		for (String i : list) {
			if (!tempList.contains(i)) {
				tempList.add(i);
			}
		}
		list.clear();
		for (String i : tempList) {
			list.add(i);
		}
	}

	private void updateLabels(Set<String> labels, String custId) {
		if (labels != null && labels.size() > 0) {
			List<TyLabel> sublabel = tyLabelService.getAllSubRecords();
			List<TyCustomerlabelBean> oldLabels = tyCustomerlabelService.queryByCustId(custId);
			Set<String> oldLabelIdSet = new HashSet<>();
			for (TyCustomerlabelBean tyCustomerlabelBean : oldLabels) {
				if (!labels.contains(tyCustomerlabelBean.getLabelName())) {
					oldLabelIdSet.add(tyCustomerlabelBean.getId());
				}
			}
			for (String id : oldLabelIdSet) {
				tyCustomerlabelService.deletePhysical(id);
			}
			// 如果标签名称已存在那么添加
			// 如果不存在，先创建该标签到其他分类，然后再添加
			for (String labelName : labels) {
				int isExist = 0;
				for (TyLabel sub : sublabel) {
					if (sub.getLabelName() != null && labelName.equals(sub.getLabelName())) {
						TyCustomerlabel label = new TyCustomerlabel();
						label.setCustomerId(custId);
						label.setLabelName(labelName);
						label.setLabelId(sub.getId());
						tyCustomerlabelService.add(label);
						isExist = 1;
						break;
					}
				}
				if (isExist == 0) {
					TyLabel lb = new TyLabel();
					lb.setLabelName(labelName);
					lb.setCatId(Constants.LABEL_OTHER_CAT_ID);
					lb.setCatName(Constants.LABEL_OTHER_CAT_NAME);
					lb.setLabelLevel("1");
					lb.setPid("0");
					TyLabel newRecord = tyLabelService.add(lb);
					TyCustomerlabel label = new TyCustomerlabel();
					label.setCustomerId(custId);
					label.setLabelId(newRecord.getId());
					tyCustomerlabelService.add(label);
				}
			}
		}
	}

	private int exportCustExcel(HttpServletResponse response, List<TyOrgcustomerBean> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件
		HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
		int currRowNum = 3;
		int currColNum;
		try {
			// 设置字体
			HSSFFont headfont = workbook.createFont();
			headfont.setFontName("黑体");
			// headfont.setFontHeightInPoints((short) 18);// 字体大小
			// 设置样式
			HSSFCellStyle headstyle = workbook.createCellStyle();
			headstyle.setFont(headfont);
			/*
			 * headstyle.setLocked(true); headstyle.setWrapText(true);// 自动换行
			 */
			// 创建第一行
			HSSFRow row0 = sheet.createRow(0);
			// 创建第一列，表名
			HSSFCell fCell = row0.createCell(0);
			fCell.setCellValue(new HSSFRichTextString("机构客户表"));
			fCell.setCellStyle(headstyle);
			// 创建第二行，时间
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell sCell = row1.createCell(0);
			sCell.setCellValue(new HSSFRichTextString(getCurrDate()));
			// 第三行，表头
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeight((short) 750);
			HSSFCell tCell = row2.createCell(0);
			tCell.setCellValue(new HSSFRichTextString("客户姓名"));
			tCell = row2.createCell(1);
			tCell.setCellValue(new HSSFRichTextString("所属机构"));
			tCell = row2.createCell(2);
			tCell.setCellValue(new HSSFRichTextString("对口销售"));
			tCell = row2.createCell(3);
			tCell.setCellValue(new HSSFRichTextString("职位"));
			tCell = row2.createCell(4);
			tCell.setCellValue(new HSSFRichTextString("研究行业"));
			tCell = row2.createCell(5);
			tCell.setCellValue(new HSSFRichTextString("手机号"));
			tCell = row2.createCell(6);
			tCell.setCellValue(new HSSFRichTextString("座机号"));
			tCell = row2.createCell(7);
			tCell.setCellValue(new HSSFRichTextString("邮箱"));
			tCell = row2.createCell(8);
			tCell.setCellValue(new HSSFRichTextString("所在地"));
			tCell = row2.createCell(9);
			tCell.setCellValue(new HSSFRichTextString("是否接受研报"));
			tCell = row2.createCell(10);
			tCell.setCellValue(new HSSFRichTextString("报告发送组"));
			tCell = row2.createCell(11);
			tCell.setCellValue(new HSSFRichTextString("标签"));
			for (TyOrgcustomerBean singleBean : list) {
				currColNum = 0;
				// 创建行数据
				HSSFRow row = sheet.createRow(currRowNum);
				// 创建第一列数据：客户姓名
				HSSFCell cell0 = row.createCell(currColNum);
				cell0.setCellValue(singleBean.getCustName());
				// 创建第二列数据：所属机构
				currColNum = currColNum + 1;
				HSSFCell cell1 = row.createCell(currColNum);
				cell1.setCellValue(singleBean.getOrgName());
				// 创建第三列数据：对口销售
				currColNum = currColNum + 1;
				HSSFCell cell2 = row.createCell(currColNum);
				cell2.setCellValue(singleBean.getServiceSaler());
				// 创建第四列数据：职位
				currColNum = currColNum + 1;
				HSSFCell cell3 = row.createCell(currColNum);
				cell3.setCellValue(singleBean.getTitle());
				// 创建第五列数据：研究行业
				currColNum = currColNum + 1;
				HSSFCell cell4 = row.createCell(currColNum);
				cell4.setCellValue(singleBean.getIndustry());
				// 创建第六列数据：手机号
				currColNum = currColNum + 1;
				HSSFCell cell5 = row.createCell(currColNum);
				cell5.setCellValue(singleBean.getCustMobile());
				// 创建第七列数据：座机号
				currColNum = currColNum + 1;
				HSSFCell cell6 = row.createCell(currColNum);
				cell6.setCellValue(singleBean.getCustTel());
				// 创建第八列数据：邮箱
				currColNum = currColNum + 1;
				HSSFCell cell7 = row.createCell(currColNum);
				cell7.setCellValue(singleBean.getCustEmail());
				// 创建第九列数据：所在地
				currColNum = currColNum + 1;
				HSSFCell cell8 = row.createCell(currColNum);
				cell8.setCellValue(singleBean.getArea());
				// 创建第十列数据：是否接受研报
				currColNum = currColNum + 1;
				HSSFCell cell9 = row.createCell(currColNum);
				cell9.setCellValue(singleBean.getIsAcceptRptText());
				// 创建第十一列数据：报告发送组
				currColNum = currColNum + 1;
				HSSFCell cell10 = row.createCell(currColNum);
				cell10.setCellValue(singleBean.getRptSendGroupName());
				// 创建第十二列数据：标签
				currColNum = currColNum + 1;
				HSSFCell cell11 = row.createCell(currColNum);
				cell11.setCellValue(singleBean.getMark());
				currRowNum++;
			}
			String fileName = "cust.xls";
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalParameterException("解析Excel文件内容失败");
		} finally {
			try {
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalParameterException("解析Excel文件内容失败");
			}
		}
		return 1;
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
	 * 获取当前时间
	 */
	private String getCurrDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		return df.format(d);
	}

	// 驼峰命名转下划线命名
	private String camelToUnderline(String str) {
		if (str == null || str.trim().isEmpty()) {
			return "";
		}

		int len = str.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
