/**
 * 
 */
package org.ibase4j.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.SecurityUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.core.utils.UserUtils;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.generator.SysUserRole;
import org.ibase4j.model.sys.SysMenuBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysAuthorizeService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysPositionService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.ty.TyStafflistBean;

/**
 * 用户管理控制器
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:12:12
 */
@RestController
@Api(value = "用户管理", description = "用户管理")
@RequestMapping(value = "/sys/user", method = RequestMethod.POST)
public class SysUserController extends BaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysAuthorizeService authorizeService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysPositionService sysPositionService;
	
	// 新增用户
	@ApiOperation(value = "添加用户")
	@RequiresPermissions("sys.user.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		
		SysUser record = Request2ModelUtil.covert(SysUser.class, request);
		//默认密码111111,并做加密处理
		record.setPassword(sysUserService.encryptPassword(StringUtils
				.isEmpty(record.getPassword()) ? "111111" : record
				.getPassword()));
		record.setSex(0);
		record.setLocked(false);
		record.setUserType(1);
		record.setCompanyId(getCompanyId(record.getDeptId()));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", record.getAccount());
		PageInfo<SysUser> userPage = sysUserService.query(params);
		if(null!=userPage && CollectionUtils.isNotEmpty(userPage.getList())){
			throw new BusinessException("用户名已存在!");
		}
		
		sysUserService.add(record);
		return setSuccessModelMap(modelMap);
	}

	private String getCompanyId(String deptId){
		return sysDeptService.getComapnyIdByDeptId(deptId);
//		if("0".equals(deptId)){
//			return "0";
//		}
//		SysDept dept = sysDeptService.queryById(deptId);
//		while(!dept.getParentId().equals("0")){
//			dept = sysDeptService.queryById(dept.getParentId());
//		}
//		return dept.getId();
	}
	
	// 添加角色
	@ApiOperation(value = "添加角色")
	@RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/role/add")
	public Object insertUserRole( ModelMap modelMap, @RequestParam(value = "id", required = true) String id
			, @RequestParam(value = "rloe[]", required = false) List<String> roles) {

		sysRoleService.deleteRoleByUserId(id);
		for(String roleId:roles){
			SysUserRole userRole= new SysUserRole();
			userRole.setRoleId(roleId);
			userRole.setUserId(id);
			sysRoleService.insertUserRole(userRole);
		}
		//清除该用户的相关权限
		sysRoleService.clearUserRoleCache(id);
		return setSuccessModelMap(modelMap);
	}

		
	// 修改用户信息
	@ApiOperation(value = "修改用户信息")
	@RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/update")
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysUser sysUser = Request2ModelUtil.covert(SysUser.class, request);
		//修改时如果不传密码，则以原密码为准
		SysUser oldRecord = sysUserService.queryById(sysUser.getId());
		if(StringUtils.isEmpty(sysUser.getPassword())){
			sysUser.setPassword(oldRecord.getPassword());
		}else{
			sysUser.setPassword(sysUserService.encryptPassword(sysUser
							.getPassword()));
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", sysUser.getAccount());
		PageInfo<SysUser> userPage = sysUserService.query(params);
		if(null!=userPage && CollectionUtils.isNotEmpty(userPage.getList())){
			if(!userPage.getList().get(0).getId().equals(sysUser.getId())){
				throw new BusinessException("用户名已存在!");
			}
		}
		sysUserService.updateUserInfo(sysUser);
		
		return setSuccessModelMap(modelMap);
	}
	//冻结解冻用户
	@ApiOperation(value = "冻结解冻用户")
	@RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/lock")
	public Object updateLock(ModelMap modelMap, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "lock", required = false) String lock
			) {
		SysUser record = sysUserService.queryById(id);
		if(record!=null&&!record.getAccount().equals("admin")){
			if(lock.equals("1")){
				record.setLocked(true);
			}else{
				record.setLocked(false);
			}
			sysUserService.update(record);
		}
		return setSuccessModelMap(modelMap);
	}

	// 修改密码
	@ApiOperation(value = "修改密码")
	//@RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/update/password")
	public Object updatePassword(ModelMap modelMap, @RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "oldPassword", required = false) String oldPassword,
			@RequestParam(value = "password", required = false) String password) {
		//if(StringUtils.isEmpty(id)){
		id = WebUtil.getCurrentUser();
		//}
        SysUser user = sysUserService.queryById(id);
        String oldpwd = sysUserService.encryptPassword(oldPassword);
        if(!oldpwd.equals(user.getPassword())){
			throw new IllegalParameterException("您的原密码不正确！");
        }
		sysUserService.updatePassword(id, password);
		return setSuccessModelMap(modelMap);
	}

	// 查询用户
	@ApiOperation(value = "查询用户")
	@RequiresPermissions("sys.user.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		if(!UserUtils.isAdmin(user)){
			params.put("rootNode", user.getCompanyId());
		}

		PageInfo<?> list = sysUserService.queryBeans(params);
		return setSuccessModelMap(modelMap, list);
	}
	
	// 用户详细信息
	@ApiOperation(value = "用户详细信息")
	@RequiresPermissions("sys.user.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysUser sysUser =null;
		if(StringUtils.isNotEmpty(id)){
			sysUser = sysUserService.queryById(id);
			if (sysUser != null) {
				sysUser.setPassword(null);
			}
			
		}
		
		Map<String,Map<String,String>> dicts =new HashMap<String,Map<String,String>>();
		Map<String,String> depts = sysDeptService.getSysDeptMap();
		addDict(dicts,"DEPTTYPE", depts);
		return setSuccessModelMap(modelMap, sysUser,dicts);
	}

	// 当前用户
	@ApiOperation(value = "当前用户信息")
	@RequestMapping(value = "/read/current")
	public Object current(ModelMap modelMap) {
	    String id = getCurrUser();
		SysUser sysUser = sysUserService.queryById(id);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		SysDept dept = new SysDept();
		if(StringUtils.isNotBlank(sysUser.getCompanyId())){
			dept = sysDeptService.queryById(sysUser.getCompanyId());
		}
		List<SysMenuBean> menus = authorizeService.queryAuthorizeByUserId(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", sysUser.getAccount());
		params.put("orderBy","id desc");
		PageInfo<TyStafflistBean> list = tyStafflistService.queryBeans(params);
		if(list!=null && list.getList().size()>0){
			TyStafflistBean singleBean = list.getList().get(0);
			SysPosition temp = sysPositionService.queryById(singleBean.getPositionId());
			singleBean.setPosition(temp==null?"":temp.getPositionName());
			modelMap.put("tyStaff", singleBean);
		}
		
		//已有角色
        List<SysUserRoleBean> userroles=sysRoleService.queryRoleByUserId(id);
		modelMap.put("user", sysUser);
		modelMap.put("dept", dept);
		modelMap.put("menus", menus);
		modelMap.put("userrole", userroles);
		return setSuccessModelMap(modelMap);
	}
	
	//查询角色
	@ApiOperation(value = "查询角色")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/read/role")
    public Object getrole( ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysUser sysUser = sysUserService.queryById(id);
        //所有角色
        List<SysRole> roles = sysRoleService.getAllList();
        //已有角色
        List<SysUserRoleBean> userrole=sysRoleService.queryRoleByUserId(id);
        
        List<SysUserRoleBean> result = new ArrayList<SysUserRoleBean>();
        for(SysRole role: roles){
        	SysUserRoleBean bean = new SysUserRoleBean();
        	bean.setRoleId(role.getId());
        	bean.setRoleName(role.getRoleName());
        	bean.setIsSelected("0");
        	for(SysUserRoleBean ur: userrole){
            	if(role.getId().equals(ur.getRoleId())){
            		bean.setIsSelected("1");
            		break;
            	}
            }
        	result.add(bean);
        }
        return setSuccessModelMap(modelMap,sysUser,result);
    }


	// 删除机构用户
	@ApiOperation(value = "获取操作员服务台地址")
	//@RequiresPermissions("pb.user.read")
	@RequestMapping(value = "/getServicePath", method = RequestMethod.POST)
	public Object getServicePath(HttpServletRequest request, ModelMap modelMap) {
		String userId = WebUtil.getCurrentUser();
		SysUser sysUser = sysUserService.queryById(userId);

		String url = PropertiesUtil.getString("serviceplatform.url.back");
		String privateKey = PropertiesUtil.getString("serviceplatform.url.privateKey");
		String account = sysUser.getAccount();
		String str=account+privateKey;
		//str = str.substring(0, str.length()-1);
		String signinKey = SecurityUtil.encryptMd5To32Str(str,"UTF-8");
		StringBuffer sb = new StringBuffer();
		sb.append(url)
		.append("&loginName=").append(account)
		.append("&signinKey=").append(signinKey);
		return setSuccessModelMap(modelMap,sb.toString());
	}

	
	//查询角色
	@ApiOperation(value = "查询角色")
    @RequiresPermissions("sys.user.read")
    @RequestMapping(value = "/read/teamrole")
    public Object getTeamrole( ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysUser sysUser = sysUserService.queryById(id);
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		
        //所有团队共享可分配角色
		Map<String,Object> params = new HashMap<String,Object>();
/*		params.put("role_type", "2");
		params.put("pageSize", "1000");
        List<SysRole> roles = sysRoleService.query(params).getList();*/
        //定制化的角色
//        params.clear();
		if (!UserUtils.isAdmin(currUser)) {
			params.put("dept_id", currUser.getCompanyId());
		}
        params.put("pageSize", "1000");
        List<SysRole> roles = sysRoleService.query(params).getList();
//        roles.addAll(roles2);
        //已有角色
        List<SysUserRoleBean> userrole=sysRoleService.queryRoleByUserId(id);
        
        List<SysUserRoleBean> result = new ArrayList<SysUserRoleBean>();
        for(SysRole role: roles){
        	SysUserRoleBean bean = new SysUserRoleBean();
        	bean.setRoleId(role.getId());
        	bean.setRoleName(role.getRoleName());
        	bean.setIsSelected("0");
        	for(SysUserRoleBean ur: userrole){
            	if(role.getId().equals(ur.getRoleId())){
            		bean.setIsSelected("1");
            		break;
            	}
            }
        	result.add(bean);
        }
        return setSuccessModelMap(modelMap,sysUser,result);
    }

}
