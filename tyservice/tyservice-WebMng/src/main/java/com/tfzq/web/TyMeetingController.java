package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.core.support.DictItem;
import com.tfzq.ty.model.generator.TyMeeting;
import com.tfzq.ty.model.ty.TyMeetingBean;
import com.tfzq.service.TyMeetingService;

/**
 * 会议控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "会议管理", description = "会议管理")
@RequestMapping(value = "ty/tyMeeting", method = RequestMethod.POST)
public class TyMeetingController extends BaseController {
	@Autowired
	private TyMeetingService tyMeetingService;
	// 查询会议
	@ApiOperation(value = "查询会议")
	@RequiresPermissions("ty.tyMeeting.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<TyMeetingBean> list = tyMeetingService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "会议详情")
	@RequiresPermissions("ty.tyMeeting.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyMeetingBean tyMeetingBean = new TyMeetingBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyMeeting record = tyMeetingService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyMeetingBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyMeetingBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyMeetingBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyMeetingBean,dicts);
	}

	
	// 新增会议
	@ApiOperation(value = "添加会议")
	@RequiresPermissions("ty.tyMeeting.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyMeeting record = Request2ModelUtil.covert(TyMeeting.class, request);
		tyMeetingService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改会议
	@ApiOperation(value = "修改会议")
	@RequiresPermissions("ty.tyMeeting.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyMeeting record = Request2ModelUtil.covert(TyMeeting.class, request);
		tyMeetingService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除会议
	@ApiOperation(value = "删除会议")
	@RequiresPermissions("ty.tyMeeting.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyMeeting record = tyMeetingService.queryById(id);
		if(record!=null){
			tyMeetingService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
