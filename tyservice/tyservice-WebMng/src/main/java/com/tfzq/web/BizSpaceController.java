package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ExDictItem;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysAreaService;
import org.ibase4j.service.sys.SysDicService;
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
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyServiceorgService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;
import com.tfzq.util.DESPlus;

/**
 * 客户控制类
 * 
 * @author shenli
 */
@Deprecated
@RestController
@Api(value = "公共查询管理", description = "客户管理")
@RequestMapping(value = "bizspace", method = RequestMethod.POST)
public class BizSpaceController extends BaseController {
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
    private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;

	// 查询机构
	@ApiOperation(value = "查询机构")
	@RequestMapping(value = "/read/orglist")
	public Object orglist(HttpServletRequest request, ModelMap modelMap) throws Exception{
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		
		String currentUserId=checkInterfaceRight(request);
		
		//TODO
		SysUser currUser = sysUserService.queryById(currentUserId);
		//已有角色
        List<SysUserRoleBean> userrole=sysRoleService.queryRoleByUserId(currUser.getId());
        if(isSeniorseller(userrole)){//如果是高级销售
        	// 先在staffList表里加入所属团队字段
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
        
		PageInfo<TyServiceorgBean> list = tyServiceorgService.queryBeans(params);
		List<TyServiceorgBean> orgList = tyServiceorgService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
		
		if(orgList!=null && orgList.size()>0){
			addDictFromModel(dicts,"Serviceorgs", orgList,"id","orgName");
		}
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
		
		//添加字典翻译
		if(list.getList().size()>0){
			for (TyServiceorgBean singleBean : list.getList()) {
				if (StringUtils.isNotEmpty(singleBean.getOrgLevel())) {
					singleBean.setOrgLevelName(orglevelMap.get(singleBean.getOrgLevel().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustStatus())) {
					singleBean.setCustStatusName(custStatusMap.get(singleBean.getCustStatus().toString()));
				}
				if (StringUtils.isNotEmpty(singleBean.getCustCat())) {
					singleBean.setCustCatName(custCatMap.get(singleBean.getCustCat().toString()));
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "机构详情")
	@RequestMapping(value = "/read/orgdetail")
	public Object orgdetail(HttpServletRequest request,ModelMap modelMap, @RequestParam(value = "id", required = false) String id) throws Exception{
		checkInterfaceRight(request);
		
		TyServiceorgBean tyServiceorgBean = new TyServiceorgBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyServiceorg record = tyServiceorgService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyServiceorgBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyServiceorgBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		if(null!=tyStafflists && tyStafflists.size()>0){
			for (TyStafflist tyStaff : tyStafflists) {
				if(sysWorkAreas.size()>0 && tyStaff!=null){
					for (SysArea sysArea : sysWorkAreas) {
						if(tyStaff.getWorkAreaid().equals(sysArea.getId())){
							String staffName = tyStaff.getStaffName() + "("+sysArea.getAreaName()+ ")";
							tyStaff.setStaffName(staffName);
							break;
						}
					}
				}
			}
		}
		
		addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
//		List<SysArea> sysWorkAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysWorkAreas", sysWorkAreas,"id","areaName");
		Map<String, String> orglevelMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
		addDictFromMap(dicts, "OrgLevels", orglevelMap);
		Map<String, String> custStatusMap = sysDicService.queryDicByDicIndexKey("CUSTSTATUS");
		addDictFromMap(dicts, "CustStatuss", custStatusMap);
		Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
		addDictFromMap(dicts, "CustCats", custCatMap);
	    if(tyServiceorgBean!=null){
			if (StringUtils.isNotEmpty(tyServiceorgBean.getOrgLevel())) {
				tyServiceorgBean.setOrgLevelName(orglevelMap.get(tyServiceorgBean.getOrgLevel().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustStatus())) {
				tyServiceorgBean.setCustStatusName(custStatusMap.get(tyServiceorgBean.getCustStatus().toString()));
			}
			if (StringUtils.isNotEmpty(tyServiceorgBean.getCustCat())) {
				tyServiceorgBean.setCustCatName(custCatMap.get(tyServiceorgBean.getCustCat().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, tyServiceorgBean,dicts);
	}
	
	// 查询机构客户
	@ApiOperation(value = "查询机构客户")
	@RequestMapping(value = "/read/custlist")
	public Object custlist(HttpServletRequest request, ModelMap modelMap) throws Exception{
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		
		String currentUserId=checkInterfaceRight(request);
		
		SysUser currUser = sysUserService.queryById(currentUserId);
		
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
		
		PageInfo<TyOrgcustomerBean> list = tyOrgcustomerService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		List<SysArea> sysAreas = sysAreaService.getAllRecords();
		addDictFromModel(dicts,"SysAreas", sysAreas,"id","areaName");
		
		List<TyServiceorg> serviceOrgList = tyServiceorgService.getAllRecords();
		addDictFromModel(dicts,"Serviceorgs", serviceOrgList,"id","orgName");
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords();
		addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
		
		//添加字典翻译
		for (TyOrgcustomerBean singleBean : list.getList()) {
			if(StringUtils.isNotEmpty(singleBean.getSalerId())){
				TyStafflist temp = tyStafflistService.queryById(singleBean.getSalerId());
				singleBean.setServiceSaler(temp==null?"":temp.getStaffName());
			}
			if(StringUtils.isNotEmpty(singleBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(singleBean.getOrgId());
				singleBean.setOrgName(temp==null?"":temp.getOrgName());
			}
			
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "机构客户详情")
	@RequestMapping(value = "/read/custdetail")
	public Object custdetail(HttpServletRequest request,ModelMap modelMap, @RequestParam(value = "id", required = false) String id) throws Exception{
		checkInterfaceRight(request);
		
		TyOrgcustomerBean tyOrgcustomerBean = new TyOrgcustomerBean();
		Map<String,List<ExDictItem>> dicts = new HashMap<String,List<ExDictItem>>();
		if(StringUtils.isNotEmpty(id)){
			TyOrgcustomer record = tyOrgcustomerService.queryById(id);
			
			//更新remark冗余字段
			Map<String, Object> pm = new HashMap<String, Object>();
			pm.put("customerId", record.getId());
			pm.put("pageSize", "1000");
			pm.put("orderBy","id desc");
			PageInfo<TyCustomerlabelBean> labelBeans = tyCustomerlabelService.queryBeans(pm);
			StringBuffer sb = new StringBuffer();
			if(labelBeans.getList().size()>0){
				for (TyCustomerlabelBean labelBean : labelBeans.getList()) {
					if(labelBean.getLabelId()!=null){
						TyLabel tl = tyLabelService.queryById(labelBean.getLabelId());
						sb.append(tl.getLabelName() + Constants.LABEL_SEPERATOR);
					}
				}
				String mark = sb.toString();
				record.setMark(mark.substring(0,mark.length()-1));
				tyOrgcustomerService.update(record);
			}
			
			try {
	            BeanUtils.copyProperties(tyOrgcustomerBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyOrgcustomerBean = null;
	        	e.printStackTrace();
	        }
	    }
		
		
		//添加字典翻译
		List<TyStafflist> tyStafflists = tyStafflistService.getSalerRecords(); //*** 找销售人员
		List<ExDictItem> staffitems = new ArrayList<ExDictItem>();
		for (Iterator iterator = tyStafflists.iterator(); iterator.hasNext();) {
			TyStafflist tyStafflist = (TyStafflist) iterator.next();
			ExDictItem item = new ExDictItem();
			item.setId(tyStafflist.getId());
			item.setText(tyStafflist.getStaffName());
			
			staffitems.add(item);
		}
		dicts.put("TyStafflists", staffitems);
//			addDictFromModel(dicts,"TyStafflists", tyStafflists,"id","staffName");
		
		
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
		dicts.put("TyServiceorgs", orgitems);
		
		
		List<TyLabelBean> catLabelList = new ArrayList<TyLabelBean>();
	    if(tyOrgcustomerBean!=null){
			if(StringUtils.isNotEmpty(tyOrgcustomerBean.getSalerId())){
				TyStafflist temp = tyStafflistService.queryById(tyOrgcustomerBean.getSalerId());
				tyOrgcustomerBean.setServiceSaler(temp==null?"":temp.getStaffName());
			}
			if(StringUtils.isNotEmpty(tyOrgcustomerBean.getOrgId())){
				TyServiceorg temp = tyServiceorgService.queryById(tyOrgcustomerBean.getOrgId());
				tyOrgcustomerBean.setOrgName(temp==null?"":temp.getOrgName());
			}
			if(null!=tyOrgcustomerBean.getId()){
				catLabelList = tyLabelService.getCatListByCustomerId(tyOrgcustomerBean.getId());
				
				if(catLabelList!=null){
					for (Iterator iterator = catLabelList.iterator(); iterator
							.hasNext();) {
						TyLabelBean tyLabelBean2 = (TyLabelBean) iterator.next();
						List<TyLabelBean> subList = new ArrayList<TyLabelBean>();
						Map<String, Object> pa = new HashMap<String, Object>();
						pa.put("customerId", tyOrgcustomerBean.getId());
						pa.put("catId", tyLabelBean2.getId());
						subList = tyLabelService.getCustSubList(pa);
						if(subList!=null){
							tyLabelBean2.setSubList(subList);
						}
					}
				}
			}
	    }
	    tyOrgcustomerBean.setLabelCatList(catLabelList);
	    
		return setSuccessModelMap(modelMap, tyOrgcustomerBean,dicts);
	}
	
	
	
	
	private boolean isDirector(List<SysUserRoleBean> surb){
		boolean result=false;
		for (Iterator iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if("selldirector".equals(sysUserRoleBean.getRoleId())){
				result=true;
				break;
			}
		}
		return result;
	}
	private boolean isSeniorseller(List<SysUserRoleBean> surb){
		boolean result=false;
		for (Iterator iterator = surb.iterator(); iterator.hasNext();) {
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
		for (Iterator iterator = surb.iterator(); iterator.hasNext();) {
			SysUserRoleBean sysUserRoleBean = (SysUserRoleBean) iterator.next();
			if("seller".equals(sysUserRoleBean.getRoleId())){
				result=true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 对请求参数需要做接入权限判断
	 * 
	 * @param request
	 */
	private String checkInterfaceRight(HttpServletRequest request) throws Exception {
		// TODO 需要再做修改
		String token = request.getParameter("token");
		if (StringUtils.isBlank(token)) {
			throw new IllegalParameterException("参数异常！");
		}
		DESPlus des = new DESPlus("%#[ekpSSOWeb]@!?");//自定义密钥    
		String desStr ="";
		try {
			desStr = des.decrypt(token);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("无效令牌！");
		}
		String[] desArray = desStr.split(";");
		
		Date date = new Date(Long.valueOf(desArray[1]));
		Date now = new Date();
		if (Math.abs(date.getTime() - now.getTime()) > 1000 * 60 * 60 * 24L) { //2小时之内
			throw new BusinessException("令牌已失效！");
		}
		SysUser currUser = sysUserService.queryByAccount(desArray[0]);
		if(null==currUser || StringUtils.isBlank(currUser.getId())){
			throw new BusinessException("账户不存在");
		}
		return currUser.getId();
	}

}
