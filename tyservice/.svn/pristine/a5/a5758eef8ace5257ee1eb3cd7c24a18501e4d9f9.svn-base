package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 活动报名控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "活动报名管理", description = "活动报名管理")
@RequestMapping(value = "ty/tyActivitysign", method = RequestMethod.POST)
public class TyActivitysignController extends BaseController {
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysAreaService sysAreaService;

	// 查询活动报名
	@ApiOperation(value = "查询活动报名")
	//@RequiresPermissions("ty.tyActivitysign.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,@RequestParam(value = "actId", required = false) String actId) {
		List<TyActivitysignBean> list = tyActivitysignService.queryByActId(actId);
		Map<String,String> signStatusMap = sysDicService.queryDicByDicIndexKey("SIGNSTATUS");
		Map<String,List<DictItem>> dicts = null;
		for (TyActivitysignBean tyActivitysignBean : list) {
			if(StringUtils.isNotBlank(tyActivitysignBean.getSignStatus())) {
				tyActivitysignBean.setSignStatusText(signStatusMap.get(tyActivitysignBean.getSignStatus()));
			}
		}
		
		
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}
	
	// 查询活动报名(分页查询)
	@ApiOperation(value = "查询活动报名.分页查询")
	//@RequiresPermissions("ty.tyActivitysign.read")
	@RequestMapping(value = "/read/pagelist")
	public Object getPage(HttpServletRequest request, ModelMap modelMap,@RequestParam(value = "activityType[]", required = false) List<String> activityTypes) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("activityTypes", activityTypes);
		PageInfo<TyActivitysignBean> list = tyActivitysignService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		Map<String,String> signStatusMap = sysDicService.queryDicByDicIndexKey("SIGNSTATUS");
		addDictFromMap(dicts, "signStauts", signStatusMap);
		List<TyStafflist> researcherList = tyStafflistService.getResearcherRecords();
		addDictFromModel(dicts,"Researchers", researcherList,"id","staffName");
		for (TyStafflist researcher : researcherList) {
			researcher.setStaffName(researcher.getStaffName()+"(" + researcher.getDeptName() + ")");
		}
		addDictFromModel(dicts,"ResearchersEx", researcherList,"id","staffName");
		
		List<SysDept> sysDepts = sysDeptService.getResearchRecords();
		addDictFromModel(dicts,"Researchdept", sysDepts,"id","deptName");
		
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		
		//添加字典翻译
		Map<String,String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
		addDictFromMap(dicts,"TOPICTYPE", topictypeMap);
		
		Map<String,List<ExDictItem>> dictsEx = new HashMap<String,List<ExDictItem>>();
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords(); //*** 找销售人员
		List<ExDictItem> staffitems = new ArrayList<ExDictItem>();
		for (Iterator iterator = tyStafflists.iterator(); iterator.hasNext();) {
			TyStafflist tyStafflist = (TyStafflist) iterator.next();
			ExDictItem item = new ExDictItem();
			item.setId(tyStafflist.getId());
			item.setText(tyStafflist.getStaffName());
			item.setRemark(tyStafflist.getWorkAreaid());
			staffitems.add(item);
		}
		dictsEx.put("TyStafflists", staffitems);
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
		dictsEx.put("TyServiceorgs", orgitems);
		
		
		for (TyActivitysignBean tyActivitysignBean : list.getList()) {
			if(StringUtils.isNotBlank(tyActivitysignBean.getSignStatus())) {
				tyActivitysignBean.setSignStatusText(signStatusMap.get(tyActivitysignBean.getSignStatus()));
			}
			if(StringUtils.isNotBlank(tyActivitysignBean.getActivityId())) {
				tyActivitysignBean.setTyActivity(tyActivityService.queryById(tyActivitysignBean.getActivityId()));
			}
			if(StringUtils.isNotBlank(tyActivitysignBean.getActivityId())) {
				List<TyStafflist> staffList = tyStafflistService.queryByActId(tyActivitysignBean.getActivityId());
				if(staffList!=null && staffList.size()>0){
					tyActivitysignBean.setStaffList(staffList);
				}
			}
		}
		
		
		//添加字典翻译
		return setSuccessModelMapEx(modelMap, list,dicts,dictsEx);
	}

	// 详细信息
	@ApiOperation(value = "活动报名详情")
	//@RequiresPermissions("ty.tyActivitysign.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivitysignBean tyDcsaloncustBean = new TyActivitysignBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivitysign record = tyActivitysignService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDcsaloncustBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDcsaloncustBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDcsaloncustBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyDcsaloncustBean,dicts);
	}

	
	// 新增活动报名
	@ApiOperation(value = "添加活动报名")
	//@RequiresPermissions("ty.tyActivitysign.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds", required = true) List<String> custIds,
			@RequestParam(value = "activityId", required = true) String activityId,
			@RequestParam(value = "signId", required = true) String signId,
			@RequestParam(value = "signName", required = true) String signName) {
		if(StringUtils.isBlank(activityId) || StringUtils.isBlank(signId) 
				|| custIds.size() <= 0 || custIds.size() > 2){
			throw new IllegalParameterException("参数异常，请检查参数");
		}
		//获取销售当前活动的记录条数，大于2，或者超过最大报名数则不能报名
		TyActivity acitivity = tyActivityService.queryById(activityId);
		if(acitivity == null ){
			throw new IllegalArgumentException("活动都不能为空,请检查activityid参数是否正确!");
		}
		TyStafflist staff = tyStafflistService.queryById(WebUtil.getCurrentUser());
		if(staff == null){
			throw new IllegalArgumentException("当前用户不存在！");
		}
		//查询当前销售在当前活动中的报名人数
		List<TyActivitysignBean> signs = tyActivitysignService.queryByActId(activityId);
		Map<String,TyActivitysignBean> signMap = toSignMap(signs);
		int signTotalSize = 0;
		for(TyActivitysignBean sign: signs){
			// 2 状态为放弃
			if(!sign.getSignStatus().equals("2")){
				signTotalSize ++;
			}
		}

		Iterator<String> iter = custIds.iterator();
		while (iter.hasNext()) {
			String custId = iter.next();
			if(signMap.containsKey(custId)){
				iter.remove();
			}
		}
		int needAddSize = custIds.size();
		
		if(signs != null && acitivity.getPerLimit() != null && (signTotalSize + needAddSize)> acitivity.getPerLimit()  ){
			throw new BusinessException("活动剩余人数不足！");
		}
		
		
		//添加
		TyActivitysign record = null;
		for (String custId : custIds) {
			if(signMap.containsKey(custId)){
				continue;
			}

			Date c = new Date();
			record = new TyActivitysign();
			TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
			record.setActivityId(activityId);
			record.setCustName(customer.getCustName() == null ? "" : customer.getCustName());
			record.setOrgName(customer.getOrgName() == null ? "" : customer.getOrgName());
			record.setOrgSimpleName(customer.getOrgName() == null ? "" : customer.getOrgName());
			record.setCustId(customer.getId());
			record.setOrgId(customer.getOrgId());
			record.setCustMobile(customer.getCustMobile() == null ? "" : customer.getCustMobile());
			record.setInWhitelist("");   //是否是白名单客户
			record.setSignId(signId);
			record.setSignName(signName);
			record.setSignStatus("0");
			record.setSignDate(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.YYYYMMDD)));
			record.setSignTime(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.HHMMSS)));
			record.setDeptId(staff.getDeptId());
			tyActivitysignService.add(record);
		}
		
		//如果人数达到上限，则关闭当前活动
		if(acitivity.getPerLimit() != null && (signTotalSize + needAddSize ) >= acitivity.getPerLimit() ){
			acitivity.setActivityStatus("1");
			tyActivityService.update(acitivity);
		}
		return setSuccessModelMap(modelMap);
	}
	
	private Map<String,TyActivitysignBean> toSignMap(List<TyActivitysignBean> signs){
		Map<String,TyActivitysignBean> results = new HashMap<String,TyActivitysignBean>();
		for(TyActivitysignBean sign:signs){
			results.put(sign.getCustId(), sign);
		}
		return results;
	}

	
	// 客户详情-新增活动服务记录
	@ApiOperation(value = "新增活动服务记录")
	@RequestMapping(value = "/add/actSignServiceLog", method = RequestMethod.POST)
	public Object addActStaffLog(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custId", required = true) String custId,
			@RequestParam(value = "signId", required = true) String signId,
			@RequestParam(value = "signName", required = true) String signName,
			@RequestParam(value = "activityIds", required = true) List<String> activityIds) throws IllegalArgumentException {
		if(StringUtils.isBlank(custId)|| StringUtils.isBlank(signId)|| StringUtils.isBlank(signName)){
			throw new IllegalArgumentException("参数异常，请检查参数");
		}
		TyStafflist staff = tyStafflistService.queryById(WebUtil.getCurrentUser());
		if(staff == null){
			throw new IllegalArgumentException("当前用户不存在！");
		}
		if(activityIds.size() > 0){
			TyActivitysign actSign = null;
			Date c = new Date();
			for (String activityId : activityIds) {
				TyActivity activity = tyActivityService.queryById(activityId);
				TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
				if(!StringUtils.equals(activity.getActivityStatus(), "0") && customer != null){
					actSign = new TyActivitysign();
					actSign.setActivityId(activity.getId());
					actSign.setCustName(customer.getCustName() == null ? "" : customer.getCustName());
					actSign.setOrgName(customer.getOrgName() == null ? "" : customer.getOrgName());
					actSign.setOrgSimpleName(customer.getOrgName() == null ? "" : customer.getOrgName());
					actSign.setCustId(customer.getId());
					actSign.setOrgId(customer.getOrgId());
					actSign.setCustMobile(customer.getCustMobile() == null ? "" : customer.getCustMobile());
					actSign.setInWhitelist("");   //是否是白名单客户
					actSign.setSignId(signId);
					actSign.setSignName(signName);
					actSign.setSignStatus("0");
					actSign.setSignDate(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.YYYYMMDD)));
					actSign.setSignTime(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.HHMMSS)));
					actSign.setDeptId(staff.getDeptId());
					tyActivitysignService.add(actSign);
				}
			}
		}
		return setSuccessModelMap(modelMap);
	}
	
	
	
	// 修改活动报名
	@ApiOperation(value = "修改活动报名")
	//@RequiresPermissions("ty.tyActivitysign.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds,
			@RequestParam(value = "actId", required = true) String actId) {
//		tyActivitysignService.deleteByActId(actId);
		addActivitysign(actId,custIds);
		return setSuccessModelMap(modelMap);
	}
	// 修改活动报名状态
	@ApiOperation(value = "修改活动报名状态")
	//@RequiresPermissions("ty.tyActivitysign.update")
	@RequestMapping(value = "/updateSignStatus", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custId", required = true)  String custId,
			@RequestParam(value = "actId", required = true) String actId,
			@RequestParam(value = "signStatus", required = true) String signStatus) {
		if(StringUtils.isBlank(custId) || StringUtils.isBlank(actId) || StringUtils.isBlank(signStatus)){
			throw new IllegalParameterException("参数错误，请检查参数!");
		}
		Map<String,Object> params = new HashMap<>();
		params.put("activityId", actId);
		params.put("custId", custId);
		List<TyActivitysignBean> signList = tyActivitysignService.queryByCondition(params);
		if(signList != null && signList.size() == 1){
			TyActivitysignBean activitysignBean = signList.get(0);
			activitysignBean.setSignStatus(signStatus);
			tyActivitysignService.update(activitysignBean);
		}
		return setSuccessModelMap(modelMap);
	}
	/**
	 * 添加参加活动的客户信息
	 * @param actId
	 * @param custIds
	 */
	private void addActivitysign(String actId,List<String> custIds){
		if(custIds!=null && custIds.size()>0){
			Map<String,Object> params = new HashMap<>();
			params.put("activityId",actId);
			List<TyActivitysignBean> queryByCondition = tyActivitysignService.queryByActId(actId);
			Set<String> signedCustomerIds = new HashSet<>();
			for (TyActivitysignBean signBean : queryByCondition) {
				signedCustomerIds.add(signBean.getCustId());
			}
			for (String custId : custIds) {
				//不存在时才进行插入动作
				if(!signedCustomerIds.contains(custId)) {
					TyActivitysign topicCust = new TyActivitysign();
					
					TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
					TyStafflist staff = tyStafflistService.queryById(WebUtil.getCurrentUser());
					Date c = new Date();
					if(customer!=null){
						topicCust.setCustId(custId);
						topicCust.setCustName(customer.getCustName() == null ? "" : customer.getCustName());
						topicCust.setCustMobile(customer.getCustMobile() == null ? "" : customer.getCustMobile());
						topicCust.setActivityId(actId);
						topicCust.setOrgName(customer.getOrgName() == null ? "" : customer.getOrgName());
						topicCust.setOrgSimpleName(customer.getOrgName() == null ? "" : customer.getOrgName());
						topicCust.setOrgId(customer.getOrgId());
						topicCust.setInWhitelist("");   //是否是白名单客户
						topicCust.setSignId(WebUtil.getCurrentUser());
						topicCust.setSignName(staff.getStaffName());
						topicCust.setSignStatus("0");
						topicCust.setSignDate(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.YYYYMMDD)));
						topicCust.setSignTime(Integer.parseInt(DateUtil.format(c, DateUtil.DATE_PATTERN.HHMMSS)));
						topicCust.setDeptId(staff.getDeptId());
						tyActivitysignService.add(topicCust);
					}
				}else {
					signedCustomerIds.remove(custId);
				}
			}
			//删除还在集合中存在的旧记录
			if(!CollectionUtils.isEmpty(signedCustomerIds)) {
				Map<String,String> queryParams = null;
				for (String signedCustId : signedCustomerIds) {
					queryParams = new HashMap<>();
					queryParams.put("activityId",actId);
					queryParams.put("custId",signedCustId);
					List<TyActivitysignBean> singedRecord = tyActivitysignService.queryByCondition(params);
					if(!CollectionUtils.isEmpty(singedRecord)) {
						tyActivitysignService.deletePhysical(singedRecord.get(0).getId());
					}
				}
			}
		}
	}
	
	// 删除活动报名
	@ApiOperation(value = "删除活动报名")
	//@RequiresPermissions("ty.tyActivitysign.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivitysign record = tyActivitysignService.queryById(id);
		if(record!=null){
			tyActivitysignService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
