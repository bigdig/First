package com.tfzq.web;

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
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyExpertService;
import com.tfzq.service.TyListedcompanyService;
import com.tfzq.service.TyProjectService;
import com.tfzq.service.TyProjectjourService;
import com.tfzq.service.TyProjecttrackService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyExpert;
import com.tfzq.ty.model.generator.TyProject;
import com.tfzq.ty.model.generator.TyProjecttrack;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyProjectBean;
import com.tfzq.ty.model.ty.TyProjectjourBean;
import com.tfzq.ty.model.ty.TyProjecttrackBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目库控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "项目库管理", description = "项目库管理")
@RequestMapping(value = "ty/tyProject", method = RequestMethod.POST)
public class TyProjectController extends BaseController {
	@Autowired
	private TyProjectService tyProjectService;
	@Autowired
	private TyProjecttrackService tyProjecttrackService;
	@Autowired
	private TyProjectjourService tyProjectjourService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private TyExpertService tyExpertService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysRoleService sysRoleService;
//	@Autowired
//	private TyServiceorgService tyServiceorgService;
//	@Autowired
//	private TyListedcompanyService tyListedcompanyService;

	// 查询项目库
	@ApiOperation(value = "查询项目库")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		
		String userId = WebUtil.getCurrentUser();
		TyStafflist tyStafflist = tyStafflistService.queryById(userId);
		List<SysUserRoleBean> myRoles = sysRoleService.queryRoleByUserId(userId);
		if(tyStafflist!=null){
			params.put("deleteFlag", "0");
			params.put("orderBy","id desc");
			params.put("createBy", userId);
			if(!CollectionUtils.isEmpty(myRoles)) {
				for (SysUserRoleBean sysUserRoleBean : myRoles) {
					if(StringUtils.equalsIgnoreCase(sysUserRoleBean.getRoleName(),"项目库管理员")) {
						params.remove("createBy");  //项目库管理员具有所有项目的权限
					}
				}
			}
			
//			if(!StringUtils.equals("所长", tyStafflist.getPosition())&&!StringUtils.equals("副所长", tyStafflist.getPosition())){
//				params.put("createBy", userId);
//			}
		}else {
			params.put("deleteFlag", "0");
			params.put("orderBy","id desc");
			params.put("createBy", userId);
		}
		PageInfo<TyProjectBean> list = tyProjectService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
//		Map<String,List<ExDictItem>> dictsEx = new HashMap<String,List<ExDictItem>>();
		//添加字典翻译
		Map<String,String> projectroleMap = sysDicService.queryDicByDicIndexKey("PROJECTROLE");
		addDictFromMap(dicts,"EXPERT_PROJECTROLE", projectroleMap);
		Map<String,String> projecttypeMap = sysDicService.queryDicByDicIndexKey("PROJECTTYPE");
		addDictFromMap(dicts,"PROJECTTYPE", projecttypeMap);
		List<TyExpert> expertList = tyExpertService.getAllRecords();
		addDictFromModel(dicts, "EXPERT_STAFF", expertList, "id", "expertName");
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
		
