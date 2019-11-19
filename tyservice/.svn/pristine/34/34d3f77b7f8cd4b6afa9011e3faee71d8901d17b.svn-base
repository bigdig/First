package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.ty.TyActivitysignBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 沙龙客户关系控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "沙龙客户关系管理", description = "沙龙客户关系管理")
@RequestMapping(value = "ty/tyDcsaloncust", method = RequestMethod.POST)
public class TyDcsaloncustController extends BaseController {
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;

	// 查询沙龙客户关系
	@ApiOperation(value = "查询沙龙客户关系")
	@RequiresPermissions("ty.tyDcsalon.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,@RequestParam(value = "salonId", required = false) String salonId) {
		List<TyActivitysignBean> list = tyActivitysignService.queryByActId(salonId);
//		if(list!=null && list.size()>0){
//			for (TyActivitysignBean saloncustBean : list) {
//				TyOrgcustomer cust = tyOrgcustomerService.queryById(saloncustBean.getCustId());
//				saloncustBean.setCustName(cust.getCustName());
//				saloncustBean.setOrgName(cust.getOrgName());
//				saloncustBean.setArea(cust.getArea());
//				saloncustBean.setServiceSaler(cust.getServiceSaler());
//			}
//		}
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "沙龙客户关系详情")
	@RequiresPermissions("ty.tyDcsalon.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivitysignBean tyDcsaloncustBean = new TyActivitysignBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivitysign record = tyActivitysignService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDcsaloncustBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDcsaloncustBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDcsaloncustBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyDcsaloncustBean,dicts);
	}

	
	// 新增沙龙客户关系
	@ApiOperation(value = "添加沙龙客户关系")
	@RequiresPermissions("ty.tyDcsalon.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyActivitysign record = Request2ModelUtil.covert(TyActivitysign.class, request);
		tyActivitysignService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改沙龙客户关系
	@ApiOperation(value = "修改沙龙客户关系")
	@RequiresPermissions("ty.tyDcsalon.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "custIds[]", required = true) List<String> custIds,
			@RequestParam(value = "salonId", required = true) String salonId) {
		tyActivitysignService.deleteByActId(salonId);
		addActivitysign(salonId,custIds);
		return setSuccessModelMap(modelMap);
	}

	/**
	 * 添加参加活动的客户信息
	 * @param actId
	 * @param custIds
	 */
	private void addActivitysign(String actId,List<String> custIds){
		if(custIds!=null && custIds.size()>0){
			for (String custId : custIds) {
				TyActivitysign topicCust = new TyActivitysign();
				topicCust.setActivityId(actId);
				TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
				if(customer!=null){
					topicCust.setCustId(custId);
					topicCust.setCustName(customer.getCustName());
					tyActivitysignService.add(topicCust);
				}
			}
		}

	}
	
	// 删除沙龙客户关系
	@ApiOperation(value = "删除沙龙客户关系")
	@RequiresPermissions("ty.tyDcsalon.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyActivitysign record = tyActivitysignService.queryById(id);
		if(record!=null){
			tyActivitysignService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
