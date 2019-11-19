package org.ibase4j.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysPosition;
import org.ibase4j.model.sys.SysPositionBean;
import org.ibase4j.service.sys.SysPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * 职务控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "职务管理", description = "职务管理")
@RequestMapping(value = "sys/sysPosition", method = RequestMethod.POST)
public class SysPositionController extends BaseController {
	@Autowired
	private SysPositionService sysPositionService;
	// 查询职务
	@ApiOperation(value = "查询职务")
	@RequiresPermissions("sys.sysPosition.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<SysPositionBean> list = sysPositionService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "职务详情")
	@RequiresPermissions("sys.sysPosition.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysPositionBean sysPositionBean = new SysPositionBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			SysPosition record = sysPositionService.queryById(id);
			try {
	            BeanUtils.copyProperties(sysPositionBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				sysPositionBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(sysPositionBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, sysPositionBean,dicts);
	}

	
	// 新增职务
	@ApiOperation(value = "添加职务")
	@RequiresPermissions("sys.sysPosition.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysPosition record = Request2ModelUtil.covert(SysPosition.class, request);
		sysPositionService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改职务
	@ApiOperation(value = "修改职务")
	@RequiresPermissions("sys.sysPosition.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysPosition record = Request2ModelUtil.covert(SysPosition.class, request);
		sysPositionService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除职务
	@ApiOperation(value = "删除职务")
	@RequiresPermissions("sys.sysPosition.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		SysPosition record = sysPositionService.queryById(id);
		if(record!=null){
			sysPositionService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
