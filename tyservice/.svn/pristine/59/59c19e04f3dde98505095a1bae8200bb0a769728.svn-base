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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysPositionroleService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyMessageauditService;
import com.tfzq.service.TyMessagelogService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyMessageaudit;
import com.tfzq.ty.model.ty.TyMessageauditBean;
import com.tfzq.ty.model.ty.TyStafflistBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 信息审核控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "信息审核管理", description = "信息审核管理")
@RequestMapping(value = "ty/tyMessageaudit", method = RequestMethod.POST)
public class TyMessageauditController extends BaseController {
	@Autowired
	private TyMessageauditService tyMessageauditService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private TyMessagelogService tyMessagelogService;
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private TyStafflistService tyStafflistService;

	// 查询信息审核
	@ApiOperation(value = "查询信息审核")
	@RequiresPermissions("ty.tyMessageaudit.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		//已有角色
        List<SysUserRoleBean> userrole=sysRoleService.queryRoleByUserId(currUser.getId());
        if(isSeniorseller(userrole)){//如果是高级销售
        	//1 找出所有销售职位
        	List<String> roleList = new ArrayList<String>();
        	roleList.add("seniorseller");
        	roleList.add("seller");
        	Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("roleList", roleList);
			params2.put("pageSize",1000);
			PageInfo<SysPositionroleBean> spr = sysPositionroleService.queryBeans(params2);
			List<String> positionList = new ArrayList<String>();
			for (Iterator iterator = spr.getList().iterator(); iterator.hasNext();) {
				SysPositionroleBean sprbean = (SysPositionroleBean) iterator.next();
				positionList.add(sprbean.getPositionId());
			}
			//2 找出所有销售职位对应的销售人员
			Map<String, Object> params3 = new HashMap<String, Object>();
			params3.put("positionList", positionList);
			params3.put("pageSize",1000);
			
			Map<String, Object> params4 = new HashMap<String, Object>();
			params4.put("userId", currUser.getAccount());
			PageInfo<TyStafflistBean> staffs = tyStafflistService.queryBeans(params4);
			params3.put("workAreaid", staffs.getList().get(0).getWorkAreaid());
			
			PageInfo<TyStafflistBean> stafflist = tyStafflistService.queryBeans(params3);
			List<String> salerList = new ArrayList<String>();
			for (Iterator iterator = stafflist.getList().iterator(); iterator
					.hasNext();) {
				TyStafflistBean staffBean = (TyStafflistBean) iterator.next();
				salerList.add(staffBean.getId());
			}
			params.put("salerList",salerList); //设置条件
        }else if(isSeller(userrole)){//如果是一般销售
        	Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("userId", currUser.getAccount());
			PageInfo<TyStafflistBean> stafflist = tyStafflistService.queryBeans(params2);
			params.put("salerId",stafflist.getList().get(0).getId()); //设置条件
        }
		
		
		PageInfo<TyMessageauditBean> list = tyMessageauditService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		if(list.getList().size()>0){
			for (TyMessageauditBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getCreateBy())) {
					SysUser currUser2 = sysUserService.queryById(singleBean.getCreateBy());
					if(currUser2!=null && currUser2.getUserName()!=null){
						singleBean.setCreateName(currUser2.getUserName());
					}
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "信息审核详情")
	@RequiresPermissions("ty.tyMessageaudit.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyMessageauditBean tyMessageauditBean = new TyMessageauditBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyMessageaudit record = tyMessageauditService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyMessageauditBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyMessageauditBean = null;
	        	e.printStackTrace();
	        }
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("msgAuditid", id);
			List<String> list = tyMessagelogService.queryReceiverName(params);
			if(list!=null){
				tyMessageauditBean.setReceiverNames(list);
			}
	    }
		//添加字典翻译
	    if(tyMessageauditBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyMessageauditBean,dicts);
	}

	
	// 新增信息审核
	@ApiOperation(value = "添加信息审核")
	@RequiresPermissions("ty.tyMessageaudit.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyMessageaudit record = Request2ModelUtil.covert(TyMessageaudit.class, request);
		tyMessageauditService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改信息审核
	@ApiOperation(value = "修改信息审核")
	@RequiresPermissions("ty.tyMessageaudit.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyMessageaudit record = Request2ModelUtil.covert(TyMessageaudit.class, request);
		tyMessageauditService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除信息审核
	@ApiOperation(value = "删除信息审核")
	@RequiresPermissions("ty.tyMessageaudit.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyMessageaudit record = tyMessageauditService.queryById(id);
		if(record!=null){
			tyMessageauditService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	// 审核通过
	@ApiOperation(value = "审核通过")
	@RequiresPermissions("ty.tyMessageaudit.update")
	@RequestMapping(value = "/pass", method = RequestMethod.POST)
	public Object pass(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "remark", required = false) String remark) {
		TyMessageaudit record = tyMessageauditService.queryById(id);
		if(record!=null){
			SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
			record.setAuditFlag("1");
			record.setRemark(remark);
			record.setAuditBy(currUser.getUserName());
			record.setAuditTime(new Date());
			tyMessageauditService.update(record);
		}
		return setSuccessModelMap(modelMap);
	}
	// 审核不通过
	@ApiOperation(value = "审核不通过")
	@RequiresPermissions("ty.tyMessageaudit.update")
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public Object reject(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "remark", required = false) String remark) {
		TyMessageaudit record = tyMessageauditService.queryById(id);
		if(record!=null){
			SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
			
			record.setAuditFlag("2");
			record.setRemark(remark);
			record.setAuditBy(currUser.getUserName());
			record.setAuditTime(new Date());
			tyMessageauditService.update(record);
		}
		return setSuccessModelMap(modelMap);
	}
	
	private boolean isSeniorseller(List<SysUserRoleBean> surb){
		boolean result=false;
		for (Iterator<SysUserRoleBean> iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if("seniorseller".equals(sysUserRoleBean.getRoleId())){
				result=true;
				break;
			}
		}
		return result;
	}
	private boolean isSeller(List<SysUserRoleBean> surb){
		boolean result=false;
		for (Iterator<SysUserRoleBean> iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if("seller".equals(sysUserRoleBean.getRoleId())){
				result=true;
				break;
			}
		}
		return result;
	}

}
