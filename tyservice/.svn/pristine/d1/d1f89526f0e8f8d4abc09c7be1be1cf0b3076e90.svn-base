package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDept;
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
import com.tfzq.service.TyActivitystaffService;
import com.tfzq.service.TyDcrecostockService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.generator.TyDcactivity;
import com.tfzq.ty.model.generator.TyDcmorningcon;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyDcrecostockBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 晨会发言控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "晨会发言管理", description = "晨会发言管理")
@RequestMapping(value = "ty/tyDcmorningcon", method = RequestMethod.POST)
public class TyDcmorningconController extends BaseController {
//	@Autowired
//	private TyDcmorningconService tyDcmorningconService;
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyDcrecostockService tyDcrecostockService;
//	@Autowired
//	private TyDcactivityService tyDcactivityService;
	@Autowired
	private TyActivitystaffService tyActivitystaffService;
	@Autowired
	private SysDeptService sysDeptService;
	

	// 查询晨会发言
	@ApiOperation(value = "查询晨会发言")
	//@RequiresPermissions("ty.tyDcmorningcon.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		//startDate,endDate,staffName,deptName
		params.put("deleteFlag", "0");
		params.put("activityType",Constants.MORNING_CON);
		params.put("orderBy","begin_date desc,update_time desc,id desc");
//		PageInfo<TyDcmorningconBean> list = tyDcmorningconService.queryBeans(params);
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
		
		Map<String, String> recommendTypeMap = sysDicService.queryDicByDicIndexKey("RECOMMENDTYPE");
		addDictFromMap(dicts, "recommendTypes", recommendTypeMap);
		//添加字典翻译
		if(list.getList().size()>0){
			for (TyActivityBean singleBean : list.getList()) {
				String actId = singleBean.getId();
				List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
				if(staffList!=null && staffList.size()>0){
					singleBean.setStaffList(staffList);
				}
				
				List<TyDcrecostockBean> stockList = tyDcrecostockService.queryBySpeakId(actId);
				if(stockList!=null && stockList.size()>0){
					for (TyDcrecostockBean stockBean : stockList) {
						if (StringUtils.isNotEmpty(stockBean.getRecommendType())) {
							stockBean.setRecommendTypeName(recommendTypeMap.get(stockBean.getRecommendType().toString()));
						}
					}
					singleBean.setStockList(stockList);
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "晨会发言详情")
	//@RequiresPermissions("ty.tyDcmorningcon.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivityBean tyDcmorningconBean = new TyActivityBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivity record = tyActivityService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDcmorningconBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDcmorningconBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDcmorningconBean!=null){
	    	
	    }
	    
		return setSuccessModelMap(modelMap, tyDcmorningconBean,dicts);
	}

	
	// 新增晨会发言
	@ApiOperation(value = "添加晨会发言")
	//@RequiresPermissions("ty.tyDcmorningcon.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		record.setActivityType(Constants.MORNING_CON);
		record.setSubActFlag("0");
		record.setDataSrc("0");
		TyActivity newCon = tyActivityService.add(record);
		addActivitystaff(newCon.getId(),userIds);
		return setSuccessModelMap(modelMap);
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
	// 修改晨会发言
	@ApiOperation(value = "修改晨会发言")
	//@RequiresPermissions("ty.tyDcmorningcon.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		TyActivityBean tyDcmorningconBean = new TyActivityBean();
		tyActivityService.update(record);
		tyActivitystaffService.deleteByActId(record.getId());
		TyActivity newCon = tyActivityService.queryById(record.getId());
		try {
            BeanUtils.copyProperties(tyDcmorningconBean, newCon);
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        } catch (InvocationTargetException e) {
            e.printStackTrace();  
        }catch (Exception e) {
			tyDcmorningconBean = null;
        	e.printStackTrace();
        }
		addActivitystaff(newCon.getId(),userIds);

		String actId = record.getId();
		List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
		if(staffList!=null && staffList.size()>0){
			tyDcmorningconBean.setStaffList(staffList);
		}
		return setSuccessModelMap(modelMap,tyDcmorningconBean);
	}

	// 删除晨会发言
	@ApiOperation(value = "删除晨会发言")
	//@RequiresPermissions("ty.tyDcmorningcon.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivity record = tyActivityService.queryById(id);
		if(record!=null){
			tyActivityService.deletePhysical(id);
			tyActivitystaffService.deleteByActId(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
