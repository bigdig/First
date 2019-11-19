package org.ibase4j.web.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDic;
import org.ibase4j.model.generator.SysDicIndex;
import org.ibase4j.service.sys.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 字典管理
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:14:34
 */
@RestController
@Api(value = "字典管理", description = "字典管理")
@RequestMapping(method = RequestMethod.POST)
public class SysDicController extends BaseController {
	@Autowired
	private SysDicService sysDicService;

	@ApiOperation(value = "查询字典")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dicIndex/read/list")
	public Object getDicIndex(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		PageInfo<?> list = sysDicService.queryDicIndex(params);
		return setSuccessModelMap(modelMap, list);
	}

	@ApiOperation(value = "字典详情")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dicIndex/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysDicIndex record = sysDicService.queryDicIndexById(id);
		return setSuccessModelMap(modelMap, record);
	}

	@ApiOperation(value = "根据关键字查询字典列表")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dic/read/key")
	public Object getDicByKey(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "key", required = false) String key) {
		Map<String, String> result = sysDicService.queryDicByDicIndexKey(key);
		return setSuccessModelMap(modelMap, result);
	}

	@ApiOperation(value = "添加字典")
	@RequiresPermissions("sys.dic.add")
	@RequestMapping(value = "sys/dicIndex/add", method = RequestMethod.POST)
	public Object addDicIndex(HttpServletRequest request, ModelMap modelMap) {
		SysDicIndex record = Request2ModelUtil.covert(SysDicIndex.class, request);
		record.setCatalogId("1");
		
		record.setUpdateBy(String.valueOf(System.currentTimeMillis()));
		sysDicService.addDicIndex(record);
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "修改字典")
	@RequiresPermissions("sys.dic.update")
	@RequestMapping(value = "sys/dicIndex/update", method = RequestMethod.POST)
	public Object updateDicIndex(HttpServletRequest request, ModelMap modelMap) {
		SysDicIndex record = Request2ModelUtil.covert(SysDicIndex.class, request);
		sysDicService.updateDicIndex(record);
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "删除字典")
	@RequiresPermissions("sys.dic.delete")
	@RequestMapping(value = "sys/dicIndex/delete", method = RequestMethod.POST)
	public Object deleteDicIndex(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		sysDicService.deleteDicIndex(id);
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "查询字典项")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dic/read/list")
	public Object getDic(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		PageInfo<?> list = sysDicService.queryDic(params);
		return setSuccessModelMap(modelMap, list);
	}
	
	@ApiOperation(value = "根据id查询字典项")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dic/list")
	public Object getDicbyid(ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		
		List<SysDic> list = sysDicService.selectByIndexId(id);
		return setSuccessModelMap(modelMap, list);
	}
	

	@ApiOperation(value = "字典项详情")
	@RequiresPermissions("sys.dic.read")
	@RequestMapping(value = "sys/dic/read/detail")
	public Object dicDetail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysDic record = sysDicService.queryDicById(id);
		return setSuccessModelMap(modelMap, record);
	}

	@ApiOperation(value = "添加字典项")
	@RequiresPermissions("sys.dic.add")
	@RequestMapping(value = "sys/dic/add", method = RequestMethod.POST)
	public Object addDic(HttpServletRequest request, ModelMap modelMap) {
		SysDic record = Request2ModelUtil.covert(SysDic.class, request);
		sysDicService.addDic(record);
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "修改字典项")
	@RequiresPermissions("sys.dic.add")
	@RequestMapping(value = "sys/dic/update")
	public Object updateDic( ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "data", required = false) String list) {
		JSONArray myJsonArray =(JSONArray) JSONArray.parse(list);
		
		List<String> tempList = new ArrayList<String>();
		for(Object dic:myJsonArray){
			JSONObject myjObject =(JSONObject)dic;
			if(!myjObject.getString("code").equals("")){
				if(tempList.contains(myjObject.getString("code"))){
					throw new BusinessException("请不要输入相同的code!");
				}else{
					tempList.add(myjObject.getString("code"));
				}
			}
		}
		
		//System.out.print(list);
		//先删除，再新增
		sysDicService.deleteByIndexId(id);
		
		int i = 0;
		/*for(Object dic:myJsonArray){
			JSONObject myjObject =(JSONObject)dic;
			i++;
			if(myjObject.getString("id").equals("")){
			   
				if(!myjObject.getString("code").equals("")){
					SysDic records=new SysDic();
					records.setIndexId(id);
					records.setEnable(true);
					records.setEditable(true);
					records.setSortNo(i);
					records.setCode(myjObject.getString("code"));
					records.setCodeText(myjObject.getString("CodeText"));
					sysDicService.addDic(records);
				}
			}else{
				if(!myjObject.getString("code").equals("")){
					SysDic records=new SysDic();
					records.setIndexId(id);
//					records.setCreateTime(new Date());
					records.setEnable(true);
					records.setEditable(true);
					records.setSortNo(i);
					records.setId(myjObject.getString("id"));
					records.setCode(myjObject.getString("code"));
					records.setCodeText(myjObject.getString("CodeText"));
					sysDicService.updateDic(records);
				}else{
					sysDicService.deleteDic(myjObject.getString("id"));
				}
			}
		}*/
		for(Object dic:myJsonArray){
			JSONObject myjObject =(JSONObject)dic;
			i++;
			if(!myjObject.getString("code").equals("")){
				SysDic records=new SysDic();
				records.setIndexId(id);
				records.setEnable(true);
				records.setEditable(true);
				records.setSortNo(i);
				records.setCode(myjObject.getString("code"));
				records.setCodeText(myjObject.getString("CodeText"));
				sysDicService.addDic(records);
			}
		}
		sysDicService.clearDicCache(id);
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "删除字典项")
	@RequiresPermissions("sys.dic.delete")
	@RequestMapping(value = "sys/dic/delete", method = RequestMethod.POST)
	public Object deleteDic(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		sysDicService.deleteDic(id);
		return setSuccessModelMap(modelMap);
	}
}
