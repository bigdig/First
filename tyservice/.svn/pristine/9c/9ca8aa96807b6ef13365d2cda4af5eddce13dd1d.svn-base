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
import com.tfzq.service.TyServiceorgService;
import com.tfzq.ty.model.generator.TyActivitysign;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.ty.TyActivitysignBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 会议客户关系控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "会议客户关系管理", description = "会议客户关系管理")
@RequestMapping(value = "ty/tyDccallcust", method = RequestMethod.POST)
public class TyDccallcustController extends BaseController {
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyServiceorgService tyServiceorgService;

	// 查询会议客户关系
	@ApiOperation(value = "查询会议客户关系")
	@RequiresPermissions("ty.tyDccall.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,@RequestParam(value = "callId", required = false) String callId) {
		List<TyActivitysignBean> list = tyActivitysignService.queryByActId(callId);
		if(list!=null && list.size()>0){
			for (TyActivitysignBean custBean : list) {
				if(custBean.getInWhitelist().equals("1")){
					if(StringUtils.isNoneBlank(custBean.getCustId())){
						TyOrgcustomer cust = tyOrgcustomerService.queryById(custBean.getCustId());
						if(StringUtils.isNoneBlank(cust.getOrgId())){
							TyServiceorg org = tyServiceorgService.queryById(cust.getOrgId());
							if(StringUtils.isNoneBlank(org.getOrgSimpleName())){
								custBean.setOrgSimpleName(org.getOrgSimpleName());
							}
						}
						custBean.setCustName(cust.getCustName());
						custBean.setArea(cust.getArea());
						custBean.setServiceSaler(cust.getServiceSaler());
					}
				}else{
				}
			}
		}
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "会议客户关系详情")
	@RequiresPermissions("ty.tyDccall.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyActivitysignBean tyDccallcustBean = new TyActivitysignBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyActivitysign record = tyActivitysignService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyDccallcustBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyDccallcustBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyDccallcustBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyDccallcustBean,dicts);
	}

	
	// 新增会议客户关系
	@ApiOperation(value = "添加会议客户关系")
	@RequiresPermissions("ty.tyDccall.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyActivitysign record = Request2ModelUtil.covert(TyActivitysign.class, request);
		tyActivitysignService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改会议客户关系
	@ApiOperation(value = "修改会议客户关系")
	@RequiresPermissions("ty.tyDccall.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyActivitysign record = Request2ModelUtil.covert(TyActivitysign.class, request);
		tyActivitysignService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除会议客户关系
	@ApiOperation(value = "删除会议客户关系")
	@RequiresPermissions("ty.tyDccall.delete")
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
