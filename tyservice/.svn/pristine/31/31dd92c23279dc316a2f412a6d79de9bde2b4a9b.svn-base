package org.ibase4j.web.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysEvent;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysEventManageService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 事件管理控制类
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "事件管理", description = "事件管理")
@RequestMapping(value = "sys/event", method = RequestMethod.POST)
public class SysEventController extends BaseController {
	@Autowired
	private SysEventManageService sysEventService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;

	// 查询事件
	@ApiOperation(value = "查询事件")
	@RequiresPermissions("sys.event.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		Map<String, String> requestURIMap = sysDicService
				.queryDicByDicIndexKey("REQUESTURI");
		List<String> dictList = new ArrayList<String>(requestURIMap.keySet());  
//		params.put("title", "bizspace");
		params.put("dictList", dictList);
		PageInfo<SysEvent> list = sysEventService.query(params);
		
		for(SysEvent event:list.getList()){
			SysUser user= sysUserService.queryById(event.getCreateBy());
			event.setCreateBy(user==null?event.getCreateBy():user.getUserName());
			String name = requestURIMap.get(event.getRequestUri());
			event.setRequestUri(name==null?event.getRequestUri():name);
		}
		return setSuccessModelMap(modelMap, list);
	}
	
	@RequiresPermissions("sys.event.read")
	@RequestMapping(value = "/read/dics")
	public Object getUri(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Map<String, String>> dicts = new HashMap<String, Map<String, String>>();
		Map<String, String> requestURIMap = sysDicService
				.queryDicByDicIndexKey("REQUESTURI");
		addDict(dicts, "REQUESTURI", requestURIMap);
		return setSuccessModelMap(modelMap, null,dicts);
	}

}
