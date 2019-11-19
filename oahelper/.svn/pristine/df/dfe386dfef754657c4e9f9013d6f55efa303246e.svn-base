package ${packageName};

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
import ${beanGenPackage}.${bean};
import ${beanCusPackage}.${bean}Bean;
import ${servicePackage}.${bean}Service;
<#list table.transFields as item>
<#if item.entityFlag>
import ${servicePackage}.${item.ref}Service;
import ${beanGenPackage}.${item.ref};
</#if>
</#list>
import com.tfzq.util.PbFileUtils;

/**
 * ${tableDesc}控制类
 * 
 * @author ${author} 
 */
@RestController
@Api(value = "${tableDesc}管理", description = "${tableDesc}管理")
@RequestMapping(value = "${module}/${beanObj}", method = RequestMethod.POST)
public class ${bean}Controller extends BaseController {
	@Autowired
	private ${bean}Service ${beanObj}Service;
	@Autowired
	private SysDicService sysDicService;
	<#list table.transFields as item>
    <#if item.entityFlag>
	@Autowired
	private ${item.ref}Service ${item.refObj}Service;
    </#if>
    </#list>

	// 查询${tableDesc}
	@ApiOperation(value = "查询${tableDesc}")
	//@RequiresPermissions("${module}.${beanObj}.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<${bean}Bean> list = ${beanObj}Service.queryBeans(params);
		
		<#if table.hasSearchDictField>
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		<#else>
		Map<String,List<DictItem>> dicts = null;
		</#if>
		//添加字典翻译
		<#list table.transFields as item>
	    <#if item.dictFlag && item.ref!="">
		Map<String,String> ${item.refObj} = sysDicService.queryDicByDicIndexKey("${item.ref}");
		<#if item.searchFlag >
		addDictFromMap(dicts,"${item.ref}", ${item.refObj});
	    </#if>
	    </#if>
	    </#list>
	    <#if table.hasDictField || table.hasEntityField>
		for (${bean}Bean singleBean : list.getList()) {
			<#list table.transFields as item>
		    <#if item.entityFlag>
			if(StringUtils.isNotEmpty(singleBean.get${item.code?cap_first}())){
				${item.ref} temp = ${item.refObj}Service.queryById(singleBean.get${item.code?cap_first}());
				singleBean.set${item.nameField?cap_first}(temp==null?"":temp.get${item.nameField?cap_first}());
			}
		    <#elseif item.dictFlag && item.ref!="">
			if (StringUtils.isNotEmpty(singleBean.get${item.idField?cap_first}())) {
				singleBean.set${item.nameField?cap_first}(${item.refObj}.get(singleBean.get${item.idField?cap_first}().toString()));
			}
		    </#if>
		    </#list>
		}
		</#if>
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "${tableDesc}详情")
	//@RequiresPermissions("${module}.${beanObj}.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		${bean}Bean ${beanObj}Bean = new ${bean}Bean();
		<#if table.hasDictField || table.hasEntityField>
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		<#else>
		Map<String,List<DictItem>> dicts = null;
		</#if>
		if(StringUtils.isNotEmpty(id)){
			${bean} record = ${beanObj}Service.queryById(id);
			try {
	            BeanUtils.copyProperties(${beanObj}Bean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				${beanObj}Bean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		<#list table.transFields as item>
	    <#if item.entityFlag>
		List<${item.ref}> ${item.refObj}s = ${item.refObj}Service.getAllRecords();
		addDictFromModel(dicts,"${item.ref}s", ${item.refObj}s,"${item.idField}","${item.nameField}");
	    <#elseif item.dictFlag && item.ref!="">
		Map<String,String> ${item.refObj} = sysDicService.queryDicByDicIndexKey("${item.ref}");
		addDictFromMap(dicts,"${item.ref}", ${item.refObj});
	    </#if>
	    </#list>
	    if(${beanObj}Bean!=null){
		<#list table.transFields as item>
	    <#if item.entityFlag>
			if(StringUtils.isNotEmpty(${beanObj}Bean.get${item.code?cap_first}())){
				${item.ref} temp = ${item.refObj}Service.queryById(${beanObj}Bean.get${item.code?cap_first}());
				${beanObj}Bean.set${item.nameField?cap_first}(temp==null?"":temp.get${item.nameField?cap_first}());
			}
	    <#elseif item.dictFlag && item.ref!="">
			if (StringUtils.isNotEmpty(${beanObj}Bean.get${item.idField?cap_first}())) {
				${beanObj}Bean.set${item.nameField?cap_first}(${item.refObj}.get(${beanObj}Bean.get${item.idField?cap_first}().toString()));
			}
	    </#if>
	    </#list>
	    <#list table.filesFields as item>
		    if(StringUtils.isNotEmpty(${beanObj}Bean.get${item.code?cap_first}())&&
					StringUtils.isNotEmpty(${beanObj}Bean.get${item.ref?cap_first}())){
				${beanObj}Bean.set${item.code?cap_first}Bean(PbFileUtils.fillFiles(${beanObj}Bean.get${item.code?cap_first}().split(","),
						${beanObj}Bean.get${item.ref?cap_first}().split(",")));
			}
		</#list>
	    }
	    
		return setSuccessModelMap(modelMap, ${beanObj}Bean,dicts);
	}

	
	// 新增${tableDesc}
	@ApiOperation(value = "添加${tableDesc}")
	//@RequiresPermissions("${module}.${beanObj}.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		${bean} record = Request2ModelUtil.covert(${bean}.class, request);
		<#if table.hasDelFlag>
		record.setDeleteFlag("0");
		</#if>
		${beanObj}Service.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改${tableDesc}
	@ApiOperation(value = "修改${tableDesc}")
	//@RequiresPermissions("${module}.${beanObj}.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		${bean} record = Request2ModelUtil.covert(${bean}.class, request);
		${beanObj}Service.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除${tableDesc}
	@ApiOperation(value = "删除${tableDesc}")
	//@RequiresPermissions("${module}.${beanObj}.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		${bean} record = ${beanObj}Service.queryById(id);
		if(record!=null){
		<#if table.hasDelFlag>
			${beanObj}Service.delete(id);
		<#else>
			${beanObj}Service.deletePhysical(id);
		</#if>
		}
		return setSuccessModelMap(modelMap);
	}
}
