package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.support.email.EmailSender;
import org.ibase4j.core.support.security.Md5Util;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyAppsecretService;
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyListedcompanyService;
import com.tfzq.service.TyMessageauditService;
import com.tfzq.service.TyMessagelogService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyOrgprepcustService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyAppsecret;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyOrgprepcust;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyListedcompanyBean;
import com.tfzq.ty.model.ty.TyMJprepcustBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyOrgprepcustBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;
import com.tfzq.util.HttpClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 客户控制类
 * 
 * @author shenli
 */
@RestController
@Api(value = "公共查询管理", description = "客户管理")
@RequestMapping(value = "openapi", method = RequestMethod.POST)
public class BizOpenApiController extends BaseController {
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysPositionService sysPositionService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private TyAppsecretService tyAppsecretService;
	@Autowired
	private TyMessageauditService tyMessageauditService;
	@Autowired
	private TyMessagelogService tyMessagelogService;
	@Autowired
	private TyOrgprepcustService tyOrgprepcustService;
	@Autowired
	private TyListedcompanyService tyListedcompanyService;

	// 查询机构
	@ApiOperation(value = "查询机构")
	@RequestMapping(value = "/read/orglist")
	public Object orglist(HttpServletRequest request, ModelMap modelMap) throws Exception{
		checkInterfaceRight(request);
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyServiceorgBean> list = tyServiceorgService.queryBeans(params);
		List<TyServiceorgBean> orgList = tyServiceorgService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
		
		if(orgList!=null && orgList.size()>0){
			addDictFromModel(dicts,"Serviceorgs", orgList,"id","orgName");
		}
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
		
		//添加字典翻译
		if(list.getList().size()>0){
			for (TyServiceorgBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getOrgLevel())) {
					singleBean.setOrgLevelName(orglevelMap.get(singleBean.getOrgLevel().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustStatus())) {
					singleBean.setCustStatusName(custStatusMap.get(singleBean.getCustStatus().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustCat())) {
					singleBean.setCustCatName(custCatMap.get(singleBean.getCustCat().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "机构详情")
	@RequestMapping(value = "/read/orgdetail")
	public Object orgdetail(HttpServletRequest request,ModelMap modelMap, @RequestParam(value = "id", required = false) String id) throws Exception{
		checkInterfaceRight(request);
		
		TyServiceorgBean tyServiceorgBean = new TyServiceorgBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyServiceorg record = tyServiceorgService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyServiceorgBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyServiceorgBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		if(null!=tyStafflists && tyStafflists.size()>0){
			for (TyStafflist tyStaff : tyStafflists) {
				if(sysWorkAreas.size()>0 && tyStaff!=null){
					for (SysArea sysArea : sysWorkAreas) {
						if(tyStaff.getWorkAreaid().equals(sysArea.getId())){
							String staffName = tyStaff.getStaffName() + "("+sysArea.getAreaName()+ ")";
							tyStaff.setStaffName(staffName);
							break;
						}
					}
				}
			}
		}
		
		addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
//		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
	    if(tyServiceorgBean!=null){
			if (StringUtils.isNotEmpty(tyServiceorgBean.getOrgLevel())) {
				tyServiceorgBean.setOrgLevelName(orglevelMap.get(tyServiceorgBean.getOrgLevel().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustStatus())) {
				tyServiceorgBean.setCustStatusName(custStatusMap.get(tyServiceorgBean.getCustStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustCat())) {
				tyServiceorgBean.setCustCatName(custCatMap.get(tyServiceorgBean.getCustCat().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, tyServiceorgBean,dicts);
	}
	
	// 查询机构客户
	@ApiOperation(value = "查询机构客户")
	@RequestMapping(value = "/read/custlist")
	public Object custlist(HttpServletRequest request, ModelMap modelMap) throws Exception{
		checkInterfaceRight(request);
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyOrgcustomerBean> list = tyOrgcustomerService.queryBeans(params);
		
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		
		//添加字典翻译
		for (TyOrgcustomerBean singleBean : list.getList()) {
			if(StringUtils.isNotEmpty(singleBean.getSalerId())){
				TyStafflist temp = tyStafflistService.queryById(singleBean.getSalerId());
				singleBean.setServiceSaler(temp==null?"":temp.getStaffName());
				singleBean.setSalerMobile(temp==null?"":temp.getTel());
			}
			if(StringUtils.isNotEmpty(singleBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(singleBean.getOrgId());
				singleBean.setOrgName(temp==null?"":temp.getOrgName());
				if(temp!=null && StringUtils.isNotEmpty(temp.getOrgLevel())){
					singleBean.setOrgLevel(temp.getOrgLevel());
					singleBean.setOrgLevelName(orglevelMap.get(temp.getOrgLevel().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list);
	}

	// 详细信息
	@ApiOperation(value = "机构客户详情")
	@RequestMapping(value = "/read/custdetail")
	public Object custdetail(HttpServletRequest request,ModelMap modelMap, @RequestParam(value = "id", required = false) String id) throws Exception{
		checkInterfaceRight(request);
		TyOrgcustomerBean tyOrgcustomerBean = new TyOrgcustomerBean();
		Map<String,List<ExDictItem>> dicts = new HashMap<String,List<ExDictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyOrgcustomer record = tyOrgcustomerService.queryById(id);
			
			//更新remark冗余字段
			Map<String, Object> pm = new HashMap<String, Object>();
			pm.put("customerId", record.getId());
			pm.put("pageSize", "1000");
			pm.put("orderBy","id desc");
			PageInfo<TyCustomerlabelBean> labelBeans = tyCustomerlabelService.queryBeans(pm);
			StringBuffer sb = new StringBuffer();
			if(labelBeans.getList().size()>0){
				for (TyCustomerlabelBean labelBean : labelBeans.getList()) {
					if(labelBean.getLabelId()!=null){
						TyLabel tl = tyLabelService.queryById(labelBean.getLabelId());
						sb.append(tl.getLabelName() + Constants.LABEL_SEPERATOR);
					}
				}
				String mark = sb.toString();
				record.setMark(mark.substring(0,mark.length()-1));
				tyOrgcustomerService.update(record);
			}
			
			try {
	            BeanUtils.copyProperties(tyOrgcustomerBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyOrgcustomerBean = null;
	        	e.printStackTrace();
	        }
	    }
		
		
		//添加字典翻译
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
//			addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
		
		
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
	    if(tyOrgcustomerBean!=null){
			if(StringUtils.isNotEmpty(tyOrgcustomerBean.getSalerId())){
				TyStafflist temp = tyStafflistService.queryById(tyOrgcustomerBean.getSalerId());
				tyOrgcustomerBean.setServiceSaler(temp==null?"":temp.getStaffName());
			}
			if(StringUtils.isNotEmpty(tyOrgcustomerBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(tyOrgcustomerBean.getOrgId());
				tyOrgcustomerBean.setOrgName(temp==null?"":temp.getOrgName());
			}
			if(null!=tyOrgcustomerBean.getId()){
				catLabelList = tyLabelService.getCatListByCustomerId(tyOrgcustomerBean.getId());
				
				if(catLabelList!=null){
					for (Iterator iterator = catLabelList.iterator(); iterator
							.hasNext();) {
						TyLabelBean tyLabelBean2 = (TyLabelBean) iterator.next();
						List<TyLabelBean> subList = new ArrayList<TyLabelBean>();
						Map<String, Object> pa = new HashMap<String, Object>();
						pa.put("customerId", tyOrgcustomerBean.getId());
						pa.put("catId", tyLabelBean2.getId());
						subList = tyLabelService.getCustSubList(pa);
						if(subList!=null){
							tyLabelBean2.setSubList(subList);
						}
					}
				}
			}
	    }
	    tyOrgcustomerBean.setLabelCatList(catLabelList);
	    
		return setSuccessModelMap(modelMap, tyOrgcustomerBean,dicts);
	}
	// 新增客户
	@ApiOperation(value = "新增客户")
	@RequestMapping(value = "/addCust")
	public Object addCust(HttpServletRequest request,ModelMap modelMap) throws Exception{
		String url = PropertiesUtil.getString("31meijia.api.member") + "save";
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", "13DC928C5E4795DE1E3219D733EAE656");
        params.put("RealName", "张三疯");
        params.put("Mobile", "13414321433");
		String retStr = HttpClient.post(url, params);
		System.out.println(retStr);
		return setSuccessModelMap(modelMap);
	}
	
	// 新增备选客户
	@ApiOperation(value = "新增备选客户")
	@RequestMapping(value = "/addPrepCust")
	public Object addPrepCust(HttpServletRequest request,ModelMap modelMap,TyMJprepcustBean mjPrepBean) throws Exception{
		logger.info(mjPrepBean.toString());
		checkInterfaceRight(request);
		
		if(StringUtils.isBlank(mjPrepBean.getCompany())){
			throw new BusinessException("机构名称不能为空！");
		}
		if(StringUtils.isBlank(mjPrepBean.getField916157347())){
			throw new BusinessException("对口销售不能为空！");
		}
		if(StringUtils.isBlank(mjPrepBean.getRealName())){
			throw new BusinessException("客户名称不能为空！");
		}
		if(StringUtils.isBlank(mjPrepBean.getPosStatusEx())){
			throw new BusinessException("职位不能为空！");
		}
		if(StringUtils.isBlank(mjPrepBean.getEmail())){
			throw new BusinessException("邮箱不能为空！");
		}
		if(StringUtils.isBlank(mjPrepBean.getMobile())){
			throw new BusinessException("手机号不能为空！");
		}
		
		String existMobileFlag="";
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custMobile", mjPrepBean.getMobile());
		custlist = tyOrgcustomerService.queryByCondition(params);
		if(custlist!=null && custlist.size()>0){
			//throw new BusinessException("手机号在白名单客户中已存在！");
			existMobileFlag="1";
		}else{
			existMobileFlag="0";
		}
		
		List<TyOrgprepcustBean> greplist = new ArrayList<TyOrgprepcustBean>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		params.put("custMobile", mjPrepBean.getMobile());
		params.put("prepcustStatus", Constants.PREP_CUST_STATUS_NOT_AUDIT);
		greplist = tyOrgprepcustService.queryByCondition(params2);
		if(greplist!=null && greplist.size()>0){
			TyOrgprepcustBean grepBean = greplist.get(0);
			grepBean.setOrgName(mjPrepBean.getCompany());
			grepBean.setServiceSaler(mjPrepBean.getField916157347());
			grepBean.setCustName(mjPrepBean.getRealName());
			grepBean.setIndustry(mjPrepBean.getField916159034());
			grepBean.setTitle(mjPrepBean.getPosStatusEx());
			grepBean.setArea(mjPrepBean.getSanJiDiZhiLianDong());
			grepBean.setCustEmail(mjPrepBean.getEmail());
			grepBean.setCustMobile(mjPrepBean.getMobile());
			grepBean.setPrepcustStatus(Constants.PREP_CUST_STATUS_NOT_AUDIT);
			grepBean.setExistMobileFlag(existMobileFlag);
			grepBean.setUpdateBy("1");
			grepBean.setUpdateTime(new Date());
			tyOrgprepcustService.update(grepBean);
		}else{
			TyOrgprepcust prepcust = new TyOrgprepcust();
			prepcust.setOrgName(mjPrepBean.getCompany());
			prepcust.setServiceSaler(mjPrepBean.getField916157347());
			prepcust.setCustName(mjPrepBean.getRealName());
			prepcust.setIndustry(mjPrepBean.getField916159034());
			prepcust.setTitle(mjPrepBean.getPosStatusEx());
			prepcust.setArea(mjPrepBean.getSanJiDiZhiLianDong());
			prepcust.setCustEmail(mjPrepBean.getEmail());
			prepcust.setCustMobile(mjPrepBean.getMobile());
			prepcust.setPrepcustStatus(Constants.PREP_CUST_STATUS_NOT_AUDIT);
			prepcust.setExistMobileFlag(existMobileFlag);
			prepcust.setCreateBy("1");
			prepcust.setCreateTime(new Date());
			prepcust.setUpdateBy("1");
			prepcust.setUpdateTime(new Date());
			tyOrgprepcustService.add(prepcust);
		}
		
		modelMap.clear();
		return setSuccessModelMap(modelMap);
	}
	
	
	//moningcall群发送邮件，email邮件
	@ApiOperation(value = "发送资讯邮件")
	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	public Object sendMsg(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custListStr", required = false) String custListStr,@RequestParam(value = "msgType", required = false) String msgType,
			@RequestParam(value = "msgContent", required = false) String msgContent,@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "isAll", required = false) boolean isAll,@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "file", required = false)List<MultipartFile> multiUploadFiles) {
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}
		
		String fileNames = "";
		String filePaths = "";
		
		List<String> arrayList = new ArrayList<String>();
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
//		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		params.put("pageSize", "100000");
		if(isAll){
			if(StringUtils.isNotBlank(groupId)){
    			params.put("groupId", groupId);
    			custlist = tyOrgcustomerService.queryByCondition(params);
			}
		}else{
			//直接传送客户列表
			if(custListStr!=null && StringUtils.isNotBlank(custListStr)){
				String[] array = custListStr.split(",");
				for (String string : array) {
					arrayList.add(string);
				}
				params.put("custidList", arrayList);
				custlist = tyOrgcustomerService.queryByCondition(params);
			}else{
				
			}
		}

		//发送审核
		if(custlist!=null && custlist.size()>0){
			TyMessageaudit messageAudit = new TyMessageaudit();
			messageAudit.setMsgType(msgType);
			messageAudit.setMsgContent(msgContent);
			messageAudit.setTitle(title==null?"-":title);
			//直接审核通过
			messageAudit.setAuditFlag("1");
			messageAudit.setCreateBy("1");
			messageAudit.setUpdateBy("1");
			messageAudit.setUpdateTime(new Date());
			messageAudit.setCreateTime(new Date());
			TyMessageaudit auditResult = tyMessageauditService.add(messageAudit);
			
			//插入记录
//			List<TyMessagelog> mlog = new ArrayList<TyMessagelog>();
			Integer failCount = 0;
			Integer successCount = 0;
			for (TyOrgcustomerBean tyOrgcustomer:custlist) {
//					TyOrgcustomer tyOrgcustomer = tyOrgcustomerService.queryById(custid);
				TyMessagelog messagelog = new TyMessagelog();
				String key = messagelog.getClass().getSimpleName();
				messagelog.setId(tyMessagelogService.createId(key));
				messagelog.setMsgType(msgType);
				messagelog.setMsgContent(msgContent);
				messagelog.setTitle(title==null?"-":title);
				messagelog.setReceiver(tyOrgcustomer.getId());
				messagelog.setReceiverName(tyOrgcustomer.getCustName());
				messagelog.setReceiverTel(tyOrgcustomer.getCustMobile());
				messagelog.setReceiverEmail(tyOrgcustomer.getCustEmail());
				messagelog.setSendFlag("0");
				messagelog.setSendDate(DateUtil.format(new Date()));
				//直接设置用户为admin
				messagelog.setCreateBy("1");
				messagelog.setCreateTime(new Date());
				messagelog.setUpdateBy("1");
				messagelog.setUpdateTime(new Date());
				messagelog.setRemark("待发送");
				messagelog.setMsgAuditid(auditResult.getId());
				messagelog.setFileName(fileNames);
				messagelog.setFilePath(filePaths);
				TyMessagelog add = tyMessagelogService.add(messagelog);
				if(add != null){
//					System.out.println("晨报资讯发送成功！！！！");
					successCount ++;
				}else{
//					System.out.println("晨报资讯发送失败！！！！");
					failCount ++;
				}
			}
			System.out.println("morningcall发送成功记录条数：    "+successCount+"  条");
			System.out.println("morningcall发送失败记录条数：    "+failCount+"  条");
//			tyMessagelogService.batchAdd(mlog);
		}
		return setSuccessModelMap(modelMap);
	}
	/**
	 * 修改待审核短信的状态 2018-01-23
	 * @param request
	 * @param modelMap
	 * @param id
	 * @param status
	 * @return
	 */
	@ApiOperation(value = "修改待审核短信的状态")
	@RequestMapping(value = "/finishAuditMsg", method = RequestMethod.POST)
	public Object finishAuditMsg(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "status", required = true) String status) {
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}

		TyMessageaudit record = tyMessageauditService.queryById(id);
		if(record!=null){
			//status:0审核不通过,1审核通过
			//auditFlag:0草稿,1审核通过,2审核不通过
			record.setAuditFlag(StringUtils.equals(status, "1")?"1":"2");
			record.setRemark("");
			record.setAuditBy("投研平台");
			record.setAuditTime(new Date());
			tyMessageauditService.update(record);

		}
		
		return setSuccessModelMap(modelMap);
	}

	/**
	 * 根据时间戳增量查询机构客户
	 * @param request
	 * @param modelMap
	 * @param lastestTime
	 * @param recordSize
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据时间戳增量查询机构客户")
	@RequestMapping(value = "/read/getLastestCusts")
	public Object getLastestCusts(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "lastestTime", required = false) String lastestTime,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}

		
		PageInfo<TyOrgcustomerBean> datas = tyOrgcustomerService.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
		return setSuccessModelMap(modelMap,datas);
	}
	
	
	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param id
	 * @param area
	 * @param custEmail
	 * @param custMobile
	 * @param custName
	 * @param custTel
	 * @param department
	 * @param industry
	 * @param title
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "更新机构客户信息")
	@RequestMapping(value = "/update/orgCustomer")
	public Object updateOrgCustomer(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "custEmail", required = false) String custEmail,
			@RequestParam(value = "custMobile", required = false) String custMobile,
			@RequestParam(value = "custName", required = false) String custName,
			@RequestParam(value = "custTel", required = false) String custTel,
			@RequestParam(value = "department", required = false) String department,
			@RequestParam(value = "industry", required = false) String industry,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "domain", required = false) String domain) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}

		TyOrgcustomer record = new TyOrgcustomer();
		record.setId(id);
		record.setArea(area);
		record.setCustEmail(custEmail);
		record.setCustMobile(custMobile);
		record.setCustName(custName);
		record.setCustTel(custTel);
		record.setDepartment(department);
		record.setIndustry(industry);
		record.setTitle(title);
		record.setDomain(domain);
		if(StringUtils.isNotBlank(record.getCustMobile())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("deleteFlag", "0");
			params.put("orderBy","id desc");
			params.put("custMobile", record.getCustMobile());
			List<TyOrgcustomerBean> custlist = tyOrgcustomerService.queryByCondition(params);
			if(custlist!=null && custlist.size()>0){
				if(custlist.size()==1){
					if( !(record.getId().equals(custlist.get(0).getId())) ){
						throw new IllegalParameterException("手机号已存在");
					}
				}else{
					throw new IllegalParameterException("手机号已存在");
				}
			}
		}
		tyOrgcustomerService.update(record);

		return setSuccessModelMap(modelMap);
	}


	/**
	 * 根据时间戳增量查询机构
	 * @param request
	 * @param modelMap
	 * @param lastestTime
	 * @param recordSize
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据时间戳增量查询机构")
	@RequestMapping(value = "/read/getLatestServiceOrgs")
	public Object getLatestServiceOrgs(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "lastestTime", required = false) String lastestTime,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}
		

		PageInfo<TyServiceorgBean> datas = tyServiceorgService.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);

		
		return setSuccessModelMap(modelMap,datas);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param id
	 * @param area
	 * @param custEmail
	 * @param custMobile
	 * @param custName
	 * @param custTel
	 * @param department
	 * @param industry
	 * @param title
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "更新机构客户信息")
	@RequestMapping(value = "/update/serviceOrg")
	public Object updateServiceOrg(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "companyMail", required = false) String companyMail,
			@RequestParam(value = "companyTel", required = false) String companyTel,
			@RequestParam(value = "contactorName", required = false) String contactorName,
			@RequestParam(value = "contactorTel", required = false) String contactorTel,
			@RequestParam(value = "custCat", required = false) String custCat,
			@RequestParam(value = "custStatus", required = false) String custStatus,
			@RequestParam(value = "orgLevel", required = false) String orgLevel,
			@RequestParam(value = "orgName", required = false) String orgName,
			@RequestParam(value = "orgSimpleName", required = false) String orgSimpleName,
			@RequestParam(value = "salerId", required = false) String salerId,
			@RequestParam(value = "serviceSaler", required = false) String serviceSaler,
			@RequestParam(value = "teamCat", required = false) String teamCat) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}

		TyServiceorg record = new TyServiceorg();
		record.setId(id);
		record.setAddress(address);
		record.setCompanyMail(companyMail);
		record.setCompanyTel(companyTel);
		record.setContactorName(contactorName);
		record.setContactorTel(contactorTel);
		record.setCustCat(custCat);
		record.setCustStatus(custStatus);
		record.setOrgLevel(orgLevel);
		record.setOrgName(orgName);
		record.setOrgSimpleName(orgSimpleName);
		record.setSalerId(salerId);
		record.setServiceSaler(serviceSaler);
		record.setTeamCat(teamCat);
		
		tyServiceorgService.update(record);
		return setSuccessModelMap(modelMap);
	}
	
	/**
	 * 根据时间戳增量查询上市公司
	 * @param request
	 * @param modelMap
	 * @param lastestTime
	 * @param recordSize
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据时间戳增量查询上市公司")
	@RequestMapping(value = "/read/getLastestListedCompany")
	public Object getLastestListedCompany(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "lastestTime", required = false) String lastestTime,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}
		

		PageInfo<TyListedcompanyBean> datas = tyListedcompanyService.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);

		
		return setSuccessModelMap(modelMap,datas);
	}
	
	/**
	 * 根据时间戳增量查询上市公司
	 * @param request
	 * @param modelMap
	 * @param lastestTime
	 * @param recordSize
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据时间戳增量查询用户信息")
	@RequestMapping(value = "/read/getLastestStaff")
	public Object getLastestStaff(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "lastestTime", required = false) String lastestTime,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize) throws Exception{
		try {
			checkInterfaceRight(request);
		} catch (IllegalParameterException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.BAD_REQUEST);
		} catch (BusinessException e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
		}

		PageInfo<TyStafflistBean> datas = tyStafflistService.queryLatestModifiedRecords(lastestTime,pageNum,pageSize);
		for (TyStafflistBean singleBean : datas.getList()) {
			if(StringUtils.isNotEmpty(singleBean.getDeptId())){
				SysDept temp = sysDeptService.queryById(singleBean.getDeptId());
				singleBean.setDeptName(temp==null?"":temp.getDeptName());
			}
			if(StringUtils.isNotEmpty(singleBean.getPositionId())){
				SysPosition temp = sysPositionService.queryById(singleBean.getPositionId());
				singleBean.setPosition(temp==null?"":temp.getPositionName());
			}
			if(StringUtils.isNotEmpty(singleBean.getWorkAreaid())){
				SysArea temp = sysAreaService.queryById(singleBean.getWorkAreaid());
				singleBean.setWorkAreaname(temp==null?"":temp.getAreaName());
				//兼容老字段
				singleBean.setAreaId(singleBean.getWorkAreaid());
				singleBean.setArea(temp==null?"":temp.getAreaName());
			}
		}

		
		return setSuccessModelMap(modelMap,datas);
	}
	@RequestMapping(value = "/read/testEmail")
	public Object testEmail(HttpServletRequest request, ModelMap modelMap){
		EmailSender emailSender = new EmailSender("192.168.24.32");
		emailSender.setNamePass("tyservice@tfzq.com", "2017TFzq", "2017TFzq");
		
		emailSender.createMimeMessage();
		emailSender.setFrom("彭涛,tyservice@tfzq.com");
		emailSender.setSubject("标题啊");
		emailSender.setBody("这是一个测试邮件");
		emailSender.setTo("peng.tao@dangdaifintech.com");
		emailSender.sendout();

		return setSuccessModelMap(modelMap);
	}

	/**
	 * 对请求参数需要做接入权限判断
	 * 
	 * @param request
	 */
	private void checkInterfaceRight(HttpServletRequest request) throws Exception {
		String url = request.getRequestURI();

		String urlCompatible=url;
		url = StringUtils.remove(url, "/tyservice-WebMng");//去掉项目名
		String timestmap = request.getParameter("t");
		if (StringUtils.isBlank(timestmap)) {
			throw new IllegalParameterException("参数异常！");
		}
		Date date =null;
		
		try {
			date = new Date(Long.valueOf(timestmap));
		} catch (Exception e) {
			throw new IllegalParameterException("时间参数异常！");
		}
		Date now = new Date();
		if (Math.abs(date.getTime() - now.getTime()) > 1000 * 60 * 60 *3L) {
			throw new IllegalParameterException("令牌已失效！");
		}
		String p = request.getParameter("p");
		String secretCode = "";// 根据P查询
		if (StringUtils.isNotBlank(p)) {
			TyAppsecret infAppsecret = tyAppsecretService.queryByPlatSrc(p);
			if (null != infAppsecret) {
				secretCode = infAppsecret.getSecretCode();
			} else {
				throw new BusinessException("平台来源不存在！");
			}
		} else {
			throw new IllegalParameterException("参数异常！");
		}
		String m = request.getParameter("m");
		if (StringUtils.isBlank(m)) {
			throw new IllegalParameterException("参数异常！");
		}
		String mdCompatible = Md5Util.getMD5(urlCompatible + timestmap + secretCode);
		String md = Md5Util.getMD5(url + timestmap + secretCode);
		logger.info("兼容url:" + urlCompatible + " timestmap:" + timestmap + " secretCode:" + secretCode + " m:" + m + " md:" + mdCompatible);
		logger.info("url:" + url + " timestmap:" + timestmap + " secretCode:" + secretCode + " m:" + m + " md:" + md);
		if (!m.equals(mdCompatible) && !m.equals(md)) {
			throw new IllegalParameterException("令牌验证错误！");
		}
	}

}
