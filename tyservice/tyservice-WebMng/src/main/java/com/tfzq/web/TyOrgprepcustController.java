package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyOrgprepcustService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyOrgprepcustBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.util.Constants;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.InputValidatorUtil;
import com.tfzq.util.excel.WriteExcelUtil;

/**
 * 备选客户控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "备选客户管理", description = "备选客户管理")
@RequestMapping(value = "ty/tyOrgprepcust", method = RequestMethod.POST)
public class TyOrgprepcustController extends BaseController {
	@Autowired
	private TyOrgprepcustService tyOrgprepcustService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private SysAreaService sysAreaService;

	
	// 查询备选客户
	@ApiOperation(value = "查询备选客户")
	//@RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyOrgprepcustBean> list = tyOrgprepcustService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> prepcuststatusMap = sysDicService.queryDicByDicIndexKey("PREPCUSTSTATUS");
		Map<String,String> existMobileFlagMap = sysDicService.queryDicByDicIndexKey("EXISTMOBILEFLAG");
		Map<String, String> industryMap = sysDicService.queryDicByDicIndexKey("INDUSTRY");
		addDictFromMap(dicts,"PREPCUSTSTATUS", prepcuststatusMap);
		addDictFromMap(dicts,"EXISTMOBILEFLAG", existMobileFlagMap);
		addDictFromMap(dicts, "industry", industryMap);
		for (TyOrgprepcustBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getPrepcustStatus())) {
				singleBean.setPrepcustStatusText(prepcuststatusMap.get(singleBean.getPrepcustStatus().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getCustMobile())){
				List<TyOrgcustomerBean> results = getCustomerByMobile(singleBean.getCustMobile());
				singleBean.setExistMobileFlag(results!=null&&results.size()>0?"1":"0");
			}
			if(StringUtils.isNotEmpty(singleBean.getExistMobileFlag())){
				singleBean.setExistMobileFlagText(existMobileFlagMap.get(singleBean.getExistMobileFlag()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	private List<TyOrgcustomerBean> getCustomerByMobile(String custMobile){
		Map<String, Object> pa = new HashMap<String, Object>();
		pa.put("deleteFlag", "0");
		pa.put("orderBy", "id desc");
		pa.put("custMobile", custMobile);
		return tyOrgcustomerService.queryByCondition(pa);
	}
	
	// 详细信息
	@ApiOperation(value = "备选客户详情")
	//@RequiresPermissions("ty.tyOrgcustomer.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyOrgprepcustBean tyOrgprepcustBean = new TyOrgprepcustBean();
		if(StringUtils.isNotEmpty(id)){
			TyOrgprepcust record = tyOrgprepcustService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyOrgprepcustBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyOrgprepcustBean = null;
	        	e.printStackTrace();
	        }
	    }
	    TyOrgcustomerBean tyOrgcustomerBean = new TyOrgcustomerBean();
	    tyOrgcustomerBean.setCustName(tyOrgprepcustBean.getCustName());
	    tyOrgcustomerBean.setIndustry(tyOrgprepcustBean.getIndustry());
	    tyOrgcustomerBean.setTitle(tyOrgprepcustBean.getTitle());
	    tyOrgcustomerBean.setArea(tyOrgprepcustBean.getArea());
	    tyOrgcustomerBean.setCustEmail(tyOrgprepcustBean.getCustEmail());
	    tyOrgcustomerBean.setCustMobile(tyOrgprepcustBean.getCustMobile());
	    
	    String org_id="";
	    String saler_id="";
	    List<TyServiceorgBean> orgBeans = tyServiceorgService.getRecordByOrgName(tyOrgprepcustBean.getOrgName());
		if(orgBeans!=null && orgBeans.size()>0){
			TyServiceorgBean orgBean = orgBeans.get(0);
			org_id = orgBean.getId();
			String salerIdStr = orgBean.getSalerId();
			String[] salerIdArray = salerIdStr.split(",");
			for (String s : salerIdArray) {
				TyStafflist staff = tyStafflistService.queryById(s);
				if(staff.getStaffName().equals(tyOrgprepcustBean.getServiceSaler())){
					saler_id = staff.getId();
					break;
				}
			}
		}
		if(StringUtils.isNoneBlank(org_id)){
			tyOrgcustomerBean.setOrgId(org_id);
			tyOrgcustomerBean.setOrgName(tyOrgprepcustBean.getOrgName());
		}
		if(StringUtils.isNoneBlank(saler_id)){
			tyOrgcustomerBean.setSalerId(saler_id);
		}
		
		//添加字典翻译
		Map<String,List<ExDictItem>> dicts = new HashMap<String,List<ExDictItem>>();
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords(); //*** 找销售人员
		List<ExDictItem> staffitems = new ArrayList<ExDictItem>();
		for (Iterator iterator = tyStafflists.iterator(); iterator.hasNext();) {
			TyStafflist tyStafflist = (TyStafflist) iterator.next();
			ExDictItem item = new ExDictItem();
			item.setId(tyStafflist.getId());
			item.setText(tyStafflist.getStaffName());
			staffitems.add(item);
		}
		dicts.put("TyStafflists", staffitems);
		
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
	    
		return setSuccessModelMap(modelMap, tyOrgcustomerBean,dicts);
	}

	
	// 新增备选客户
	@ApiOperation(value = "添加备选客户")
	//@RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyOrgprepcust record = Request2ModelUtil.covert(TyOrgprepcust.class, request);
		tyOrgprepcustService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改备选客户
	@ApiOperation(value = "修改备选客户")
	//@RequiresPermissions("ty.tyOrgcustomer.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyOrgprepcust record = Request2ModelUtil.covert(TyOrgprepcust.class, request);
		tyOrgprepcustService.update(record);
		return setSuccessModelMap(modelMap);
	}
	
	// 将备选客户保存到客户表
	@ApiOperation(value = "将备选客户保存到客户表")
	//@RequiresPermissions("ty.tyOrgcustomer.update")
	@RequestMapping(value = "/saveCust", method = RequestMethod.POST)
	public Object saveCust(HttpServletRequest request, ModelMap modelMap,@RequestParam(value = "prepcustId", required = true) String prepcustId,
			@RequestParam(value = "labels[]", required = false) List<String> labels) {
		TyOrgcustomer record = Request2ModelUtil.covert(TyOrgcustomer.class, request);
		TyOrgprepcust prepCust = tyOrgprepcustService.queryById(prepcustId);
		Set<String> markSet = new LinkedHashSet<>();
		if(labels != null && labels.size() > 0){
			markSet.addAll(labels);
		}
		if(StringUtils.isNotBlank(prepCust.getMark())){
			markSet.addAll(Arrays.asList(prepCust.getMark().split(Constants.LABEL_SEPERATOR)));
		}
		//对机构进行处理
//		if(StringUtils.isBlank(record.getOrgId())){
//			TyServiceorg orgRecord = Request2ModelUtil.covert(TyServiceorg.class, request);
//			TyServiceorg serviceOrg = new TyServiceorg();
//			serviceOrg.setOrgName(orgRecord.getOrgName());
//			serviceOrg.setOrgSimpleName(orgRecord.getOrgName());
//			serviceOrg.setSalerId(orgRecord.getSalerId());
//			serviceOrg.setServiceSaler(orgRecord.getServiceSaler());
//			TyStafflist staff= tyStafflistService.queryById(orgRecord.getSalerId());
//			SysArea temp = sysAreaService.queryById(staff.getWorkAreaid());
//			serviceOrg.setTeamCat(temp==null?"":temp.getAreaName());
//			serviceOrg.setOrgLevel(orgRecord.getOrgLevel());
//			serviceOrg.setCustStatus(orgRecord.getCustStatus());
//			serviceOrg.setRecEmail(orgRecord.getRecEmail());
//			serviceOrg.setRecSms(orgRecord.getRecSms());
//			serviceOrg.setDeleteFlag("0");
//			TyServiceorg result = tyServiceorgService.add(serviceOrg);
//			record.setOrgId(result.getId());
//		}
		TyOrgcustomer custResult=null;
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		if(StringUtils.isNotBlank(record.getCustMobile())){
			params.put("custMobile", record.getCustMobile());
			custlist = tyOrgcustomerService.queryByCondition(params);
			if(custlist!=null && custlist.size()>0){ //修改客户信息
				custResult = custlist.get(0);
				custResult.setCustName(record.getCustName());
				custResult.setOrgId(record.getOrgId());
				custResult.setOrgName(record.getOrgName());
				custResult.setSalerId(record.getSalerId());
				custResult.setServiceSaler(record.getServiceSaler());
				custResult.setDepartment(record.getDepartment());
				custResult.setTitle(record.getTitle());
				custResult.setCustTel(record.getCustTel());
				custResult.setCustEmail(record.getCustEmail());
				custResult.setArea(record.getArea());
				custResult.setIndustry(record.getIndustry());
				custResult.setDeleteFlag("0");
				//更新客户表中的标签数据
				if(StringUtils.isNotBlank(custResult.getMark())){
					markSet.addAll(Arrays.asList(custResult.getMark().split(Constants.LABEL_SEPERATOR)));
				}
				
				custResult.setMark(getMarkString(markSet));
				tyOrgcustomerService.update(custResult);
//				tyCustomerlabelService.deleteByCustId(custResult.getId());
				//更新客户标签
				updateLabels(markSet,custResult.getId());
				
				return setSuccessModelMap(modelMap);
			}
		}else{
			throw new BusinessException("请输入手机号");
		}
		
		//新增客户记录
		if(StringUtils.isNotEmpty(record.getOrgId())){
			TyServiceorg temp = tyServiceorgService.queryById(record.getOrgId());
			record.setOrgName(temp==null?"":temp.getOrgName());
		}
		if(StringUtils.isNotEmpty(record.getSalerId())){
			TyStafflist staff = tyStafflistService.queryById(record.getSalerId());
			record.setServiceSaler(staff==null?"":staff.getStaffName());
		}
		//添加标签
		String markStr = getMarkString(markSet);
		if(StringUtils.isNotBlank(markStr)){
			record.setMark(markStr);
		}
		record.setDeleteFlag("0");
		custResult = tyOrgcustomerService.add(record);
		//更新客户标签
		updateLabels(markSet,custResult.getId());
		
		//更新预审客户表中的状态
		prepCust.setCustomerId(custResult.getId());
		prepCust.setPrepcustStatus(Constants.PREP_CUST_STATUS_PASS);
		tyOrgprepcustService.update(prepCust);
		
		return setSuccessModelMap(modelMap);
	}

	private String getMarkString(Collection<String> markSet) {
		if(markSet != null && markSet.size() > 0){
			StringBuffer sb = new StringBuffer();
			for (String labelName : markSet) {
				sb.append(labelName + Constants.LABEL_SEPERATOR);
			}
			return sb.substring(0, sb.length() - 1);
		}
		return null;
	}

	// 删除备选客户
	@ApiOperation(value = "删除备选客户")
	//@RequiresPermissions("ty.tyOrgcustomer.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyOrgprepcust record = tyOrgprepcustService.queryById(id);
		if(record!=null){
			tyOrgprepcustService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	
	// 批量导入客户
	@ApiOperation(value = "批量导入备选客户")
	//@RequiresPermissions("ty.tyOrgcustomer.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
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
		if(successNum == 0){
			return setModelMap(modelMap, HttpCode.BAD_REQUEST,m);
		}
		return setSuccessModelMap(modelMap, m);
	}
	
	private void updateLabels(Collection<String> labels,String custId){
		if(labels!=null && labels.size()>0){
			List<TyLabel> sublabel = tyLabelService.getAllSubRecords();
			//如果标签名称已存在那么添加
			//如果不存在，先创建该标签到其他分类，然后再添加
			List<TyCustomerlabelBean> oldLabels = tyCustomerlabelService.queryByCustId(custId);
			Set<String> oldLabelIdSet = new HashSet<>();
			for (TyCustomerlabelBean tyCustomerlabelBean : oldLabels) {
				if(!labels.contains(tyCustomerlabelBean.getLabelName())) {
					oldLabelIdSet.add(tyCustomerlabelBean.getId());
				}
			}
			for (String id : oldLabelIdSet) {
				tyCustomerlabelService.deletePhysical(id);
			}
			for (String labelName : labels) {
				int isExist = 0;
				for (TyLabel sub : sublabel) {
					if(sub.getLabelName()!=null && labelName.equals(sub.getLabelName())){
						TyCustomerlabel label = new  TyCustomerlabel();
						label.setCustomerId(custId);
						label.setLabelId(sub.getId());
						tyCustomerlabelService.add(label);
						isExist=1;
						break;
					}
				}
				if(isExist==0){
					TyLabel lb = new TyLabel();
					lb.setLabelName(labelName);
					lb.setCatId(Constants.LABEL_OTHER_CAT_ID);
					lb.setCatName(Constants.LABEL_OTHER_CAT_NAME);
					lb.setLabelLevel("1");
					lb.setPid("0");
					TyLabel newRecord = tyLabelService.add(lb);
					TyCustomerlabel label = new  TyCustomerlabel();
					label.setCustomerId(custId);
					label.setLabelId(newRecord.getId());
					tyCustomerlabelService.add(label);
				}
			}
		}
	}
	
	private void removeDuplicate(List<String> list){      
		List<String> tempList= new ArrayList<String>();  
	    for(String i:list){  
	        if(!tempList.contains(i)){  
	            tempList.add(i);  
	        }  
	    } 
	    list.clear();
	    for(String i:tempList){  
	    	list.add(i);  
	    } 
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
	
	private int importCustExcel(File excel, List<String> list) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		String isMobileExists = "0";
		boolean samePrepCustMobileaFlag = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());// new
																				// Workbook(is);
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
//			List<TyServiceorg> allOrgs = tyServiceorgService.getAllRecords();
//			Map<String, TyServiceorg> orgMap = new HashMap<String, TyServiceorg>();
//			for (TyServiceorg org : allOrgs) {
//				orgMap.put(org.getOrgName(), org);
//			}
//			List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
//			Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
//			for (TyStafflist stf : allStaffs) {
//				staffMap.put(stf.getId(), stf);
//			}
			// List<TyOrgcustomer> = tyOrgcustomerService.getAllRecords();
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
//						isMobileExists = false;
						samePrepCustMobileaFlag = false;
						isMobileExists = "0";
						currColNum = 0;
						readColFinished = false;
						String cust_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isBlank(cust_name)) {
							continue;
						}
						currColNum = currColNum + 1;
						String org_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						String org_id = null;
						currColNum = currColNum + 1;
						String service_saler = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						String saler_id = null;
//						TyServiceorg orgBean = orgMap.get(org_name);
//						if (orgBean != null) {
//							org_id = orgBean.getId();
//							String salerIdStr = orgBean.getSalerId();
//							String[] salerIdArray = salerIdStr.split(",");
//							Boolean ismatch = false;
//							for (String sid : salerIdArray) {
//								TyStafflist staff = staffMap.get(sid);// tyStafflistService.queryById(s);
//								if (staff.getStaffName().equals(service_saler)) {
//									saler_id = staff.getId();
//									ismatch = true;
//									break;
//								}
//							}
//							if (!ismatch) {
//								throw new BusinessException(
//										"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列对口销售名称有误,请手动新增;");
//							}
//						} else {
//							throw new BusinessException(
//									"Excel文件单元格第" + (rowNum + 1) + "行,第" + currColNum + "列机构名称有误,请手动新增;");
//						}

//						currColNum = currColNum + 1;
//						String department = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String title = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = currColNum + 1;
						String cust_mobile = WriteExcelUtil.trimAllBlankCellStringToNull(hssfRow, currColNum);
						
						Map<String, Object> pa = new HashMap<String, Object>();
						TyOrgprepcust record = new TyOrgprepcust();
						TyOrgcustomer customer = new TyOrgcustomer();
						List<TyOrgprepcustBean> custlist = new ArrayList<TyOrgprepcustBean>();
						List<TyOrgcustomerBean> orgcustList = new ArrayList<>();
						pa.put("deleteFlag", "0");
						pa.put("orderBy", "id desc");
						if (StringUtils.isNotBlank(cust_mobile)) {
							if(InputValidatorUtil.isMobile(cust_mobile)){
								cust_mobile = StringUtils.remove(cust_mobile, "-");
								pa.put("custMobile", cust_mobile);
								custlist = tyOrgprepcustService.queryByCondition(pa);
								orgcustList = tyOrgcustomerService.queryByCondition(pa);
								if (custlist != null && custlist.size() > 0) {
									samePrepCustMobileaFlag =  true;    //预选客户中存在相同的手机账户
									record = custlist.get(0);
									// throw new
									// IllegalParameterException("Excel文件单元格第"+(rowNum+1)+"行,第"+(currColNum+1)+"手机号已存在;");
								}
								if(orgcustList != null && orgcustList.size() > 0){
									isMobileExists = "1";                          //客户表中存在相同的手机账户
									//customer = orgcustList.get(0);
								}
							}else{
								throw new IllegalParameterException(
										"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "手机号不符合规范:大陆手机号11位,香港手机号格式为[00852-11111111]");

							}
						} else {
							throw new IllegalParameterException(
									"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "手机号为空");
						}

//						currColNum = currColNum + 1;
//						String cust_tel = WriteExcelUtil.trimAllBlankCellStringToNull(hssfRow, currColNum);
//						if(!StringUtils.isBlank(cust_tel)){
//							if(InputValidatorUtil.isTel(cust_tel)){
//								//cust_tel = StringUtils.remove(cust_tel, "-");
//							}else{
//								throw new IllegalParameterException(
//										"Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "座机号不符合规则");
//							}
//						}
						currColNum = currColNum + 1;
						String cust_email = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = currColNum + 1;
						String area = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						currColNum = currColNum + 1;
						String industry = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
//						currColNum = currColNum + 1;
//						String domain = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						
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
//							StringBuffer sb = new StringBuffer();
//							for (String labelName : labels) {
//								sb.append(labelName + Constants.LABEL_SEPERATOR);
//							}
							// markStr = sb.toString();
							markStr = getMarkString(labels);
						}
//						currColNum = currColNum + 1;
//						String idNo = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);

						TyOrgprepcust newRecord = new TyOrgprepcust();
						readColFinished = true;
						if (samePrepCustMobileaFlag) {
							// 手机号存在，更新
//							if(!StringUtils.contains(record.getSalerId(), userId)){
//								throw new BusinessException(
//										"Excel文件单元格第" + (rowNum + 1) + "行客户不属于当前登录用户，不允许修改。");
//							}
							
							if (cust_name != null)
								record.setCustName(cust_name);
							if (org_name != null)
								record.setOrgName(org_name);
//							if (org_id != null)
//								record.setOrgId(org_id);
//							if (saler_id != null)
//								record.setSalerId(saler_id);
							if (service_saler != null)
								record.setServiceSaler(service_saler);
//							if (department != null)
//								record.setDepartment(department);
							if (title != null)
								record.setTitle(title);
							if (cust_mobile != null)
								record.setCustMobile(cust_mobile);
//							if (cust_tel != null)
//								record.setCustTel(cust_tel);
							if (cust_email != null)
								record.setCustEmail(cust_email);
							if (area != null)
								record.setArea(area);
							if (industry != null)
								record.setIndustry(industry);
//							if (domain != null)
//								record.setDomain(domain);
							if (markStr != null)
								record.setMark(markStr);
//							if (idNo != null)
//								record.setIdNo(idNo);
							record.setExistMobileFlag(isMobileExists);
							record.setPrepcustStatus(Constants.PREP_CUST_STATUS_NOT_AUDIT);
							tyOrgprepcustService.update(record);
//							tyCustomerlabelService.deleteByCustId(record.getId());
							// 更新客户标签
//							updateLabels(labels, record.getId());
							newRecord = tyOrgprepcustService.queryById(record.getId());
						} else {// 手机号不存在，新增
							record.setCustName(cust_name);
							record.setOrgName(org_name);
//							record.setOrgId(org_id);
//							record.setSalerId(saler_id);
							record.setServiceSaler(service_saler);
//							record.setDepartment(department);
							record.setTitle(title);
							record.setCustMobile(cust_mobile);
//							record.setCustTel(cust_tel);
							record.setCustEmail(cust_email);
							record.setArea(area);
							record.setIndustry(industry);
//							record.setDomain(domain);
							record.setMark(markStr);
//							record.setIdNo(idNo);
							record.setDeleteFlag("0");
							record.setExistMobileFlag(isMobileExists);
							record.setPrepcustStatus(Constants.PREP_CUST_STATUS_NOT_AUDIT);
							newRecord = tyOrgprepcustService.add(record);
							// 更新客户标签
//							updateLabels(labels, newRecord.getId());
							newRecord = tyOrgprepcustService.queryById(newRecord.getId());
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
		list.add(0, "成功"+(samePrepCustMobileaFlag?"更新":"新增" ) + successNum + "条数据;");
		return successNum;
	}
	
}
