package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyActivitystaffService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 课题控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "课题管理", description = "课题管理")
@RequestMapping(value = "ty/tyDctopic", method = RequestMethod.POST)
public class TyDctopicController extends BaseController {
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
//	@Autowired
//	private TyDcactivityService tyActivitystaffService;
	@Autowired
	private TyActivitystaffService tyActivitystaffService;
	@Autowired
	private SysAreaService sysAreaService;

	// 查询课题
	@ApiOperation(value = "查询课题")
	//@RequiresPermissions("ty.tyDctopic.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("activityType",Constants.ENTRUST_TOPIC);
		params.put("orderBy","begin_date desc,update_time desc,id desc");
		PageInfo<TyActivityBean> list = tyActivityService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
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
		
		for (TyActivityBean singleBean : list.getList()) {
			String actId = singleBean.getId();
			List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
			if(staffList!=null && staffList.size()>0){
				singleBean.setStaffList(staffList);
			}
			
			List<TyActivitysignBean> topicCustList = tyActivitysignService.queryByActId(actId);
			singleBean.setCustList(topicCustList);
			
			if (StringUtils.isNotEmpty(singleBean.getTopicType())) {
				singleBean.setTopicTypeText(topictypeMap.get(singleBean.getTopicType().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(singleBean.getOrgId());
				singleBean.setTeamCat(temp==null?"":temp.getTeamCat());
				singleBean.setServiceSaler(temp==null?"":temp.getServiceSaler());
				singleBean.setOrgName(temp==null?"":temp.getOrgName());
			}
		}
		return setSuccessModelMapEx(modelMap, list,dicts,dictsEx);
	}

	// 详细信息
	@ApiOperation(value = "课题详情")
	//@RequiresPermissions("ty.tyDctopic.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivityBean tyDctopicBean = new TyActivityBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyActivity record = tyActivityService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDctopicBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDctopicBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
		addDictFromMap(dicts,"TOPICTYPE", topictypeMap);
		List<TyServiceorg> tyServiceorgs = tyServiceorgService.getAllRecords();
		addDictFromModel(dicts,"TyServiceorgs", tyServiceorgs,"id","orgName");
	    if(tyDctopicBean!=null){
			if (StringUtils.isNotEmpty(tyDctopicBean.getTopicType())) {
				tyDctopicBean.setTopicTypeText(topictypeMap.get(tyDctopicBean.getTopicType().toString()));
			}
			if(StringUtils.isNotEmpty(tyDctopicBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(tyDctopicBean.getOrgId());
				tyDctopicBean.setOrgName(temp==null?"":temp.getOrgName());
			}
	    }
	    
		return setSuccessModelMap(modelMap, tyDctopicBean,dicts);
	}

	
	// 新增课题
	@ApiOperation(value = "添加课题")
	//@RequiresPermissions("ty.tyDctopic.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		record.setActivityType(Constants.ENTRUST_TOPIC);
		record.setSubActFlag("0");
		record.setDataSrc("0");
		TyActivity newCon = tyActivityService.add(record);
		addActivitystaff(newCon.getId(),userIds);
		addActivitysign(newCon.getId(),custIds);
		return setSuccessModelMap(modelMap);
	}

	// 修改课题
	@ApiOperation(value = "修改课题")
	//@RequiresPermissions("ty.tyDctopic.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		record.setActivityType(Constants.ENTRUST_TOPIC);
		TyActivityBean tyDctopicBean = new TyActivityBean();
		tyActivityService.update(record);
		String actId = record.getId();
		tyActivitystaffService.deleteByActId(record.getId());
		tyActivitysignService.deleteByActId(record.getId());
		TyActivity newCon = tyActivityService.queryById(record.getId());
		try {
            BeanUtils.copyProperties(tyDctopicBean, newCon);
			
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        } catch (InvocationTargetException e) {
            e.printStackTrace();  
        }catch (Exception e) {
			tyDctopicBean = null;
        	e.printStackTrace();
        }
		//添加字典翻译
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		Map<String,String> topictypeMap = sysDicService.queryDicByDicIndexKey("TOPICTYPE");
		addDictFromMap(dicts,"TOPICTYPE", topictypeMap);
		
		addActivitystaff(actId,userIds);
		List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
		if(staffList!=null && staffList.size()>0){
			tyDctopicBean.setStaffList(staffList);
		}

		addActivitysign(actId,custIds);
		List<TyActivitysignBean> topicCustList = tyActivitysignService.queryByActId(actId);
		tyDctopicBean.setCustList(topicCustList);
		if (StringUtils.isNotEmpty(tyDctopicBean.getTopicType())) {
			tyDctopicBean.setTopicTypeText(topictypeMap.get(tyDctopicBean.getTopicType().toString()));
		}
		if(StringUtils.isNotEmpty(tyDctopicBean.getOrgId())){
			TyServiceorg temp = tyServiceorgService.queryById(tyDctopicBean.getOrgId());
			tyDctopicBean.setTeamCat(temp==null?"":temp.getTeamCat());
			tyDctopicBean.setServiceSaler(temp==null?"":temp.getServiceSaler());
			tyDctopicBean.setOrgName(temp==null?"":temp.getOrgName());
		}
		
		return setSuccessModelMap(modelMap,tyDctopicBean);
	}
	/**
	 * 添加参加活动的员工信息
	 * @param actId
	 * @param userIds
	 */
	private void addActivitystaff(String actId,List<String> userIds){
		if(userIds!=null && userIds.size()>0){
			for (String userId : userIds) {
				TyActivitystaff act = new TyActivitystaff();
				act.setActivityId(actId);
				TyStafflist staff = tyStafflistService.queryById(userId);
				if(staff!=null){
					act.setStaffId(staff.getId());
					act.setStaffName(staff.getStaffName());
				}
				tyActivitystaffService.add(act);
			}
		}
	}
	
	/**
	 * 添加参加活动的客户信息
	 * @param actId
	 * @param custIds
	 */
	private void addActivitysign(String actId,List<String> custIds){
		if(custIds!=null && custIds.size()>0){
			for (String custId : custIds) {
				TyActivitysign topicCust = new TyActivitysign();
				topicCust.setActivityId(actId);
				TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
				if(customer!=null){
					topicCust.setCustId(custId);
					topicCust.setCustName(customer.getCustName());
					tyActivitysignService.add(topicCust);
				}
			}
		}

	}

	// 删除课题
	@ApiOperation(value = "删除课题")
	//@RequiresPermissions("ty.tyDctopic.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivity record = tyActivityService.queryById(id);
		if(record!=null){
			tyActivityService.deletePhysical(id);
			tyActivitystaffService.deleteByActId(id);
			tyActivitysignService.deleteByActId(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
