package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
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
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.core.support.DictItem;
import com.tfzq.pr.model.generator.GoVoteItem;
import com.tfzq.pr.model.pr.GoVoteItemBean;
import com.tfzq.service.GoVoteItemService;
import com.tfzq.util.PbFileUtils;

/**
 * 投票选项控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "投票选项管理", description = "投票选项管理")
@RequestMapping(value = "pr/goVoteItem", method = RequestMethod.POST)
public class GoVoteItemController extends BaseController {
	@Autowired
	private GoVoteItemService goVoteItemService;
	@Autowired
	private SysDicService sysDicService;

	// 查询投票选项
	@ApiOperation(value = "查询投票选项")
	//@RequiresPermissions("pr.goVoteItem.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","sort_no asc,id desc");
		PageInfo<GoVoteItemBean> list = goVoteItemService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}
	
	// 查询投票选项
	@ApiOperation(value = "条件查询投票选项")
	//@RequiresPermissions("pr.goVoteItem.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","sort_no asc,id desc");
		List<GoVoteItemBean> list = goVoteItemService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "投票选项详情")
	//@RequiresPermissions("pr.goVoteItem.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoVoteItemBean goVoteItemBean = new GoVoteItemBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			GoVoteItem record = goVoteItemService.queryById(id);
			try {
	            BeanUtils.copyProperties(goVoteItemBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goVoteItemBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(goVoteItemBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, goVoteItemBean,dicts);
	}

	
	// 新增投票选项
	@ApiOperation(value = "添加投票选项")
	//@RequiresPermissions("pr.goVoteItem.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoVoteItem record = Request2ModelUtil.covert(GoVoteItem.class, request);
		goVoteItemService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改投票选项
	@ApiOperation(value = "修改投票选项")
	//@RequiresPermissions("pr.goVoteItem.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoVoteItem record = Request2ModelUtil.covert(GoVoteItem.class, request);
		goVoteItemService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除投票选项
	@ApiOperation(value = "删除投票选项")
	//@RequiresPermissions("pr.goVoteItem.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoVoteItem record = goVoteItemService.queryById(id);
		if(record!=null){
			goVoteItemService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
