package org.ibase4j.web.sys;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysMenu;
import org.ibase4j.model.generator.SysRole;
import org.ibase4j.model.generator.SysRoleMenu;
import org.ibase4j.model.sys.SysRoleBean;
import org.ibase4j.model.sys.SysRoleMenuBean;
import org.ibase4j.service.sys.SysCacheService;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysMenuService;
import org.ibase4j.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;

/**
 * 角色管理
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:15:43
 */
@RestController
@Api(value = "角色管理", description = "角色管理")
@RequestMapping(value = "sys/role", method = RequestMethod.POST)
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysCacheService sysCacheService;
	@Autowired
	private SysDeptService sysDeptService;

    @ApiOperation(value = "查询角色")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/read/list")
    public Object get(HttpServletRequest request, ModelMap modelMap) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        PageInfo<SysRoleBean> list = sysRoleService.queryBean(params);
        for(SysRoleBean sysRoleBean:list.getList()){
        	if(StringUtils.isNotEmpty(sysRoleBean.getDeptId())){
				SysDept temp = sysDeptService.queryById(sysRoleBean.getDeptId());
				sysRoleBean.setDeptName(temp==null?"":temp.getDeptName());
			}
        }
        return setSuccessModelMap(modelMap, list);
    }

    // 详细信息
    @ApiOperation(value = "角色详情")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/read/detail")
    public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
    	SysRoleBean sysRoleBean = new SysRoleBean();
    	Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
    	if(StringUtils.isNotEmpty(id)){
        	SysRole record = sysRoleService.queryById(id);
        	try {
	            BeanUtils.copyProperties(sysRoleBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
	        	sysRoleBean = null;
	        	e.printStackTrace();
	        }
    	}
    	//添加字典翻译
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", "0");
		params.put("pageSize","1000");
		PageInfo<SysDept> deptList = sysDeptService.query(params);
		SysDept dept = sysDeptService.queryById("0");
		if(dept!=null){
			deptList.getList().add(dept);
		}
		List<SysDept> sysDepts = deptList.getList();
		addDictFromModel(dicts,"SysDepts", sysDepts,"id","deptName");
		if(sysRoleBean!=null){
			if(StringUtils.isNotEmpty(sysRoleBean.getDeptId())){
				SysDept temp = sysDeptService.queryById(sysRoleBean.getDeptId());
				sysRoleBean.setDeptName(temp==null?"":temp.getDeptName());
			}
	    }
        return setSuccessModelMap(modelMap, sysRoleBean,dicts);
    }

    // 新增
    @ApiOperation(value = "添加角色")
    @RequiresPermissions("sys.role.add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request, ModelMap modelMap) {
        SysRole record = Request2ModelUtil.covert(SysRole.class, request);
        if(StringUtils.isBlank(record.getDeptId())){
        	record.setDeptId("0");
        }
        record.setRoleType(0);
        record.setId(record.getRoleCode());
        SysRole sysRole = sysRoleService.queryById(record.getRoleCode());
        if (sysRole == null) {
        	sysRoleService.add(record);
        } else {
        	throw new IllegalParameterException("角色编码已存在");
        }
        
        return setSuccessModelMap(modelMap);
    }

    // 修改
    @ApiOperation(value = "修改角色")
    @RequiresPermissions("sys.role.update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object update(HttpServletRequest request, ModelMap modelMap) {
        SysRole record = Request2ModelUtil.covert(SysRole.class, request);
        if(StringUtils.isBlank(record.getDeptId())){
        	record.setDeptId("0");
        }
        if(record.getId().equals(record.getRoleCode())){
        	sysRoleService.update(record);
        }else{
			throw new IllegalParameterException("不允许修改角色编码！");
        }
        return setSuccessModelMap(modelMap);
    }

    // 删除
    @ApiOperation(value = "删除角色")
    @RequiresPermissions("sys.role.delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(HttpServletRequest request, ModelMap modelMap,
        @RequestParam(value = "id", required = false) String id) {
        sysRoleService.delete(id);
        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "获取角色权限")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/read/permission", method = RequestMethod.POST)
    public Object getPermission(HttpServletRequest request, ModelMap modelMap,
        @RequestParam(value = "id", required = false) String id) {
        List<String> permissions = sysRoleService.getPermissions(id);
        return setSuccessModelMap(modelMap, permissions);
    }
    @ApiOperation(value = "获取角色菜单")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/read/menu", method = RequestMethod.POST)
    public Object getMenu(HttpServletRequest request, ModelMap modelMap,
    		@RequestParam(value = "id", required = false) String id) {
        SysRole record = sysRoleService.queryById(id);
        //所有菜单
    	List<SysMenu> menus = sysMenuService.getAllListSorted();
    	
    	//排序
    	menus = this.sortSysMenu(menus);
    	
        //已有菜单权限
        List<SysRoleMenuBean> userrole=sysRoleService.queryMenuByRoleId(id);
        List<SysRoleMenuBean> result = new ArrayList<SysRoleMenuBean>();
        for(SysMenu menu: menus){
        	SysRoleMenuBean bean = new SysRoleMenuBean();
        	bean.setMenuId(menu.getId());
        	bean.setMenuName(menu.getMenuName());
        	bean.setIsSelected("0");
        	for(SysRoleMenuBean ur: userrole){
            	if(menu.getId().equals(ur.getMenuId())){
            		bean.setIsSelected("1");
            		break;
            	}
            }
        	result.add(bean);
        }
        
    	return setSuccessModelMap(modelMap,record, result);
    }
    private List<SysMenu> sortSysMenu(List<SysMenu> menus) {
    	List<SysMenu> parentMenus = new ArrayList<SysMenu>();
    	
    	List<SysMenu> resultList = new ArrayList<SysMenu>();
    	
    	//1、拿到主菜单的列表 parentMenus
    	for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();) {
			SysMenu sysMenu = (SysMenu) iterator.next();
			if(sysMenu.getParentId().equals("0")){
				parentMenus.add(sysMenu);
			}
		}
    	
    	for (Iterator<SysMenu> iterator = parentMenus.iterator(); iterator.hasNext();) {
			SysMenu sysMenu = (SysMenu) iterator.next();
			//a 先加入父级元素
			resultList.add(sysMenu);
			//b 循环menus,找出parent_id为此时父级元素id的元素
			List<SysMenu> tempList = new ArrayList<SysMenu>();
			for (Iterator<SysMenu> iterator2 = menus.iterator(); iterator2.hasNext();) {
				SysMenu sysMenuTemp = (SysMenu) iterator2.next();
				if(sysMenuTemp.getParentId().equals(sysMenu.getId())){
					tempList.add(sysMenuTemp);
				}
			}
			if(CollectionUtils.isNotEmpty(tempList)){
				resultList.addAll(tempList);
			}
		}
    	
		return resultList;
	}

	@ApiOperation(value = "更新角色菜单")
    @RequiresPermissions("sys.role.read")
    @RequestMapping(value = "/menu/update", method = RequestMethod.POST)
    public Object updateMenu(HttpServletRequest request, ModelMap modelMap,
    		@RequestParam(value = "roleId", required = true) String roleId
			, @RequestParam(value = "menu[]", required = false) List<String> menus) {
    	sysRoleService.deleteMenuByRoleId(roleId);
    	if(null!=menus && menus.size()>0){
    		for(String menu:menus){
    			SysRoleMenu roleMenu= new SysRoleMenu();
    			roleMenu.setRoleId(roleId);
    			roleMenu.setMenuId(menu);
    			roleMenu.setPermission("*");
    			sysRoleService.insertRoleMenu(roleMenu);
    		}
    	}
		
		//清除所有权限相关缓存
		sysCacheService.flushAuthCaches();
    	return setSuccessModelMap(modelMap);
    }
	
}
