package com.tfzq.web;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysParam;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysParamService;
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
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyServiceorglabelService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyServiceorglabel;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.model.ty.TyServiceorglabelBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.excel.WriteExcelUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 机构控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "机构管理", description = "机构管理")
@RequestMapping(value = "ty/tyServiceorg", method = RequestMethod.POST)
public class TyServiceorgController extends BaseController {
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyServiceorglabelService  tyServiceorglabelService;
	@Autowired
	private TyLabelService tyLabelService;

	// 查询机构
	@ApiOperation(value = "查询机构")
	@RequiresPermissions("ty.tyServiceorg.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		
		
		if(StringUtils.isNotBlank(request.getParameter("orderBy"))){
			String orderString = camelToUnderline(request.getParameter("orderBy")) +" "+ ( StringUtils.isBlank(request.getParameter("rule")) ? "desc" : request.getParameter("rule"));
			params.put("orderBy",  orderString +" , id desc");
		}else{
			params.put("orderBy", "create_time desc, id desc");
		}
		
		//增加白名单到期日查询条件
		geneWhiteDeadLine(params);

		//添加查询范围条件
		geneDataScope(params);

		PageInfo<TyServiceorgBean> list = tyServiceorgService.queryBeans(params);
		List<TyServiceorgBean> orgList = tyServiceorgService.queryByCondition(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
		Map<String, String> areaCodeMap = sysDicService.queryDicByDicIndexKey("AREACODE");
		addDictFromMap(dicts, "AreaCode", areaCodeMap);
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);

		if (orgList != null && orgList.size() > 0) {
			addDictFromModel(dicts, "Serviceorgs", orgList, "id", "orgName");
		}
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts, "TyStafflists", tyStafflists, "id", "staffName");