		for (TyProjectBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getProjectRole())) {
				singleBean.setProjectRoleText(projectroleMap.get(singleBean.getProjectRole().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getProjectType())) {
				singleBean.setProjectTypeText(projecttypeMap.get(singleBean.getProjectType().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getDeleteFlag())){
				singleBean.setDeleteFlagText(deleteflagMap.get(singleBean.getDeleteFlag()));
			}
			//查看附件是否为空
			if(StringUtils.isNotBlank(singleBean.getAttachment())){
				singleBean.setAttachmentFlag("1");
			}else{
				singleBean.setAttachmentFlag("0");
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}
	// 查询项目库
	@ApiOperation(value = "查询项目库修改流水")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/jourlist")
	public Object getJour(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<TyProjectjourBean> list = tyProjectjourService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
//		Map<String,List<ExDictItem>> dictsEx = new HashMap<String,List<ExDictItem>>();
		//添加字典翻译
		Map<String,String> projectroleMap = sysDicService.queryDicByDicIndexKey("PROJECTROLE");
		addDictFromMap(dicts,"EXPERT_PROJECTROLE", projectroleMap);
		Map<String,String> projecttypeMap = sysDicService.queryDicByDicIndexKey("PROJECTTYPE");
		addDictFromMap(dicts,"PROJECTTYPE", projecttypeMap);
		List<TyExpert> expertList = tyExpertService.getAllRecords();
		addDictFromModel(dicts, "EXPERT_STAFF", expertList, "id", "expertName");
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
		
		for (TyProjectjourBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getProjectRole())) {
				singleBean.setProjectRoleText(projectroleMap.get(singleBean.getProjectRole().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getProjectType())) {
				singleBean.setProjectTypeText(projecttypeMap.get(singleBean.getProjectType().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getDeleteFlag())){
				singleBean.setDeleteFlagText(deleteflagMap.get(singleBean.getDeleteFlag()));
			}
			//查看附件是否为空
			if(StringUtils.isNotBlank(singleBean.getAttachment())){
				singleBean.setAttachmentFlag("1");
			}else{
				singleBean.setAttachmentFlag("0");
			}
			if(StringUtils.isNotBlank(singleBean.getCreateBy())){				
				SysUser user= sysUserService.queryById(singleBean.getCreateBy());
				singleBean.setCreateByName(user==null?singleBean.getCreateBy():user.getUserName());
			}
			if(StringUtils.isNotBlank(singleBean.getUpdateBy())){		
				SysUser user2= sysUserService.queryById(singleBean.getUpdateBy());
				singleBean.setUpdateByName(user2==null?singleBean.getUpdateBy():user2.getUserName());
			}

		}
		return setSuccessModelMap(modelMap, list, dicts);
	}
	// 查询项目库
	@ApiOperation(value = "查询项目库修改流水")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/tracklist")
	public Object getTrack(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<TyProjecttrackBean> list = tyProjecttrackService.queryBeans(params);
		
		//添加字典翻译
		for (TyProjecttrackBean singleBean : list.getList()) {
			if(StringUtils.isNotBlank(singleBean.getCreateBy())){				
				SysUser user= sysUserService.queryById(singleBean.getCreateBy());
				singleBean.setCreateByName(user==null?singleBean.getCreateBy():user.getUserName());
			}
			if(StringUtils.isNotBlank(singleBean.getUpdateBy())){		
				SysUser user2= sysUserService.queryById(singleBean.getUpdateBy());
				singleBean.setUpdateByName(user2==null?singleBean.getUpdateBy():user2.getUserName());
			}
		}

		return setSuccessModelMap(modelMap, list);
	}

	// 详细信息
	@ApiOperation(value = "项目库详情")
	@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyProjectBean tyProjectBean = new TyProjectBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyProject record = tyProjectService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyProjectBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyProjectBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> projectroleMap = sysDicService.queryDicByDicIndexKey("PROJECTROLE");
		addDictFromMap(dicts,"PROJECTROLE", projectroleMap);
		Map<String,String> projecttypeMap = sysDicService.queryDicByDicIndexKey("PROJECTTYPE");
		addDictFromMap(dicts,"PROJECTTYPE", projecttypeMap);
		Map<String, String> deleteflagMap = sysDicService.queryDicByDicIndexKey("DELETEFLAG");
	    if(tyProjectBean!=null){
			if (StringUtils.isNotEmpty(tyProjectBean.getProjectRole())) {
				tyProjectBean.setProjectRoleText(projectroleMap.get(tyProjectBean.getProjectRole().toString()));
			}
			if (StringUtils.isNotEmpty(tyProjectBean.getProjectType())) {
				tyProjectBean.setProjectTypeText(projecttypeMap.get(tyProjectBean.getProjectType().toString()));
			}
			if(StringUtils.isNotEmpty(tyProjectBean.getDeleteFlag())){
				tyProjectBean.setDeleteFlagText(deleteflagMap.get(tyProjectBean.getDeleteFlag()));
			}
	    }
//	    if(StringUtils.isNotBlank(tyProjectBean.getExpertStaffId())){
//	    	List<TyExpert> expertList = new ArrayList<TyExpert>();
//	    	String[] expertIds = tyProjectBean.getExpertStaffId().split(",");
//	    	for(String expertId : expertIds){
//	    		TyExpert record = tyExpertService.queryById(expertId);
//	    		expertList.add(record);
//	    	}
//	    }
	    
		return setSuccessModelMap(modelMap, tyProjectBean,dicts);
	}

	
	// 新增项目库
	@ApiOperation(value = "添加项目库")
	@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyProject record = Request2ModelUtil.covert(TyProject.class, request);
		record.setDeleteFlag("0");
		tyProjectService.add(record);
		return setSuccessModelMap(modelMap);
	}
	// 新增项目库
	@ApiOperation(value = "添加项目库备注")
	@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/addTrack", method = RequestMethod.POST)
	public Object addTrack(HttpServletRequest request, ModelMap modelMap) {
		TyProjecttrack record = Request2ModelUtil.covert(TyProjecttrack.class, request);
		record.setDeleteFlag("0");
		tyProjecttrackService.add(record);
		return setSuccessModelMap(modelMap);
	}
	// 新增项目库
	@ApiOperation(value = "修改项目库备注")
	@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/updateTrack", method = RequestMethod.POST)
	public Object updateTrack(HttpServletRequest request, ModelMap modelMap) {
		TyProjecttrack record = Request2ModelUtil.covert(TyProjecttrack.class, request);
		record.setCreateBy(null);
		record.setCreateTime(null);
		tyProjecttrackService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改项目库
	@ApiOperation(value = "修改项目库")
	@RequiresPermissions("ty.tyListedcompany.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyProject record = Request2ModelUtil.covert(TyProject.class, request);
		/*TyProject oldRecord = tyProjectService.queryById(id);
		oldRecord.setCompanyId(oldRecord.getCompanyId());
		oldRecord.setCompanyName(oldRecord.getCompanyName());*/
		record.setCreateBy(null);
		record.setCreateTime(null);
		tyProjectService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除项目库
	@ApiOperation(value = "删除项目库")
	@RequiresPermissions("ty.tyListedcompany.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyProject record = tyProjectService.queryById(id);
		if(record!=null){
			tyProjectService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}
	// 删除项目库
	@ApiOperation(value = "删除项目库跟踪")
	@RequiresPermissions("ty.tyListedcompany.delete")
	@RequestMapping(value = "/deleteTrack", method = RequestMethod.POST)
	public Object deleteTrack(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyProjecttrack record = tyProjecttrackService.queryById(id);
		if(record!=null){
			tyProjecttrackService.delete(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
