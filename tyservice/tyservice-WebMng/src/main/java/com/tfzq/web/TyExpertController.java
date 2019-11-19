package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;

import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyExpertBean;
import com.tfzq.service.TyExpertService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.PbFileUtils;
import com.tfzq.util.excel.WriteExcelUtil;

/**
 * 专家控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "专家管理", description = "专家管理")
@RequestMapping(value = "ty/tyExpert", method = RequestMethod.POST)
public class TyExpertController extends BaseController {
	@Autowired
	private TyExpertService tyExpertService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysRoleService sysRoleService;

	// 查询专家
	@ApiOperation(value = "查询专家")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		String userId = WebUtil.getCurrentUser();
		TyStafflist tyStafflist = tyStafflistService.queryById(userId);
		List<SysUserRoleBean> myRoles = sysRoleService.queryRoleByUserId(userId);
		if(tyStafflist!=null){
			params.put("deleteFlag", "0");
			params.put("orderBy","id desc");
			params.put("brokerId", userId);
			if(!CollectionUtils.isEmpty(myRoles)) {
				for (SysUserRoleBean sysUserRoleBean : myRoles) {
					if(StringUtils.equalsIgnoreCase(sysUserRoleBean.getRoleName(),"专家库管理员")) {
						params.remove("brokerId");  //专家库管理员具有查看所有人的权限
					}
				}
			}
		}else {
			params.put("deleteFlag", "0");
			params.put("orderBy","id desc");
			params.put("brokerId", userId);
		}
		PageInfo<TyExpertBean> list = tyExpertService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译EXPERT_PROJECTROLE
		Map<String, String> experttypeMap = sysDicService.queryDicByDicIndexKey("EXPERTTYPE");
		addDictFromMap(dicts, "EXPERTTYPE", experttypeMap);
		Map<String, String> closelevelMap = sysDicService.queryDicByDicIndexKey("CLOSELEVEL");
		addDictFromMap(dicts, "CLOSELEVEL", closelevelMap);
		Map<String, String> expertprojectroleMap = sysDicService.queryDicByDicIndexKey("EXPERT_PROJECTROLE");
		addDictFromMap(dicts, "EXPERT_PROJECTROLE", expertprojectroleMap);
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
		for (TyExpertBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getExpertType())) {
				singleBean.setExpertTypeText(experttypeMap.get(singleBean.getExpertType().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getCloseLevel())) {
				singleBean.setCloseLevelText(closelevelMap.get(singleBean.getCloseLevel().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getExpertProjectRole())) {
				singleBean.setExpertProjectRoleText(expertprojectroleMap.get(singleBean.getExpertProjectRole().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getDeleteFlag())){
				singleBean.setDeleteFlagText(deleteflagMap.get(singleBean.getDeleteFlag()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "专家详情")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyExpertBean tyExpertBean = new TyExpertBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyExpert record = tyExpertService.queryById(id);
			try {
				BeanUtils.copyProperties(tyExpertBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyExpertBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> experttypeMap = sysDicService.queryDicByDicIndexKey("EXPERTTYPE");
		addDictFromMap(dicts, "EXPERTTYPE", experttypeMap);
		Map<String, String> closelevelMap = sysDicService.queryDicByDicIndexKey("CLOSELEVEL");
		addDictFromMap(dicts, "CLOSELEVEL", closelevelMap);
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
		if (tyExpertBean != null) {
			if (StringUtils.isNotEmpty(tyExpertBean.getExpertType())) {
				tyExpertBean.setExpertTypeText(experttypeMap.get(tyExpertBean.getExpertType().toString()));
			}
			if (StringUtils.isNotEmpty(tyExpertBean.getCloseLevel())) {
				tyExpertBean.setCloseLevelText(closelevelMap.get(tyExpertBean.getCloseLevel().toString()));
			}
			if(StringUtils.isNotEmpty(tyExpertBean.getDeleteFlag())){
				tyExpertBean.setDeleteFlagText(deleteflagMap.get(tyExpertBean.getDeleteFlag()));
			}
		}

		return setSuccessModelMap(modelMap, tyExpertBean, dicts);
	}

	// 新增专家
	@ApiOperation(value = "添加专家")
	@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyExpert record = Request2ModelUtil.covert(TyExpert.class, request);
		record.setDeleteFlag("0");
		tyExpertService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改专家
	@ApiOperation(value = "修改专家")
	@RequiresPermissions("ty.tyListedcompany.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyExpert record = Request2ModelUtil.covert(TyExpert.class, request);
		tyExpertService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除专家
	@ApiOperation(value = "删除专家")
	@RequiresPermissions("ty.tyListedcompany.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyExpert record = tyExpertService.queryById(id);
		if (record != null) {
			tyExpertService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}

	// 所属行业分类
	@ApiOperation(value = "行业分类")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/industryNum", method = RequestMethod.POST)
	public Object getIndustryNum(HttpServletRequest request, ModelMap modelMap) {
		int totleNum = 0, num = 0, other = 0;
		Map<String, Object> dataList = new HashMap<String, Object>();
		Map<String, Object> params = WebUtil.getParameterMap(request);
		List<TyExpertBean> list = new ArrayList<TyExpertBean>();
		List<TyExpertBean> oldList = tyExpertService.queryIndustryNum(params);
		for (TyExpertBean singleBean : oldList) {
			totleNum += Integer.parseInt(singleBean.getIndustryNum());
			if (num < 3) {
				list.add(singleBean);
			} else {
				other+=Integer.parseInt(singleBean.getIndustryNum());
			}
			num++;
		}
		if(other>0){
			TyExpertBean ty = new TyExpertBean();
			ty.setIndustry("其他");
			ty.setIndustryNum(String.valueOf(other));
			list.add(ty);	
		}
		dataList.put("list", list);
		dataList.put("industryTotleNum", totleNum);
		return setSuccessModelMap(modelMap, dataList);
	}

	// 关系程度分类
	@ApiOperation(value = "关系程度分类")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/closeLevelNum", method = RequestMethod.POST)
	public Object getCloseLevelNum(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		List<TyExpertBean> list = tyExpertService.queryCloseLevelNum(params);
		Map<String, String> closelevelMap = sysDicService.queryDicByDicIndexKey("CLOSELEVEL");
		for (TyExpertBean singleBean : list) {
			if (StringUtils.isNotEmpty(singleBean.getCloseLevel())) {
				singleBean.setCloseLevelText(closelevelMap.get(singleBean.getCloseLevel().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list);
	}
	
	
	// 批量导入专家
	@ApiOperation(value = "批量导入专家")
	@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		List<String> list = new ArrayList<String>();
		int successNum=0;
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
		successNum = importExpertExcel(file, list);

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("errorLogList", list);
		m.put("successNum", successNum);
		if(successNum == 0){
			return setModelMap(modelMap, HttpCode.BAD_REQUEST,m);
		}
		return setSuccessModelMap(modelMap, m);
	}
	
	private int importExpertExcel(File excel, List<String> list) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean isExpertExists = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());// new
																				// Workbook(is);
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
			
			
			Map<String, String> experttypeMap = sysDicService.queryDicByDicIndexKey("EXPERTTYPE");
			Map<String, String> closelevelMap = sysDicService.queryDicByDicIndexKey("CLOSELEVEL");
			Map<String, String> expertprojectroleMap = sysDicService.queryDicByDicIndexKey("EXPERT_PROJECTROLE");
			List<TyServiceorg> allOrgs = tyServiceorgService.getAllRecords();
			Map<String, TyServiceorg> orgMap = new HashMap<String, TyServiceorg>();
			for (TyServiceorg org : allOrgs) {
				orgMap.put(org.getOrgName(), org);
			}
			List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
			Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
			for (TyStafflist stf : allStaffs) {
				staffMap.put(stf.getTel(), stf);
			}
			List<TyExpert> allExperts = tyExpertService.getAllRecords();
			Map<String, TyExpert> expertMap = new HashMap<String, TyExpert>();
			for (TyExpert expert : allExperts) {
				expertMap.put(expert.getExpertTel(), expert);
			}
			
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						isExpertExists = false;
						readColFinished = false;
						currColNum = 0;
						String expert_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 1;
						String expert_tel = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotBlank(expert_tel)) {
							TyExpert expertBean = expertMap.get(expert_tel);
							if (expertBean != null) {
								isExpertExists = true;
							}
						} else {
							continue;
						}

						currColNum = 2;
						String expert_type_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String expert_type = getKeyByValFromDic(expert_type_str,experttypeMap);
						
						currColNum = 3;
						String org_id = null;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if(StringUtils.isNotEmpty(org_name)){
							TyServiceorg org = orgMap.get(org_name);
							if(org != null){
								org_id = org.getId();
							}
						}
						currColNum = 4;
						String title = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 5;
						String industry = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 6;
						String broker_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 7;
						String broker_id = null;
						String broker_tel = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotEmpty(broker_tel)) {
							TyStafflist broker = staffMap.get(broker_tel);
							if (broker != null) {
								broker_id = broker.getId();
								broker_name = broker.getStaffName();
							} else {
								throw new BusinessException(
										"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列对接人有误,请手动新增;");
							}
						}
						
						currColNum = 8;
						String close_level_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String close_levle = getKeyByValFromDic(close_level_str,closelevelMap);
						
						currColNum = 9;
						String project_role_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String project_role = getKeyByValFromDic(project_role_str, expertprojectroleMap);
						
						currColNum = 10;
						String source = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = 11;
						String remark = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						

						readColFinished = true;
						if (isExpertExists) {
							TyExpert record = new TyExpert();
							TyExpert oldrecord = expertMap.get(expert_tel);
							BeanUtils.copyProperties(record, oldrecord);
							
							if (expert_name != null)
								record.setExpertName(expert_name);
							if (expert_type != null)
								record.setExpertType(expert_type);
							if (org_name != null)
								record.setOrgName(org_name);
//							if (saler_id != null)
//								record.setSalerId(saler_id);
//							if (service_saler != null)
//								record.setServiceSaler(service_saler);
							if (org_id != null)
								record.setOrgId(org_id);
							if (title != null)
								record.setTitle(title);
							if (industry != null)
								record.setIndustry(industry);
							if (broker_name != null)
								record.setBrokerName(broker_name);
							if (broker_id != null)
								record.setBrokerId(broker_id);
							if (broker_tel != null)
								record.setBrokerTel(broker_tel);
							if (close_levle != null)
								record.setCloseLevel(close_levle);
							if (project_role != null)
								record.setExpertProjectRole(project_role);
							if (source != null)
								record.setSource(source);
							if(remark != null){
								record.setRemark(remark);
							}
							tyExpertService.update(record);
						} else {
							TyExpert record = new TyExpert();
							record.setExpertName(expert_name);
							record.setOrgName(org_name);
							record.setExpertTel(expert_tel);
							record.setExpertType(expert_type);
							record.setOrgId(org_id);
							record.setTitle(title);
							record.setIndustry(industry);
							record.setBrokerId(broker_id);
							record.setBrokerName(broker_name);
							record.setBrokerTel(broker_tel);
							record.setCloseLevel(close_levle);
							record.setExpertProjectRole(project_role);
							record.setSource(source);
							record.setRemark(remark);
							record.setDeleteFlag("0");
							tyExpertService.add(record);
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
		list.add(0, "成功"+(isExpertExists?"更新":"新增" )+ successNum + "条数据;");
		return successNum;
	}
	
	
	private String getKeyByValFromDic(String value, Map<String, String> experttypeMap) {
		Set<Entry<String,String>> entrySet = experttypeMap.entrySet();
		if(CollectionUtils.isNotEmpty(entrySet) && StringUtils.isNotBlank(value)){
			for (Entry<String, String> entry : entrySet) {
				if(StringUtils.equals(value, entry.getValue())){
					return entry.getKey();
				}
			}
		}
		return null;
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
	
}
