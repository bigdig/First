package org.ibase4j.web.sys;

import java.lang.reflect.InvocationTargetException;
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
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.core.utils.UserUtils;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysDeptBean;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 部门管理控制类
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "部门管理", description = "部门管理")
@RequestMapping(value = "sys/dept", method = RequestMethod.POST)
public class SysDeptController extends BaseController {
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysDicService sysDicService;

	// 查询部门
	@ApiOperation(value = "查询租户")
	@RequiresPermissions("sys.dept.read")
	@RequestMapping(value = "/read/list")
	public Object getCompany(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		if(params.get("parentId")==null || StringUtils.isBlank(params.get("parentId").toString())){
			params.put("parentId", "0");
		}
		PageInfo<?> list = sysDeptService.query(params);
		return setSuccessModelMap(modelMap, list);
	}
	// 查询部门
	@ApiOperation(value = "查询部门")
	@RequiresPermissions("sys.dept.read")
	@RequestMapping(value = "/read/deptlist")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("orderBy","id asc");
		if(params.get("parentId")==null || StringUtils.isBlank(params.get("parentId").toString())){
			params.put("parentId", "0");
		}
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		if(!UserUtils.isAdmin(user)){
			params.put("rootNode", user.getCompanyId());
		}
		PageInfo<SysDeptBean> list = sysDeptService.queryBeans(params);
		setSysBeanChildren(list);
		
		return setSuccessModelMap(modelMap, list);
	}
	
	private PageInfo<SysDeptBean> setSysBeanChildren(PageInfo<SysDeptBean> list){
		for (Iterator iterator = list.getList().iterator(); iterator.hasNext();) {
			SysDeptBean sysBean = (SysDeptBean) iterator.next();
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("parentId", sysBean.getId());
			params2.put("pageSize", "10000");
			params2.put("orderBy","id asc");
			PageInfo<SysDeptBean> list2 = sysDeptService.queryBeans(params2);
			if(list2!=null && list2.getList()!=null && list2.getList().size()>0){
				sysBean.setChildren(list2.getList());
				setSysBeanChildren(list2);
			}
		}
		return null;
	}
	
	// 查询所有部门
	@ApiOperation(value = "查询所有部门")
	//@RequiresPermissions("sys.dept.read")
	@RequestMapping(value = "/read/allDept")
	public Object allDept(HttpServletRequest request, ModelMap modelMap) {
		List<SysDept> sysDepts = sysDeptService.getAllRecords();
		return setSuccessModelMap(modelMap, sysDepts);
	}

	// 详细信息
	@ApiOperation(value = "部门详情")
	@RequiresPermissions("sys.dept.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysDeptBean SysDeptBean = new SysDeptBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotBlank(id)){
			SysDept record = sysDeptService.queryById(id);
			try {
	            BeanUtils.copyProperties(SysDeptBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
	        	SysDeptBean = null;
	        	e.printStackTrace();
	        }
		}
		Map<String,String> contentstatusMap = sysDicService.queryDicByDicIndexKey("CHANNELTYPE");
		addDictFromMap(dicts,"CHANNELTYPE", contentstatusMap);
	    if(SysDeptBean!=null){
			if (null!=SysDeptBean.getChannelType()) {
				SysDeptBean.setChannelTypeText(contentstatusMap.get(SysDeptBean.getChannelType().toString()));
			}
	    }
		return setSuccessModelMap(modelMap, SysDeptBean,dicts);
	}

	// 新增部门
	@ApiOperation(value = "添加部门")
	@RequiresPermissions("sys.dept.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysDept record = Request2ModelUtil.covert(SysDept.class, request);
		if(StringUtils.isEmpty(record.getParentId())){
			record.setParentId("0");
		}
		if(record.getParentId().equals("0")){
			record.setId(record.getDeptNo());
		}else{
			record.setId(record.getParentId() + record.getDeptNo());			
		}
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("id", record.getId());
		PageInfo<SysDept> list = sysDeptService.query(params);
		if(list.getList().size() > 0){
			throw new IllegalParameterException("该部门编码已经存在！");
		}
		record.setLeaf(1);
		sysDeptService.add(record);
		//更新父结点标记
		SysDept parent = sysDeptService.queryById(record.getParentId());
		if(parent!=null && parent.getLeaf()==1){
			parent.setLeaf(0);
			sysDeptService.update(parent);
		}
		return setSuccessModelMap(modelMap);
	}

	// 修改部门
	@ApiOperation(value = "修改部门")
	@RequiresPermissions("sys.dept.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysDept record = Request2ModelUtil.covert(SysDept.class, request);
		record.setEnable(true);
		sysDeptService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除部门
	@ApiOperation(value = "删除部门")
	@RequiresPermissions("sys.dept.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		sysDeptService.delete(id);
		return setSuccessModelMap(modelMap);
	}
	
	// 切换是否启用
	@ApiOperation(value = "切换是否启用")
	@RequiresPermissions("sys.dept.delete")
	@RequestMapping(value = "/deleteOrNot", method = RequestMethod.POST)
	public Object deleteOrNot(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id,String enable) {
		SysDept record = sysDeptService.queryById(id);
		if(null!=record){
			record.setEnable(enable.equals("1")?true:false);
			record.setUpdateTime(new Date());
			sysDeptService.update(record);
		}
		return setSuccessModelMap(modelMap);
	}
}
