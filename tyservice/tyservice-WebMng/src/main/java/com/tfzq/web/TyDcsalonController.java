package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.tfzq.service.TyActivitystaffService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyDcactivity;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyActivitystaff;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 沙龙控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "沙龙管理", description = "沙龙管理")
@RequestMapping(value = "ty/tyDcsalon", method = RequestMethod.POST)
public class TyDcsalonController extends BaseController {
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyActivitystaffService tyActivitystaffService;

	// 查询沙龙
	@ApiOperation(value = "查询沙龙")
	//@RequiresPermissions("ty.tyDcsalon.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("subActFlag","0"); //顶级活动
		params.put("activityType",Constants.SALON);
		params.put("orderBy","begin_date desc,update_time desc,id desc");
		PageInfo<TyActivityBean> list = tyActivityService.queryBeans(params);
		
		//添加字典翻译
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<TyStafflist> researcherList = tyStafflistService.getResearcherRecords();
		addDictFromModel(dicts,"Researchers", researcherList,"id","staffName");
		for (TyStafflist researcher : researcherList) {
			researcher.setStaffName(researcher.getStaffName()+"(" + researcher.getDeptName() + ")");
		}
		addDictFromModel(dicts,"ResearchersEx", researcherList,"id","staffName");
		
		List<SysDept> sysDepts = sysDeptService.getResearchRecords();
		addDictFromModel(dicts,"Researchdept", sysDepts,"id","deptName");
		
		if(list.getList().size()>0){
			for (TyActivityBean singleBean : list.getList()) {
				String actId = singleBean.getId();
				List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
				singleBean.setStaffList(staffList);
				
				List<TyActivitysignBean> custList = tyActivitysignService.queryByActId(actId);
				singleBean.setCustList(custList);
				//查询当前活动的子活动
				if(StringUtils.equals(singleBean.getSubActFlag(), "0")){ //>0 : 独立活动  顶层父活动
					Map<String,Object>params1 = new HashMap<>();     
					params1.put("deleteFlag", "0");
					params1.put("parentActId", singleBean.getId());
					params1.put("activityType",Constants.SALON);
					params1.put("orderBy","begin_date desc,update_time desc,id desc");
					List<TyActivityBean> sublist = tyActivityService.queryByCondition(params1);
					singleBean.setSubActList(sublist);
					getSubActListRecursively(params1, sublist);
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	
	//递归获取子活动
	private void getSubActListRecursively(Map<String, Object> params1, List<TyActivityBean> sublist) {
		if(!CollectionUtils.isEmpty(sublist)){
			for (TyActivityBean tyActivityBean : sublist) {
				params1.put("parentActId", tyActivityBean.getId());
				List<TyActivityBean> sublist2 = tyActivityService.queryByCondition(params1);
				tyActivityBean.setSubActList(sublist2);
				getSubActListRecursively(params1, sublist2);
			}
		}
	}

	// 详细信息
	@ApiOperation(value = "沙龙详情")
	//@RequiresPermissions("ty.tyDcsalon.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivityBean tyDcsalonBean = new TyActivityBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivity record = tyActivityService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDcsalonBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDcsalonBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDcsalonBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyDcsalonBean,dicts);
	}

	
	// 新增沙龙
	@ApiOperation(value = "添加沙龙")
	//@RequiresPermissions("ty.tyDcsalon.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		record.setActivityType(Constants.SALON);
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
	
	
	// 修改沙龙
	@ApiOperation(value = "修改沙龙")
	//@RequiresPermissions("ty.tyDcsalon.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "userIds[]", required = true) List<String> userIds,
			@RequestParam(value = "parentActId", required = false) String parentActId,
			@RequestParam(value = "subActFlag", required = false) String subActFlag
			) {
		TyActivity record = Request2ModelUtil.covert(TyActivity.class, request);
		if(StringUtils.isNotBlank(parentActId)){
			//设置子活动的parentId和活动类型
			record.setParentActId(parentActId);
			record.setSubActFlag(subActFlag);  //限制两层沙龙活动嵌套
		}
		record.setActivityType(Constants.SALON);
		TyActivityBean tyDcsalonBean = new TyActivityBean();
		tyActivityService.update(record);
		tyActivitystaffService.deleteByActId(record.getId());
		TyActivity newCon = tyActivityService.queryById(record.getId());
		try {
            BeanUtils.copyProperties(tyDcsalonBean, newCon);
			
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        } catch (InvocationTargetException e) {
            e.printStackTrace();  
        }catch (Exception e) {
			tyDcsalonBean = null;
        	e.printStackTrace();
        }
		
		addActivitystaff(record.getId(),userIds);

		String actId = record.getId();
		List<TyStafflist> staffList = tyStafflistService.queryByActId(actId);
		tyDcsalonBean.setStaffList(staffList);
		
		return setSuccessModelMap(modelMap,tyDcsalonBean);
	}

	// 删除沙龙
	@ApiOperation(value = "删除沙龙")
	//@RequiresPermissions("ty.tyDcsalon.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivity record = tyActivityService.queryById(id);
		if(record!=null){
			record.setDeleteFlag("1");
			tyActivityService.update(record);
//			tyActivityService.deletePhysical(id);
			tyActivitysignService.deleteByActId(id);
			tyActivitystaffService.deleteByActId(id);
			//删除对应的子活动
			if(StringUtils.equals(record.getSubActFlag(), "0")){
				Map<String,Object> params1 = new HashMap<>();
				params1.put("deleteFlag", "0");
				params1.put("parentActId", record.getId());
				params1.put("activityType",Constants.SALON);
				List<TyActivityBean> sublist = tyActivityService.queryByCondition(params1);
				for (TyActivityBean tyActivityBean : sublist) {
					tyActivityBean.setDeleteFlag("1");
					tyActivityService.update(tyActivityBean);
				}
			}
		}
		return setSuccessModelMap(modelMap);
	}
}
