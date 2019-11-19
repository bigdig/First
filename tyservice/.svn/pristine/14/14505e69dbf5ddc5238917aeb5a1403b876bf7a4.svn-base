package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.generator.SysUserRole;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysPositionService;
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
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyStafflistBean;

/**
 * 员工控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "员工管理", description = "员工管理")
@RequestMapping(value = "ty/tyStafflist", method = RequestMethod.POST)
public class TyStafflistController extends BaseController {
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysPositionService sysPositionService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
    private SysDicService sysDicService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;

	// 查询员工
	@ApiOperation(value = "查询员工")
	//@RequiresPermissions("ty.tyStafflist.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "deleteFlag", required = false) String deleteFlag) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		//params.put("deleteFlag", "0");
		if(StringUtils.isNoneBlank(deleteFlag)) {
			params.put("deleteFlag", deleteFlag);
		}
		params.put("orderBy","id desc");
		PageInfo<TyStafflistBean> list = tyStafflistService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
		for (TyStafflistBean singleBean : list.getList()) {
			if(StringUtils.isNotEmpty(singleBean.getDeptId())){
				SysDept temp = sysDeptService.queryById(singleBean.getDeptId());
				singleBean.setDeptName(temp==null?"":temp.getDeptName());
			}
			if(StringUtils.isNotEmpty(singleBean.getPositionId())){
				SysPosition temp = sysPositionService.queryById(singleBean.getPositionId());
				singleBean.setPosition(temp==null?"":temp.getPositionName());
			}
			if(StringUtils.isNotEmpty(singleBean.getDeleteFlag())){
				singleBean.setDeleteFlagText(deleteflagMap.get(singleBean.getDeleteFlag()));
			}
			if(StringUtils.isNotEmpty(singleBean.getWorkAreaid())){
				SysArea temp = sysAreaService.queryById(singleBean.getWorkAreaid());
				singleBean.setWorkAreaname(temp==null?"":temp.getAreaName());
			}
		}
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "员工详情")
	//@RequiresPermissions("ty.tyStafflist.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyStafflistBean tyStafflistBean = new TyStafflistBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyStafflist record = tyStafflistService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyStafflistBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyStafflistBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		List<SysDept> sysDepts = sysDeptService.getAllRecords();
		addDictFromModel(dicts,"SysDepts", sysDepts,"id","deptName");
		List<SysPosition> sysPositions = sysPositionService.getAllRecords();
		addDictFromModel(dicts,"SysPositions", sysPositions,"id","positionName");
		/*List<SysArea> sysAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysAreas", sysAreas,"id","areaName");*/
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
	    if(tyStafflistBean!=null){
			if(StringUtils.isNotEmpty(tyStafflistBean.getDeptId())){
				SysDept temp = sysDeptService.queryById(tyStafflistBean.getDeptId());
				tyStafflistBean.setDeptName(temp==null?"":temp.getDeptName());
			}
			if(StringUtils.isNotEmpty(tyStafflistBean.getPositionId())){
				SysPosition temp = sysPositionService.queryById(tyStafflistBean.getPositionId());
				tyStafflistBean.setPosition(temp==null?"":temp.getPositionName());
			}
			/*if(StringUtils.isNotEmpty(tyStafflistBean.getAreaId())){
				SysArea temp = sysAreaService.queryById(tyStafflistBean.getAreaId());
				tyStafflistBean.setArea(temp==null?"":temp.getAreaName());
			}*/
			if(StringUtils.isNotEmpty(tyStafflistBean.getWorkAreaid())){
				SysArea temp = sysAreaService.queryById(tyStafflistBean.getWorkAreaid());
				tyStafflistBean.setWorkAreaname(temp==null?"":temp.getAreaName());
			}
			if(StringUtils.isNotEmpty(tyStafflistBean.getDeleteFlag())){
				tyStafflistBean.setDeleteFlagText(deleteflagMap.get(tyStafflistBean.getDeleteFlag()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, tyStafflistBean,dicts);
	}

	
	// 新增员工
	@ApiOperation(value = "添加员工")
	//@RequiresPermissions("ty.tyStafflist.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyStafflist record = Request2ModelUtil.covert(TyStafflist.class, request);
		record.setDeleteFlag("0");
		TyStafflist newRecord = tyStafflistService.add(record);
		
		//员工数据同步到sys_user表，做登录使用
		SysUser sysUser = new SysUser();
		//默认密码111111,并做加密处理
		sysUser.setId(newRecord.getId());
		sysUser.setAccount(record.getUserId());
		sysUser.setUserName(record.getStaffName());
		sysUser.setPassword(" ");
		sysUser.setSex(0);
		sysUser.setLocked(false);
		sysUser.setUserType(1);
		sysUser.setCompanyId("1001");
		sysUser.setDeptId(record.getDeptId());
		sysUser.setPhone(record.getTel());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", sysUser.getAccount());
		PageInfo<SysUser> userPage = sysUserService.query(params);
		//不存在才添加到sysuser表
		if(null!=userPage && CollectionUtils.isNotEmpty(userPage.getList())){
			//throw new BusinessException("用户名已存在!");
		}else{
			SysUser su = sysUserService.add(sysUser);
			//添加user_role
			//1 根据职位查询role_id
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("positionId", record.getPositionId());
			PageInfo<SysPositionroleBean> spr = sysPositionroleService.queryBeans(params2);
			if(null!=spr && CollectionUtils.isNotEmpty(spr.getList())){
				SysPositionroleBean sprb = spr.getList().get(0);
				String roleId = sprb.getRoleId();
				SysUserRole userRole= new SysUserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(su.getId());
				sysRoleService.insertUserRole(userRole);
			}
		}
		
		return setSuccessModelMap(modelMap);
	}

	// 修改员工
	@ApiOperation(value = "修改员工")
	//@RequiresPermissions("ty.tyStafflist.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyStafflist record = Request2ModelUtil.covert(TyStafflist.class, request);
		tyStafflistService.update(record);
		//如果修改的是销售，则需要对客户表进行同步更新
		tyOrgcustomerService.updateServiceSaler(record.getId());
		return setSuccessModelMap(modelMap);
	}

	// 删除员工
	@ApiOperation(value = "删除员工")
	//@RequiresPermissions("ty.tyStafflist.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyStafflist record = tyStafflistService.queryById(id);
		if(record!=null){
			tyStafflistService.delete(id);
			SysUser sysUser = sysUserService.queryById(id);
			if(sysUser!=null){
				sysUser.setLocked(true);
				sysUser.setEnable(false);
				sysUserService.update(sysUser);
			}
		}
		return setSuccessModelMap(modelMap);
	}
}
