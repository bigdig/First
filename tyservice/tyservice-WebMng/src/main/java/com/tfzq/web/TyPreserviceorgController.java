package com.tfzq.web;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
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
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyPreserviceorgService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyPreserviceorg;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyPreserviceorgBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
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
@Api(value = "潜在机构管理", description = "潜在机构管理")
@RequestMapping(value = "ty/tyPreserviceorg", method = RequestMethod.POST)
public class TyPreserviceorgController extends BaseController {
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private TyPreserviceorgService tyPreserviceorgService;
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

	// 查询机构
	@ApiOperation(value = "查询机构")
	//@RequiresPermissions("ty.tyPreserviceorg.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "create_time desc, id desc");

		//添加查询范围条件
		//geneDataScope(params);

		PageInfo<TyPreserviceorgBean> list = tyPreserviceorgService.queryBeans(params);
		List<TyPreserviceorgBean> orgList = tyPreserviceorgService.queryByCondition(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts, "SysWorkAreas", sysWorkAreas, "id", "areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
		Map<String,String> preporgstatusMap = sysDicService.queryDicByDicIndexKey("PREPORGSTATUS");
		addDictFromMap(dicts,"PREPORGSTATUS", preporgstatusMap);
		Map<String,String> orgExistsMap = sysDicService.queryDicByDicIndexKey("ORGEXISTS");
		addDictFromMap(dicts,"ORGEXISTS", orgExistsMap);

		if (orgList != null && orgList.size() > 0) {
			addDictFromModel(dicts, "Serviceorgs", orgList, "id", "orgName");
		}
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts, "TyStafflists", tyStafflists, "id", "staffName");

