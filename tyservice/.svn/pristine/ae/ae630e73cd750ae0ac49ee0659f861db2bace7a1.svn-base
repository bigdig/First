package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.tfzq.service.TyCustgroupService;
import com.tfzq.service.TyCustgroupdetailService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyCustgroup;
import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupBean;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyStafflistBean;

/**
 * 客户分组控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "客户分组管理", description = "客户分组管理")
@RequestMapping(value = "ty/tyCustgroup", method = RequestMethod.POST)
public class TyCustgroupController extends BaseController {
	private static Logger logger = LogManager.getLogger(TyCustgroupController.class);
	@Autowired
	private TyCustgroupService tyCustgroupService;
	@Autowired
	private TyCustgroupdetailService tyCustgroupdetailService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;

	// 查询客户分组
	@ApiOperation(value = "查询客户分组")
	//@RequiresPermissions("ty.tyServiceorg.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id asc");
		params.put("pageSize", 10000);
		params.put("userID", WebUtil.getCurrentUser());
		PageInfo<TyCustgroupBean> list = tyCustgroupService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}
	
	// 查询客户分组明细
	@ApiOperation(value = "查询客户分组明细")
	//@RequiresPermissions("ty.tyServiceorg.read")
	@RequestMapping(value = "/read/detailList")
	public Object detailList(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyCustgroupdetailBean> list = tyCustgroupdetailService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "客户分组详情")
	//@RequiresPermissions("ty.tyServiceorg.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyCustgroupBean tyCustgroupBean = new TyCustgroupBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyCustgroup record = tyCustgroupService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyCustgroupBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyCustgroupBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyCustgroupBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyCustgroupBean,dicts);
	}

	
	// 新增客户分组
	@ApiOperation(value = "添加客户分组")
	//@RequiresPermissions("ty.tyServiceorg.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyCustgroup record = Request2ModelUtil.covert(TyCustgroup.class, request);
		TyCustgroup newRecord = tyCustgroupService.add(record);
		return setSuccessModelMap(modelMap,newRecord);
	}

	// 修改客户分组
	@ApiOperation(value = "修改客户分组")
	//@RequiresPermissions("ty.tyServiceorg.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyCustgroup record = Request2ModelUtil.covert(TyCustgroup.class, request);
		tyCustgroupService.update(record);
		return setSuccessModelMap(modelMap);
	}
	
	// 批量修改客户分组明细
	@ApiOperation(value = "批量修改客户分组明细")
	//@RequiresPermissions("ty.tyServiceorg.update")
	@RequestMapping(value = "/batchUpdateGroupDetail", method = RequestMethod.POST)
	public Object batchUpdateGroupDetail(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds[]", required = false) List<String> custIds,@RequestParam(value = "isAll", required = false) boolean isAll,
			String groupId,String groupName) {
		TyCustgroup group = new TyCustgroup();
		TyCustgroup groupAdd =new TyCustgroup();
		List<TyCustgroupdetail> groupDetailList = new ArrayList<TyCustgroupdetail>();
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		
		List<TyOrgcustomerBean> custlist = new ArrayList<TyOrgcustomerBean>();
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		params.put("pageSize", "100000");
		
		if(isAll){
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
			custlist = tyOrgcustomerService.queryByCondition(params);
			
			if(CollectionUtils.isNotEmpty(custlist)){
				custIds = new ArrayList<String>();
				for (TyOrgcustomerBean cu : custlist) {
					custIds.add(cu.getId());
				}
			}
		}
		//将客户添加到新分组
		if(StringUtils.isNotBlank(groupName)){ 
			group.setCustGroupname(groupName);
			group.setUserId(currUser.getId());
			group.setUserName(currUser.getUserName());
			groupAdd = tyCustgroupService.add(group);
			groupId = groupAdd.getId();
//			if(null!=groupAdd && StringUtils.isNotBlank(groupAdd.getId())){
//				for (String custId : custIds) {
//					TyCustgroupdetail groupDetail = new TyCustgroupdetail();
//					String key = groupDetail.getClass().getSimpleName();
//					groupDetail.setId(tyCustgroupdetailService.createId(key));
//					groupDetail.setGroupId(groupAdd.getId());
//					groupDetail.setCustId(custId);
////						tyCustgroupdetailService.add(groupDetail);
//					groupDetailList.add(groupDetail);
//				}
//				tyCustgroupdetailService.batchAdd(groupDetailList);
//			}
		} 
		//else 将客户添加到老分组
		
		if(StringUtils.isNotBlank(groupId)){
			for (String custId : custIds) {
				TyCustgroupdetail groupDetail = new TyCustgroupdetail();
				String key = groupDetail.getClass().getSimpleName();
				groupDetail.setId(tyCustgroupdetailService.createId(key));
				groupDetail.setGroupId(groupId);
				groupDetail.setCustId(custId);
//						tyCustgroupdetailService.add(groupDetail);
				groupDetailList.add(groupDetail);
			}
			tyCustgroupdetailService.batchAdd(groupDetailList);
		}
		return setSuccessModelMap(modelMap,groupAdd);
	}

	// 删除客户分组
	@ApiOperation(value = "删除客户分组")
	//@RequiresPermissions("ty.tyServiceorg.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyCustgroup record = tyCustgroupService.queryById(id);
		if(record!=null){
			tyCustgroupService.deletePhysical(id);
			
			tyCustgroupdetailService.deleteByGroupId(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	// 批量删除客户分组明细
	@ApiOperation(value = "批量删除客户分组明细")
	//@RequiresPermissions("ty.tyServiceorg.delete")
	@RequestMapping(value = "/batchDeleteDetail", method = RequestMethod.POST)
	public Object batchDeleteDetail(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds,
			String groupId) {
		if(StringUtils.isNotBlank(groupId)){
			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("pageSize", 10000);
			params.put("groupId", groupId);
			params.put("custIds", custIds);
			List<TyCustgroupdetailBean> list = tyCustgroupdetailService.queryByCondition(params);
			if(list!=null && list.size()>0){
				for (TyCustgroupdetailBean detail : list) {
					tyCustgroupdetailService.deletePhysical(detail.getId());
				}
			}
		}
		return setSuccessModelMap(modelMap);
	}
	
	// 批量客户移组
	@ApiOperation(value = "批量客户移组")
	//@RequiresPermissions("ty.tyServiceorg.update")
	@RequestMapping(value = "/batchRemoveDetail", method = RequestMethod.POST)
	public Object batchRemoveDetail(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds,
			@RequestParam(value = "srcGroupId", required = true) String srcGroupId,
			@RequestParam(value = "destGroupId", required = true) String destGroupId) {
		if(StringUtils.isNotBlank(srcGroupId)){
			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("pageSize", 10000);
			params.put("groupId", srcGroupId);
			params.put("custIds", custIds);
			List<TyCustgroupdetailBean> list = tyCustgroupdetailService.queryByCondition(params);
			if(list!=null && list.size()>0){
				for (TyCustgroupdetailBean detail : list) {
					detail.setGroupId(destGroupId);
					tyCustgroupdetailService.update(detail);
				}
			}
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
