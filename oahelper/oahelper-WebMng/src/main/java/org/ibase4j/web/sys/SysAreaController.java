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
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.sys.SysAreaBean;
import org.ibase4j.service.sys.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * 区域控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "区域管理", description = "区域管理")
@RequestMapping(value = "sys/sysArea", method = RequestMethod.POST)
public class SysAreaController extends BaseController {
	@Autowired
	private SysAreaService sysAreaService;
	// 查询区域
	@ApiOperation(value = "查询区域")
	@RequiresPermissions("sys.sysArea.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<SysAreaBean> list = sysAreaService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = null;
		//添加字典翻译
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "区域详情")
	@RequiresPermissions("sys.sysArea.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysAreaBean sysAreaBean = new SysAreaBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			SysArea record = sysAreaService.queryById(id);
			try {
	            BeanUtils.copyProperties(sysAreaBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				sysAreaBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(sysAreaBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, sysAreaBean,dicts);
	}

	
	// 新增区域
	@ApiOperation(value = "添加区域")
	@RequiresPermissions("sys.sysArea.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysArea record = Request2ModelUtil.covert(SysArea.class, request);
		sysAreaService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改区域
	@ApiOperation(value = "修改区域")
	@RequiresPermissions("sys.sysArea.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysArea record = Request2ModelUtil.covert(SysArea.class, request);
		sysAreaService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除区域
	@ApiOperation(value = "删除区域")
	@RequiresPermissions("sys.sysArea.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		SysArea record = sysAreaService.queryById(id);
		if(record!=null){
			sysAreaService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