		// 添加字典翻译
		if (list.getList().size() > 0) {
			for (TyServiceorgBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getOrgLevel())) {
					singleBean.setOrgLevelName(orglevelMap.get(singleBean.getOrgLevel().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustStatus())) {
					singleBean.setCustStatusName(custStatusMap.get(singleBean.getCustStatus().toString()));
					//已签约机构不显示白名单日期
					singleBean.setWhiteDeadline(StringUtils.equals(singleBean.getCustStatus(), "3") ? null : singleBean.getWhiteDeadline());
				}
				if (StringUtils.isNotEmpty(singleBean.getCustCat())) {
					singleBean.setCustCatName(custCatMap.get(singleBean.getCustCat().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}


	/**
	 * 根据不同的角色设置不同的客户数据查询范围
	 * 
	 * @param params
	 */
	private void geneDataScope(Map<String, Object> params) {
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		// 已有角色
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(currUser.getId());
		if (tyStafflistService.isSeniorseller(userrole)) {// 如果是高级销售
			// TODO 先在staffList表里加入所属团队字段
			// 1 找出所有销售职位
			List<String> roleList = new ArrayList<String>();
			roleList.add(Constants.ROLE_SENIORSELLER);
			roleList.add(Constants.ROLE_SELLER);
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
			params3.put("pageSize", 1000);

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
			TyStafflistBean selfStaff = tyStafflistService.queryByUserId(currUser.getAccount());
			params.put("salerId", selfStaff.getId()); // 设置条件
		}
	}

	// 详细信息
	@ApiOperation(value = "机构详情")
	//@RequiresPermissions("ty.tyServiceorg.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyServiceorgBean tyServiceorgBean = new TyServiceorgBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyServiceorg record = tyServiceorgService.queryById(id);
			List<TyServiceorglabelBean> labelBeans = tyServiceorglabelService.queryByOrgId(record.getId());
			StringBuffer sb = new StringBuffer();
			if (labelBeans!=null && labelBeans.size() > 0) {
				for (TyServiceorglabelBean labelBean : labelBeans) {
					if (labelBean.getLabelId() != null) {
						TyLabel tl = tyLabelService.queryById(labelBean.getLabelId());
						sb.append(tl.getLabelName() + Constants.LABEL_SEPERATOR);
					}
				}
				String mark = sb.toString();
				record.setMark(mark.substring(0, mark.length() - 1));
				tyServiceorgService.update(record);
			}
			try {
				BeanUtils.copyProperties(tyServiceorgBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyServiceorgBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		if (null != tyStafflists && tyStafflists.size() > 0) {
			for (TyStafflist tyStaff : tyStafflists) {
				if (sysWorkAreas.size() > 0 && tyStaff != null) {
					for (SysArea sysArea : sysWorkAreas) {
						if (tyStaff.getWorkAreaid().equals(sysArea.getId())) {
							String staffName = tyStaff.getStaffName() + "(" + sysArea.getAreaName() + ")";
							tyStaff.setStaffName(staffName);
							break;
						}
					}
				}
			}
		}

		addDictFromModel(dicts, "TyStafflists", tyStafflists, "id", "staffName");
		// List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
		
		List<TyLabelBean> catLabelList = new ArrayList<TyLabelBean>();
		if (tyServiceorgBean != null) {
			if (StringUtils.isNotEmpty(tyServiceorgBean.getOrgLevel())) {
				tyServiceorgBean.setOrgLevelName(orglevelMap.get(tyServiceorgBean.getOrgLevel().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustStatus())) {
				tyServiceorgBean.setCustStatusName(custStatusMap.get(tyServiceorgBean.getCustStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustCat())) {
				tyServiceorgBean.setCustCatName(custCatMap.get(tyServiceorgBean.getCustCat().toString()));
			}
			if (null != tyServiceorgBean.getId()) {

				catLabelList = tyLabelService.getCatListByOrgId(tyServiceorgBean.getId());

				if (catLabelList != null) {
					for (Iterator iterator = catLabelList.iterator(); iterator.hasNext();) {
						TyLabelBean tyLabelBean2 = (TyLabelBean) iterator.next();
						List<TyLabelBean> subList = new ArrayList<TyLabelBean>();
						Map<String, Object> pa = new HashMap<String, Object>();
						pa.put("orgId", tyServiceorgBean.getId());
						pa.put("catId", tyLabelBean2.getId());
						subList = tyLabelService.getOrgSubList(pa);
						if (subList != null) {
							tyLabelBean2.setSubList(subList);
						}
					}
				}
			}
		}
		tyServiceorgBean.setLabelCatList(catLabelList);
		return setSuccessModelMap(modelMap, tyServiceorgBean, dicts);
	}

	// 新增机构
	@ApiOperation(value = "添加机构")
	@RequiresPermissions("ty.tyServiceorg.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labels[]", required = false) List<String> labels) {
		//查询机构白名单自动到期时限：默认值30天
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		SysParam deadlineLimit = sysParamService.queryByKey("ty.serviceorg.whiteDeadLineTimeLimit", user.getCompanyId());
		TyServiceorg record = Request2ModelUtil.covert(TyServiceorg.class, request);
		record.setWhiteDeadline(Integer.parseInt(DateUtil.format(DateUtil.addDate(new Date(), Calendar.DATE, Integer.parseInt(deadlineLimit.getParamValue())),DateUtil.DATE_PATTERN.YYYYMMDD)));

		if(StringUtils.length(record.getOrgName())<=6){
			throw new IllegalParameterException("机构全称长度不应该少于6个。");
		}
		if (StringUtils.isNotEmpty(record.getSalerId())) {
			// TODO 对口销售id以,分隔
			String[] salerArray = record.getSalerId().split(",");
			String staffName = "";
			String areaName = "";
			for (String saler : salerArray) {
				TyStafflist temp = tyStafflistService.queryById(saler);
				staffName = staffName + (temp == null ? "" : temp.getStaffName()) + ",";
				List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
				if (sysWorkAreas.size() > 0 && temp != null) {
					for (SysArea sysArea : sysWorkAreas) {
						if (temp.getWorkAreaid().equals(sysArea.getId())) {
							areaName = areaName + sysArea.getAreaName() + ",";
							break;
						}
					}
				}
			}
			record.setServiceSaler(staffName.substring(0, staffName.length() - 1));
			record.setTeamCat(areaName.substring(0, areaName.length() - 1));
		}
		
		//添加标签
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
		record.setDeleteFlag("0");
		tyServiceorgService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改机构
	@ApiOperation(value = "修改机构")
	@RequiresPermissions("ty.tyServiceorg.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labels[]", required = false) List<String> labels) {
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		SysParam deadlineLimit = sysParamService.queryByKey("ty.serviceorg.whiteDeadLineTimeLimit", user.getCompanyId());
		TyServiceorg record = Request2ModelUtil.covert(TyServiceorg.class, request);
		record.setWhiteDeadline(Integer.parseInt(DateUtil.format(DateUtil.addDate(new Date(), Calendar.DATE, Integer.parseInt(deadlineLimit.getParamValue())),DateUtil.DATE_PATTERN.YYYYMMDD)));
		if(StringUtils.length(record.getOrgName())<=6){
			throw new IllegalParameterException("机构全称长度不应该少于6个。");
		}
		TyServiceorg oldrecord = tyServiceorgService.queryById(record.getId());
		if (StringUtils.isNotEmpty(record.getSalerId())) {
			// TODO 对口销售id以,分隔
			String[] salerArray = record.getSalerId().split(",");
			String staffName = "";
			String areaName = "";
			List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
			for (String saler : salerArray) {
				TyStafflist temp = tyStafflistService.queryById(saler);
				staffName = staffName + (temp == null ? "" : temp.getStaffName()) + ",";
				if (sysWorkAreas.size() > 0 && temp != null) {
					for (SysArea sysArea : sysWorkAreas) {
						if (temp.getWorkAreaid().equals(sysArea.getId())) {
							areaName = areaName + sysArea.getAreaName() + ",";
							break;
						}
					}
				}
			}
			record.setServiceSaler(staffName.substring(0, staffName.length() - 1));
			record.setTeamCat(areaName.substring(0, areaName.length() - 1));
			
			//更新标签
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
				tyServiceorglabelService.deleteByOrgId(record.getId());
			}

			tyServiceorgService.update(record);
			// TyStafflist st = tyStafflistService.queryById(salerArray[0]);
			// 更新该机构下所有的客户的对口销售
			updateCustomer(oldrecord, record);
			//更新机构对应的标签
			Set<String> labelSet = new LinkedHashSet<String>();
			if (labels != null)
				labelSet.addAll(labels);
			updateLabels(labelSet, record.getId());
		}

		return setSuccessModelMap(modelMap, record);
	}

	private void updateCustomer(TyServiceorg oldrecord, TyServiceorg record) {
		if (StringUtils.isBlank(record.getSalerId())) {
			return;
		}
		String[] salerArray = record.getSalerId().split(",");
		TyStafflist st = tyStafflistService.queryById(salerArray[0]);
		// 更新该机构下所有的客户的对口销售
		if (!StringUtils.equals(record.getOrgName(), oldrecord.getOrgName())
				|| !StringUtils.equals(record.getSalerId(), oldrecord.getSalerId())
				|| !StringUtils.equals(record.getOrgLevel(), oldrecord.getOrgLevel())
				|| !StringUtils.equals(record.getCustCat(), oldrecord.getCustCat())) {
			Map<String, Object> pa = new HashMap<String, Object>();
			pa.put("orgId", record.getId());
			List<TyOrgcustomerBean> tbean = tyOrgcustomerService.queryByCondition(pa);
			if (tbean != null) {
				for (TyOrgcustomerBean tyOrgcustomerBean : tbean) {
						// try {
						tyOrgcustomerBean.setOrgName(record.getOrgName());
						tyOrgcustomerBean.setServiceSaler(st.getStaffName());
						tyOrgcustomerBean.setSalerId(st.getId());
						tyOrgcustomerService.update(tyOrgcustomerBean);
						// tyOrgcustomerService.update31Cust(tyOrgcustomerBean);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
				}
			}
		}
	}

	// 删除机构
	@ApiOperation(value = "删除机构")
	@RequiresPermissions("ty.tyServiceorg.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyServiceorg record = tyServiceorgService.queryById(id);
		if (record != null) {
			record.setOrgName(record.getOrgName()+"-d");
			tyServiceorgService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}

	// 批量导入机构
	@ApiOperation(value = "批量导入机构")
	@RequiresPermissions("ty.tyServiceorg.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(WebUtil.getCurrentUser());
		List<String> list = new ArrayList<String>();
		int successNum=0;
		if(tyStafflistService.isSysmng(userrole)){
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
			successNum = importOrgExcel(file, list,userrole);
		}else{
			list.add("您不是系统管理员，无法使用机构批量导入功能。");
		}

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("errorLogList", list);
		m.put("successNum", successNum);
		if(successNum == 0){
			return setModelMap(modelMap, HttpCode.BAD_REQUEST,m);
		}
		return setSuccessModelMap(modelMap, m);
	}

	// 批量导出客户
	@ApiOperation(value = "批量导出客户")
	@RequiresPermissions("ty.tyServiceorg.add")
	@RequestMapping(value = "/batchExport", method = RequestMethod.GET)
	public Object batchExport(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyServiceorgBean> list = new ArrayList<TyServiceorgBean>();
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(WebUtil.getCurrentUser());
		if(tyStafflistService.isSeller(userrole) || tyStafflistService.isSeniorseller(userrole)){
			//params.put("salerId", WebUtil.getCurrentUser());
			geneDataScope(params);
			params.put("deleteFlag", "0");
			list = tyServiceorgService.queryByCondition(params);
		} else {
			if(tyStafflistService.isSysmng(userrole)){				
				List<TyServiceorg> _list = tyServiceorgService.getAllRecords();
				for(TyServiceorg record : _list){
					TyServiceorgBean tyServiceorgBean = new TyServiceorgBean();
					try {
						BeanUtils.copyProperties(tyServiceorgBean, record);
						
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (Exception e) {
						tyServiceorgBean = null;
						e.printStackTrace();
					}
					list.add(tyServiceorgBean);
				}
			}
		}
		int successNum = exportOrgExcel(response,list);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("successNum", successNum);
		return setSuccessModelMap(modelMap, m);
	}

	private int importOrgExcel(File excel, List<String> list,List<SysUserRoleBean> userrole) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean isOrgExists = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());// new
																				// Workbook(is);
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
				staffMap.put(stf.getStaffName(), stf);
			}
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						isOrgExists = false;
						readColFinished = false;
						TyServiceorg  orgBean = null;
						currColNum = 0;
						String org_simple_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 1;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotBlank(org_name)) {
							if(StringUtils.length(org_name)<=6){
								throw new BusinessException("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列机构全称长度不应该少于6个。");
							}
							orgBean = orgMap.get(org_name);
							if (orgBean != null) {
								isOrgExists = true;
							}
						} else {
							continue;
						}

						currColNum = 2;
						String service_saler = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String saler_id = null;
						String team_cat = null;
						if (StringUtils.isNotEmpty(service_saler)) {
							String[] salerArray = service_saler.split(",");
							String staffId = "";
							String areaName = "";
							for (String saler : salerArray) {
								TyStafflist temp = staffMap.get(saler);
								if (temp != null) {
									// TyStafflist temp = staffList.get(0);
									staffId = staffId + (temp == null ? "" : temp.getId()) + ",";
									List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
									if (sysWorkAreas.size() > 0 && temp != null) {
										for (SysArea sysArea : sysWorkAreas) {
											if (temp.getWorkAreaid().equals(sysArea.getId())) {
												areaName = areaName + sysArea.getAreaName() + ",";
												break;
											}
										}
									}
								} else {
									throw new BusinessException(
											"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列对口销售有误,请手动新增;");
								}
							}
							saler_id = staffId.substring(0, staffId.length() - 1);
							team_cat = areaName.substring(0, areaName.length() - 1);
						}
						currColNum = 3;
						String cust_cat_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String cust_cat = transCustCat(cust_cat_str);
						currColNum = 4;
						String cust_status_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String cust_status = transCustStatus(cust_status_str);
						currColNum = 5;
						String org_level_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String org_level = transOrgLevel(org_level_str);
						currColNum = 6;
						String address = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 7;
						String company_mail = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = 8;
						String company_tel = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = 9;
						String contactor_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = 10;
						String contactor_tel = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						//增加标签
						currColNum = 11;
						String mark = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String markStr = null;
						Set<String> labels = new LinkedHashSet<String>();
						if (orgBean!=null && StringUtils.isNotEmpty(orgBean.getMark()) ) {
							String[] labelArray = orgBean.getMark().split(Constants.LABEL_SEPERATOR);
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
						currColNum = 12;
						String source = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						currColNum = 13;
						String remark = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);


						readColFinished = true;
						if (isOrgExists) {
							TyServiceorg record = new TyServiceorg();
							TyServiceorg oldrecord = orgMap.get(org_name);
							BeanUtils.copyProperties(record, oldrecord);
							if(!tyStafflistService.isSysmng(userrole)&&!StringUtils.contains(oldrecord.getSalerId(), userId)){
								throw new BusinessException(
										"Excel文件单元格第" + (rowNum + 1) + "行机构不属于当前登录用户，不允许修改。");
							}
							
							if (org_simple_name != null)
								record.setOrgSimpleName(org_simple_name);
							if (org_name != null)
								record.setOrgName(org_name);
//							if (saler_id != null)
//								record.setSalerId(saler_id);
//							if (service_saler != null)
//								record.setServiceSaler(service_saler);
							if (cust_cat != null)
								record.setCustCat(cust_cat);
							if (cust_status != null)
								record.setCustStatus(cust_status);
							if (org_level != null)
								record.setOrgLevel(org_level);
							if (address != null)
								record.setAddress(address);
							if (company_mail != null)
								record.setCompanyMail(company_mail);
							if (company_tel != null)
								record.setCompanyTel(company_tel);
							if (contactor_name != null)
								record.setContactorName(contactor_name);
							if (contactor_tel != null)
								record.setContactorTel(contactor_tel);
							if (team_cat != null)
								record.setTeamCat(team_cat);
							if(markStr != null)
								record.setMark(markStr);
							if(source != null)
								record.setSource(source);
							if(remark != null)
								record.setRemark(remark);
							tyServiceorgService.update(record);
							updateCustomer(oldrecord, record);
						} else {
							TyServiceorg record = new TyServiceorg();
							record.setOrgSimpleName(org_simple_name);
							record.setOrgName(org_name);
							record.setServiceSaler(service_saler);
							record.setCustCat(cust_cat);
							record.setCustStatus(cust_status);
							record.setOrgLevel(org_level);
							record.setAddress(address);
							record.setCompanyMail(company_mail);
							record.setCompanyTel(company_tel);
							record.setContactorName(contactor_name);
							record.setContactorTel(contactor_tel);
							record.setSalerId(saler_id);
							record.setTeamCat(team_cat);
							record.setDeleteFlag("0");
							record.setMark(markStr);
							record.setSource(source);
							record.setRemark(remark);
							tyServiceorgService.add(record);
						}
						successNum++;
					} catch (IllegalParameterException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (BusinessException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						if(readColFinished){
							list.add("Excel文件单元格第" + (rowNum + 1) + "行数据处理异常;");
						}else{
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
		list.add(0, "成功"+(isOrgExists?"更新":"新增" )+ successNum + "条数据;");
		return successNum;
	}
	
	private int exportOrgExcel(HttpServletResponse response,List<TyServiceorgBean> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件
		HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
		int currRowNum = 3;
		int currColNum;
		Map<String, String> custcatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		Map<String, String> custstatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		try {
			// 设置字体
			HSSFFont headfont = workbook.createFont();
			headfont.setFontName("黑体");
			//headfont.setFontHeightInPoints((short) 18);// 字体大小
			// 设置样式
			HSSFCellStyle headstyle = workbook.createCellStyle();
			headstyle.setFont(headfont);
			/*headstyle.setLocked(true);
			headstyle.setWrapText(true);// 自动换行*/			
			// 创建第一行
			HSSFRow row0 = sheet.createRow(0);
			// 创建第一列，表名
			HSSFCell fCell = row0.createCell(0);
			fCell.setCellValue(new HSSFRichTextString("机构表"));
			fCell.setCellStyle(headstyle);
			// 创建第二行，时间
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell sCell = row1.createCell(0);
			sCell.setCellValue(new HSSFRichTextString(getCurrDate()));
			// 第三行，表头
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeight((short) 750);
			HSSFCell tCell = row2.createCell(0);
			tCell.setCellValue(new HSSFRichTextString("机构名称"));
			tCell = row2.createCell(1);
			tCell.setCellValue(new HSSFRichTextString("机构类型"));
			tCell = row2.createCell(2);
			tCell.setCellValue(new HSSFRichTextString("对口销售"));
			tCell = row2.createCell(3);
			tCell.setCellValue(new HSSFRichTextString("机构级别"));
			tCell = row2.createCell(4);
			tCell.setCellValue(new HSSFRichTextString("机构状态"));
			tCell = row2.createCell(5);
			tCell.setCellValue(new HSSFRichTextString("区域"));
			tCell = row2.createCell(6);
			tCell.setCellValue(new HSSFRichTextString("标签"));
			for (TyServiceorg single : list) {
				currColNum = 0;
				// 创建行数据
				HSSFRow row = sheet.createRow(currRowNum);
				// 创建第一列数据
				HSSFCell cell0 = row.createCell(currColNum);
				cell0.setCellValue(single.getOrgName());
				// 创建第二列数据
				currColNum = currColNum + 1;
				HSSFCell cell1 = row.createCell(currColNum);
				String cust_cat = single.getCustCat()==null?null:custcatMap.get(single.getCustCat());
				cell1.setCellValue(cust_cat);
				// 创建第三列数据
				currColNum = currColNum + 1;
				HSSFCell cell2 = row.createCell(currColNum);
				cell2.setCellValue(single.getServiceSaler());
				// 创建第四列数据
				currColNum = currColNum + 1;
				HSSFCell cell3 = row.createCell(currColNum);
				String org_level = single.getOrgLevel()==null?null:orglevelMap.get(single.getOrgLevel());
				cell3.setCellValue(org_level);
				// 创建第五列数据
				currColNum = currColNum + 1;
				HSSFCell cell4 = row.createCell(currColNum);
				String cust_status = single.getCustStatus()==null?null:custstatusMap.get(single.getCustStatus());
				cell4.setCellValue(cust_status);
				// 创建第六列数据
				currColNum = currColNum + 1;
				HSSFCell cell5 = row.createCell(currColNum);
				cell5.setCellValue(single.getTeamCat());
				//第七列数据 标签
				currColNum = currColNum + 1;
				HSSFCell cell6 = row.createCell(currColNum);
				cell6.setCellValue(single.getMark());
				currRowNum++;
			}
			String fileName = "org.xls";
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
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

	private String transCustCat(String cust_cat_str) {
		if ("保险资管".equals(cust_cat_str)) {
			return "1";
		} else if ("公募基金".equals(cust_cat_str)) {
			return "2";
		} else if ("券商资管".equals(cust_cat_str)) {
			return "3";
		} else if ("券商自营".equals(cust_cat_str)) {
			return "4";
		} else if ("私募基金".equals(cust_cat_str)) {
			return "5";
		} else if ("信托公司".equals(cust_cat_str)) {
			return "6";
		} else if ("其他".equals(cust_cat_str)) {
			return "7";
		} else if ("公募基金子公司".equals(cust_cat_str)) {
			return "14";
		} else if ("财务公司".equals(cust_cat_str)) {
			return "10";
		} else if ("海外投资机构".equals(cust_cat_str)) {
			return "8";
		} else if ("PE/VC".equals(cust_cat_str)) {
			return "11";
		} else if ("大型集团资产管理公司".equals(cust_cat_str)) {
			return "13";
		} else if ("银行".equals(cust_cat_str)) {
			return "9";
		} else if ("证券公司".equals(cust_cat_str)) {
			return "15";
		} else if ("QFII".equals(cust_cat_str)) {
			return "12";
		} else if ("保险公司".equals(cust_cat_str)) {
			return "16";
		}
		return null;
	}

	private String transCustStatus(String cust_status_str) {
		if ("拟签约".equals(cust_status_str)) {
			return "1";
		} else if ("潜在客户".equals(cust_status_str)) {
			return "2";
		} else if ("已签约".equals(cust_status_str)) {
			return "3";
		} else if ("其他".equals(cust_status_str)) {
			return "4";
		}
		return null;
	}

	private String transOrgLevel(String org_level_str) {
		if ("第一梯队".equals(org_level_str)) {
			return "1";
		} else if ("第二梯队".equals(org_level_str)) {
			return "2";
		} else if ("第三梯队".equals(org_level_str)) {
			return "3";
		} else if ("其他".equals(org_level_str)) {
			return "4";
		}
		return null;
	}

	private boolean isDirector(List<SysUserRoleBean> surb) {
		boolean result = false;
		for (Iterator iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if ("selldirector".equals(sysUserRoleBean.getRoleId())) {
				result = true;
				break;
			}
		}
		return result;
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
	private String getCurrDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date d = new Date();
		return df.format(d);
	}
	
	/**
	 * 去除重复标签
	 * @param list
	 */
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
	
	//更新机构标签表
	private void updateLabels(Set<String> labels, String orgId) {
		if (labels != null && labels.size() > 0) {
			List<TyLabel> sublabel = tyLabelService.getAllSubRecords();
			// 如果标签名称已存在那么添加
			// 如果不存在，先创建该标签到其他分类，然后再添加
			List<TyServiceorglabelBean> oldLabels = tyServiceorglabelService.queryByOrgId(orgId);
			Set<String> oldLabelIdSet = new HashSet<>();
			for (TyServiceorglabelBean tyServicelabelBean : oldLabels) {
				if(!labels.contains(tyServicelabelBean.getLabelName())) {
					oldLabelIdSet.add(tyServicelabelBean.getId());
				}
			}
			for (String id : oldLabelIdSet) {
				tyServiceorglabelService.deletePhysical(id);
			}
			for (String labelName : labels) {
				int isExist = 0;
				for (TyLabel sub : sublabel) {
					if (sub.getLabelName() != null && labelName.equals(sub.getLabelName())) {
						TyServiceorglabel label = new TyServiceorglabel();
						label.setOrgId(orgId);
						label.setLabelId(sub.getId());
						tyServiceorglabelService.add(label);
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
					TyServiceorglabel label = new TyServiceorglabel();
					label.setOrgId(orgId);
					label.setLabelId(newRecord.getId());
					tyServiceorglabelService.add(label);
				}
			}
		}
	}
	
	//设置结构白名单到期时间查询条件
	private void geneWhiteDeadLine(Map<String, Object> params) {
		if(params.get("whiteDeadline") != null) {
			Date now = new Date();
			switch(params.get("whiteDeadline").toString()) {
			
				case Constants.SERVICE_ORG_WHITELINE_ONE_WEEK : 
					Date sevenDayLater = DateUtil.addDate(now, Calendar.DATE, 7);
					params.put("whiteDeadlineEnd", Integer.parseInt(DateUtil.format(sevenDayLater, DateUtil.DATE_PATTERN.YYYYMMDD)));
					break;
					
				case Constants.SERVICE_ORG_WHITELINE_HALF_MONTH : 
					Date halfMonth = DateUtil.addDate(now, Calendar.DATE, 15);
					params.put("whiteDeadlineEnd", Integer.parseInt(DateUtil.format(halfMonth, DateUtil.DATE_PATTERN.YYYYMMDD)));
					break;
					
				case Constants.SERVICE_ORG_WHITELINE_OTHER : 
					System.out.println("机构白名单截止日期查询条件不正确");
					break;
			}
		}
	}
	
	
	//驼峰命名转下划线命名
	 private  String camelToUnderline(String str) {  
	        if (str == null || str.trim().isEmpty()){    
	            return "";    
	        }  
	  
	        int len = str.length();    
	        StringBuilder sb = new StringBuilder(len);    
	        for (int i = 0; i < len; i++) {    
	            char c = str.charAt(i);    
	            if (Character.isUpperCase(c)){    
	                sb.append("_");    
	                sb.append(Character.toLowerCase(c));    
	            }else{    
	                sb.append(c);    
	            }    
	        }    
	        return sb.toString();  
	    }  
	

}