		// 添加字典翻译
		if (list.getList().size() > 0) {
			for (TyPreserviceorgBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getOrgLevel())) {
					singleBean.setOrgLevelName(orglevelMap.get(singleBean.getOrgLevel().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustStatus())) {
					singleBean.setCustStatusName(custStatusMap.get(singleBean.getCustStatus().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustCat())) {
					singleBean.setCustCatName(custCatMap.get(singleBean.getCustCat().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getPreporgStatus())) {
					singleBean.setPreporgStatusText(preporgstatusMap.get(singleBean.getPreporgStatus().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getOrgExists())) {
					singleBean.setOrgExistsText(orgExistsMap.get(singleBean.getOrgExists().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}



	// 详细信息
	@ApiOperation(value = "机构详情")
	////@RequiresPermissions("ty.tyPreserviceorg.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyPreserviceorgBean tyPreserviceorgBean = new TyPreserviceorgBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyPreserviceorg record = tyPreserviceorgService.queryById(id);
			try {
				BeanUtils.copyProperties(tyPreserviceorgBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyPreserviceorgBean = null;
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
		Map<String,String> preporgstatusMap = sysDicService.queryDicByDicIndexKey("PREPORGSTATUS");
		addDictFromMap(dicts,"PREPORGSTATUS", preporgstatusMap);
		Map<String,String> orgExistsMap = sysDicService.queryDicByDicIndexKey("ORGEXISTS");
		addDictFromMap(dicts,"ORGEXISTS", orgExistsMap);
		if (tyPreserviceorgBean != null) {
			if (StringUtils.isNotEmpty(tyPreserviceorgBean.getOrgLevel())) {
				tyPreserviceorgBean.setOrgLevelName(orglevelMap.get(tyPreserviceorgBean.getOrgLevel().toString()));
			}
			if (StringUtils.isNotEmpty(tyPreserviceorgBean.getCustStatus())) {
				tyPreserviceorgBean.setCustStatusName(custStatusMap.get(tyPreserviceorgBean.getCustStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyPreserviceorgBean.getCustCat())) {
				tyPreserviceorgBean.setCustCatName(custCatMap.get(tyPreserviceorgBean.getCustCat().toString()));
			}
			if (StringUtils.isNotEmpty(tyPreserviceorgBean.getPreporgStatus())) {
				tyPreserviceorgBean.setPreporgStatusText(preporgstatusMap.get(tyPreserviceorgBean.getPreporgStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyPreserviceorgBean.getOrgExists())) {
				tyPreserviceorgBean.setOrgExistsText(orgExistsMap.get(tyPreserviceorgBean.getOrgExists().toString()));
			}
		}

		return setSuccessModelMap(modelMap, tyPreserviceorgBean, dicts);
	}

	// 新增机构
	@ApiOperation(value = "添加机构")
	//@RequiresPermissions("ty.tyPreserviceorg.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyPreserviceorg record = Request2ModelUtil.covert(TyPreserviceorg.class, request);

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
		record.setDeleteFlag("0");
		tyPreserviceorgService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改机构
	@ApiOperation(value = "修改机构")
	//@RequiresPermissions("ty.tyPreserviceorg.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyPreserviceorg record = Request2ModelUtil.covert(TyPreserviceorg.class, request);
		if(StringUtils.length(record.getOrgName())<=6){
			throw new IllegalParameterException("机构全称长度不应该少于6个。");
		}
		TyPreserviceorg oldrecord = tyPreserviceorgService.queryById(record.getId());
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

			tyPreserviceorgService.update(record);
			// TyStafflist st = tyStafflistService.queryById(salerArray[0]);
			// 更新该机构下所有的客户的对口销售
			//updateCustomer(oldrecord, record);
		}

		return setSuccessModelMap(modelMap, record);
	}

//	private void updateCustomer(TyPreserviceorg oldrecord, TyPreserviceorg record) {
//		if (StringUtils.isBlank(record.getSalerId())) {
//			return;
//		}
//		String[] salerArray = record.getSalerId().split(",");
//		TyStafflist st = tyStafflistService.queryById(salerArray[0]);
//		// 更新该机构下所有的客户的对口销售
//		Map<String, Object> pa = new HashMap<String, Object>();
//		pa.put("orgId", record.getId());
//		List<TyOrgcustomerBean> tbean = tyOrgcustomerService.queryByCondition(pa);
//		if (tbean != null) {
//			for (TyOrgcustomerBean tyOrgcustomerBean : tbean) {
//				if (!StringUtils.equals(record.getOrgName(), oldrecord.getOrgName())
//						|| !StringUtils.equals(record.getSalerId(), oldrecord.getSalerId())
//						|| !StringUtils.equals(record.getOrgLevel(), oldrecord.getOrgLevel())
//						|| !StringUtils.equals(record.getCustCat(), oldrecord.getCustCat())) {
//					// try {
//					tyOrgcustomerBean.setOrgName(record.getOrgName());
//					tyOrgcustomerBean.setServiceSaler(st.getStaffName());
//					tyOrgcustomerBean.setSalerId(st.getId());
//					tyOrgcustomerService.update(tyOrgcustomerBean);
//					// tyOrgcustomerService.update31Cust(tyOrgcustomerBean);
//					// } catch (Exception e) {
//					// e.printStackTrace();
//					// }
//				}
//			}
//		}
//	}

	// 删除机构
	@ApiOperation(value = "删除机构")
	//@RequiresPermissions("ty.tyPreserviceorg.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyPreserviceorg record = tyPreserviceorgService.queryById(id);
		if (record != null) {
			tyPreserviceorgService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}

	
	// 将备选客户保存到客户表
	@ApiOperation(value = "将潜在机构保存到机构表")
	@RequestMapping(value = "/saveOrg", method = RequestMethod.POST)
	public Object saveOrg(HttpServletRequest request, ModelMap modelMap) {
		TyPreserviceorg orgRecord = Request2ModelUtil.covert(TyPreserviceorg.class, request);
		//
		TyServiceorg serviceOrg = new TyServiceorg();
		serviceOrg.setOrgName(orgRecord.getOrgName());
		serviceOrg.setOrgSimpleName(orgRecord.getOrgName());
		serviceOrg.setSalerId(orgRecord.getSalerId());
		if (StringUtils.isNotEmpty(orgRecord.getSalerId())) {
			// TODO 对口销售id以,分隔
			String[] salerArray = orgRecord.getSalerId().split(",");
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
			serviceOrg.setServiceSaler(staffName.substring(0, staffName.length() - 1));
			serviceOrg.setTeamCat(areaName.substring(0, areaName.length() - 1));
		}
		
		serviceOrg.setOrgLevel(orgRecord.getOrgLevel());
		serviceOrg.setCustStatus(orgRecord.getCustStatus());
		serviceOrg.setRecEmail(orgRecord.getRecEmail());
		serviceOrg.setRecSms(orgRecord.getRecSms());
		serviceOrg.setDeleteFlag("0");
		
		List<TyServiceorgBean> orgs = tyServiceorgService.getRecordByOrgName(orgRecord.getOrgName());
		if(orgs!=null && orgs.size()>0){
			serviceOrg.setId(orgs.get(0).getId());
			tyServiceorgService.update(serviceOrg);
			//如果更新了对口销售，就需要更新对应所有销售
			updateCustomer(orgs.get(0),serviceOrg);
		}else{
			tyServiceorgService.add(serviceOrg);
		}

		
		
		//更新预审客户表中的状态
		orgRecord.setPreporgStatus(Constants.PREP_CUST_STATUS_PASS);
		tyPreserviceorgService.update(orgRecord);
		
		return setSuccessModelMap(modelMap);
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
				}
			}
		}
	}
	
	// 批量导入机构
	@ApiOperation(value = "批量导入机构")
	//@RequiresPermissions("ty.tyPreserviceorg.add")
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
			successNum = importOrgExcel(file, list);
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
	//@RequiresPermissions("ty.tyPreserviceorg.add")
	@RequestMapping(value = "/batchExport", method = RequestMethod.GET)
	public Object batchExport(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyPreserviceorgBean> list = new ArrayList<TyPreserviceorgBean>();
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(WebUtil.getCurrentUser());
		if(tyStafflistService.isSeller(userrole) || tyStafflistService.isSeniorseller(userrole)){
			//params.put("salerId", WebUtil.getCurrentUser());
			//geneDataScope(params);
			//params.put("deleteFlag", "0");
			//list = tyPreserviceorgService.queryByCondition(params);
		} else {
			if(tyStafflistService.isSysmng(userrole)){
				List<TyPreserviceorg> _list = tyPreserviceorgService.getAllRecords();
				for(TyPreserviceorg record : _list){
					TyPreserviceorgBean tyPreserviceorgBean = new TyPreserviceorgBean();
					try {
						BeanUtils.copyProperties(tyPreserviceorgBean, record);
						
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (Exception e) {
						tyPreserviceorgBean = null;
						e.printStackTrace();
					}
					list.add(tyPreserviceorgBean);
				}
			}
		}
		int successNum = exportOrgExcel(response,list);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("successNum", successNum);
		return setSuccessModelMap(modelMap, m);
	}

	private int importOrgExcel(File excel, List<String> list) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean isPreServiceOrgExists = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());// new
																				// Workbook(is);
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
//			List<TyPreserviceorg> allOrgs = tyServiceorgService.getAllRecords();
//			Map<String, TyPreserviceorg> orgMap = new HashMap<String, TyPreserviceorg>();
//			for (TyPreserviceorg org : allOrgs) {
//				orgMap.put(org.getOrgName(), org);
//			}
			List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
			Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
			for (TyStafflist stf : allStaffs) {
				staffMap.put(stf.getStaffName(), stf);
			}
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						TyPreserviceorg preServiceorgBean=null;
						isPreServiceOrgExists = false;
						readColFinished = false;
						currColNum = 0;
						String org_simple_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 1;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotBlank(org_name)) {
							if(StringUtils.length(org_name)<=6){
								throw new BusinessException("机构全称长度不应该少于6个。");
							}
							preServiceorgBean = getPreServiceOrgByName(org_name);
							if (preServiceorgBean != null) {
								isPreServiceOrgExists = true;
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
						

						readColFinished = true;
						boolean isServiceorgExists = isServiceOrgExists(org_name);
						if (isPreServiceOrgExists) {
							TyPreserviceorg record =  preServiceorgBean;
							record.setOrgExists(isServiceorgExists?"1":"0");
							if (org_simple_name != null)
								record.setOrgSimpleName(org_simple_name);
							if (org_name != null)
								record.setOrgName(org_name);
							if (saler_id != null)
								record.setSalerId(saler_id);
							if (service_saler != null)
								record.setServiceSaler(service_saler);
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
							tyPreserviceorgService.update(record);
						} else {
							TyPreserviceorg record = new TyPreserviceorg();
							record.setOrgExists(isServiceorgExists?"1":"0");
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
							record.setPreporgStatus("0"); //默认审核状态
							tyPreserviceorgService.add(record);
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
		list.add(0, "成功"+(isPreServiceOrgExists?"更新":"新增" )+ successNum + "条数据;");
		return successNum;
	}
	
	private int exportOrgExcel(HttpServletResponse response,List<TyPreserviceorgBean> list) {
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
			for (TyPreserviceorg single : list) {
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
		} else if ("公募".equals(cust_cat_str)) {
			return "2";
		} else if ("券商资管".equals(cust_cat_str)) {
			return "3";
		} else if ("券商自营".equals(cust_cat_str)) {
			return "4";
		} else if ("私募".equals(cust_cat_str)) {
			return "5";
		} else if ("信托".equals(cust_cat_str)) {
			return "6";
		} else if ("其他".equals(cust_cat_str)) {
			return "7";
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
	
	private TyPreserviceorgBean getPreServiceOrgByName(String orgName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgNameAccurate", orgName);
		List<TyPreserviceorgBean> orgs = tyPreserviceorgService.queryByCondition(params);
		if(orgs!=null&&orgs.size()==1){
			return orgs.get(0);
		}else{
			return null;
		}
	}
	private boolean isServiceOrgExists(String orgName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgNameAccurate", orgName);
		List<TyServiceorgBean> orgs = tyServiceorgService.queryByCondition(params);
		if(orgs!=null&&orgs.size()==1){
			return true;
		}else{
			return false;
		}
	}

}
