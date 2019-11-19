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
import com.tfzq.pr.model.generator.GoVote;
import com.tfzq.pr.model.pr.GoVoteBean;
import com.tfzq.service.GoVoteService;
import com.tfzq.util.PbFileUtils;

/**
 * 投票控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "投票管理", description = "投票管理")
@RequestMapping(value = "pr/goVote", method = RequestMethod.POST)
public class GoVoteController extends BaseController {
	@Autowired
	private GoVoteService goVoteService;
	@Autowired
	private SysDicService sysDicService;

	// 查询投票
	@ApiOperation(value = "查询投票")
	//@RequiresPermissions("pr.goVote.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","sort_no asc,id desc");
		PageInfo<GoVoteBean> list = goVoteService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts,"YES_NO", yes_noMap);
		for (GoVoteBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getIsCustomizable())) {
				singleBean.setIsCustomizableText(yes_noMap.get(singleBean.getIsCustomizable().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsMulti())) {
				singleBean.setIsMultiText(yes_noMap.get(singleBean.getIsMulti().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}
	
	@ApiOperation(value = "条件查询")
	//@RequiresPermissions("pr.goVote.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","sort_no asc,id desc");
		List<GoVoteBean> list = goVoteService.queryByCondition(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts,"YES_NO", yes_noMap);
		for (GoVoteBean singleBean : list) {
			if (StringUtils.isNotEmpty(singleBean.getIsCustomizable())) {
				singleBean.setIsCustomizableText(yes_noMap.get(singleBean.getIsCustomizable().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsMulti())) {
				singleBean.setIsMultiText(yes_noMap.get(singleBean.getIsMulti().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "投票详情")
	//@RequiresPermissions("pr.goVote.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoVoteBean goVoteBean = new GoVoteBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			GoVote record = goVoteService.queryById(id);
			try {
	            BeanUtils.copyProperties(goVoteBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goVoteBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts,"YES_NO", yes_noMap);
	    if(goVoteBean!=null){
			if (StringUtils.isNotEmpty(goVoteBean.getIsCustomizable())) {
				goVoteBean.setIsCustomizableText(yes_noMap.get(goVoteBean.getIsCustomizable().toString()));
			}
			if (StringUtils.isNotEmpty(goVoteBean.getIsMulti())) {
				goVoteBean.setIsMultiText(yes_noMap.get(goVoteBean.getIsMulti().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, goVoteBean,dicts);
	}

	
	// 新增投票
	@ApiOperation(value = "添加投票")
	//@RequiresPermissions("pr.goVote.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoVote record = Request2ModelUtil.covert(GoVote.class, request);
		goVoteService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改投票
	@ApiOperation(value = "修改投票")
	//@RequiresPermissions("pr.goVote.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoVote record = Request2ModelUtil.covert(GoVote.class, request);
		goVoteService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除投票
	@ApiOperation(value = "删除投票")
	//@RequiresPermissions("pr.goVote.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoVote record = goVoteService.queryById(id);
		if(record!=null){
			goVoteService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
