package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoRemindInform;
import com.tfzq.pr.model.pr.GoInformBean;
import com.tfzq.pr.model.pr.GoRemindInformBean;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoRemindInformService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 流程提醒控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "流程提醒管理", description = "流程提醒管理")
@RequestMapping(value = "pr/goRemindInform", method = RequestMethod.POST)
public class GoRemindInformController extends BaseController {
	@Autowired
	private GoRemindInformService goRemindInformService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private GoInformService goInformService;

	// 查询流程提醒
	@ApiOperation(value = "查询流程提醒")
	//@RequiresPermissions("pr.goRemindInform.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		if(StringUtils.isNotEmpty(params.get("remindTypes").toString())){
			String remindTypes = params.get("remindTypes").toString();
			List<String> remindTypeList = Arrays.asList(remindTypes.split(","));
			params.put("remindTypeList", remindTypeList);
		}
		
		PageInfo<GoRemindInformBean> list = goRemindInformService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> remind_statusMap = sysDicService.queryDicByDicIndexKey("REMIND_STATUS");
		addDictFromMap(dicts,"REMIND_STATUS", remind_statusMap);
		for (GoRemindInformBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getRemindStatus())) {
				singleBean.setRemindStatusText(remind_statusMap.get(singleBean.getRemindStatus().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}
	
	@ApiOperation(value = "根据条件查询流程提醒")
	//@RequiresPermissions("pr.goRemindInform.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		List<GoRemindInformBean> list = goRemindInformService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> remind_statusMap = sysDicService.queryDicByDicIndexKey("REMIND_STATUS");
		addDictFromMap(dicts,"REMIND_STATUS", remind_statusMap);
		for (GoRemindInformBean singleBean : list) {
			if (StringUtils.isNotEmpty(singleBean.getRemindStatus())) {
				singleBean.setRemindStatusText(remind_statusMap.get(singleBean.getRemindStatus().toString()));
			}
			if(StringUtils.isNotEmpty(singleBean.getId())){
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("srcId",singleBean.getId());
				params1.put("deleteFlag", "0");
				List<GoInformBean> goInform = goInformService.queryByCondition(params1);
				if(goInform!=null && goInform.size()>0){
					singleBean.setRemark(goInform.get(0).getRemark());
				}
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "流程提醒详情")
	//@RequiresPermissions("pr.goRemindInform.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoRemindInformBean goRemindInformBean = new GoRemindInformBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			GoRemindInform record = goRemindInformService.queryById(id);
			try {
	            BeanUtils.copyProperties(goRemindInformBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goRemindInformBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> remind_statusMap = sysDicService.queryDicByDicIndexKey("REMIND_STATUS");
		addDictFromMap(dicts,"REMIND_STATUS", remind_statusMap);
	    if(goRemindInformBean!=null){
			if (StringUtils.isNotEmpty(goRemindInformBean.getRemindStatus())) {
				goRemindInformBean.setRemindStatusText(remind_statusMap.get(goRemindInformBean.getRemindStatus().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, goRemindInformBean,dicts);
	}

	
	// 新增流程提醒
	@ApiOperation(value = "添加流程提醒")
	//@RequiresPermissions("pr.goRemindInform.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoRemindInform record = Request2ModelUtil.covert(GoRemindInform.class, request);
		goRemindInformService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改流程提醒
	@ApiOperation(value = "修改流程提醒")
	//@RequiresPermissions("pr.goRemindInform.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoRemindInform record = Request2ModelUtil.covert(GoRemindInform.class, request);
		goRemindInformService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除流程提醒
	@ApiOperation(value = "删除流程提醒")
	//@RequiresPermissions("pr.goRemindInform.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoRemindInform record = goRemindInformService.queryById(id);
		if(record!=null){
			goRemindInformService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
