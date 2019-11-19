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
import com.tfzq.pr.model.generator.GoFileArchive;
import com.tfzq.pr.model.pr.GoFileArchiveBean;
import com.tfzq.service.GoFileArchiveService;
import com.tfzq.util.PbFileUtils;

/**
 * 文件归档控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "文件归档管理", description = "文件归档管理")
@RequestMapping(value = "pr/goFileArchive", method = RequestMethod.POST)
public class GoFileArchiveController extends BaseController {
	@Autowired
	private GoFileArchiveService goFileArchiveService;
	@Autowired
	private SysDicService sysDicService;

	// 查询文件归档
	@ApiOperation(value = "查询文件归档")
	//@RequiresPermissions("pr.goFileArchive.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","file_weight desc,id desc");
		PageInfo<GoFileArchiveBean> list = goFileArchiveService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> file_catalogMap = sysDicService.queryDicByDicIndexKey("FILE_CATALOG");
		addDictFromMap(dicts,"FILE_CATALOG", file_catalogMap);
		for (GoFileArchiveBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getFileCatalog())) {
				singleBean.setFileCatalogText(file_catalogMap.get(singleBean.getFileCatalog().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "文件归档详情")
	//@RequiresPermissions("pr.goFileArchive.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoFileArchiveBean goFileArchiveBean = new GoFileArchiveBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			GoFileArchive record = goFileArchiveService.queryById(id);
			try {
	            BeanUtils.copyProperties(goFileArchiveBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goFileArchiveBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> file_catalogMap = sysDicService.queryDicByDicIndexKey("FILE_CATALOG");
		addDictFromMap(dicts,"FILE_CATALOG", file_catalogMap);
	    if(goFileArchiveBean!=null){
			if (StringUtils.isNotEmpty(goFileArchiveBean.getFileCatalog())) {
				goFileArchiveBean.setFileCatalogText(file_catalogMap.get(goFileArchiveBean.getFileCatalog().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, goFileArchiveBean,dicts);
	}

	
	// 新增文件归档
	@ApiOperation(value = "添加文件归档")
	//@RequiresPermissions("pr.goFileArchive.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoFileArchive record = Request2ModelUtil.covert(GoFileArchive.class, request);
		goFileArchiveService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改文件归档
	@ApiOperation(value = "修改文件归档")
	//@RequiresPermissions("pr.goFileArchive.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoFileArchive record = Request2ModelUtil.covert(GoFileArchive.class, request);
		goFileArchiveService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除文件归档
	@ApiOperation(value = "删除文件归档")
	//@RequiresPermissions("pr.goFileArchive.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoFileArchive record = goFileArchiveService.queryById(id);
		if(record!=null){
			goFileArchiveService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
