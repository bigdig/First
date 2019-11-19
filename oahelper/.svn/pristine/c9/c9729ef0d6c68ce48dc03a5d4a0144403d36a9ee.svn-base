/**
 * 
 */
package org.ibase4j.core.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.ibase4j.core.Constants;
import org.ibase4j.core.exception.BaseException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.WebUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 控制器基类
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:47:58
 */
public abstract class BaseController {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	/** 获取当前用户Id */
	protected String getCurrUser() {
		return WebUtil.getCurrentUser();
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap) {
		return setSuccessModelMap(modelMap, null);
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap, Object data) {
		return setModelMap(modelMap, HttpCode.OK, data);
	}

	/** 设置成功响应代码 */
	protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap, Object data, Object dicts) {
		return setModelMap(modelMap, HttpCode.OK, data, dicts);
	}
	
	/** 设置成功响应代码 */
	protected ResponseEntity<ModelMap> setSuccessModelMapEx(ModelMap modelMap, Object data, Object dicts,Object dictsEx) {
		return setModelMap(modelMap, HttpCode.OK, data, dicts,dictsEx);
	}

	/** 设置响应代码 */
	protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpCode code) {
		return setModelMap(modelMap, code, null);
	}

	/** 设置响应代码 */
	protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpCode code, Object data) {
		return setModelMap(modelMap, code, data, null);
	}

	/** 设置响应代码 */
	protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpCode code, Object data, Object dicts) {
		modelMap.remove("void");
		if (data != null) {
			modelMap.put("data", data);
		}
		if (dicts != null) {
			modelMap.put("dicts", dicts);
		}
		modelMap.put("httpCode", code.value());
		modelMap.put("msg", code.msg());
		modelMap.put("timestamp", System.currentTimeMillis());
		return ResponseEntity.ok(modelMap);
	}
	
	/** 设置响应代码 */
	protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpCode code, Object data, Object dicts,Object dictsEx) {
		modelMap.remove("void");
		if (data != null) {
			modelMap.put("data", data);
		}
		if (dicts != null) {
			modelMap.put("dicts", dicts);
		}
		if (dictsEx != null) {
			modelMap.put("dictsEx", dictsEx);
		}
		modelMap.put("httpCode", code.value());
		modelMap.put("msg", code.msg());
		modelMap.put("timestamp", System.currentTimeMillis());
		return ResponseEntity.ok(modelMap);
	}

	protected Map<String, Map<String, String>> addDict(Map<String, Map<String, String>> dicts, String dict_index, Map<String, String> dict) {
		dicts.put(dict_index, dict);
		return dicts;
	}

	protected Map<String, List<DictItem>> addDictFromMap(Map<String, List<DictItem>> dicts, String dict_index, Map<String, String> dict) {
		List<DictItem> items = new ArrayList<DictItem>();
		for (Entry<String, String> entry : dict.entrySet()) {
			DictItem item = new DictItem();
			item.setId(entry.getKey());
			item.setText(entry.getValue());
			items.add(item);
		}
		dicts.put(dict_index, items);
		return dicts;
	}

	protected Map<String, List<DictItem>> addDictFromModel(Map<String, List<DictItem>> dicts, String dict_index, List olist,String idField, String nameFiled) {
		List<DictItem> items = new ArrayList<DictItem>();
		for (Object entry : olist) {
			DictItem item = new DictItem();
			item.setId(getValueFromReflect(idField,entry));
			item.setText(getValueFromReflect(nameFiled,entry));
			items.add(item);
		}
		dicts.put(dict_index, items);
		return dicts;
	}

	private String getValueFromReflect(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value==null?null:value.toString();
		} catch (Exception e) {
			logger.error("属性不存在:"+fieldName);
			return null;
		}
	}

	/** 异常处理 */
	@ExceptionHandler(Exception.class)
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
		logger.error(Constants.Exception_Head, ex);
		ModelMap modelMap = new ModelMap();
		if (ex instanceof BaseException) {
			((BaseException) ex).handler(modelMap);
		} else if (ex instanceof IllegalArgumentException) {
			new IllegalParameterException(ex.getMessage()).handler(modelMap);
		} else if (ex instanceof UnauthorizedException) {
			setModelMap(modelMap, HttpCode.FORBIDDEN);
		}  else if (ex instanceof NullPointerException) {
        	new IllegalParameterException("服务调用发生空指针异常").handler(modelMap);
		} else {
			//用回车键来分隔几个元素
			String msg = ex.getMessage();
	        String line = StringUtils.substringBefore(msg, "\n");
	        String classN = StringUtils.substringBefore(line, ":");
	        String text = StringUtils.substringAfter(line, ":");
	        if(classN.equals(IllegalParameterException.class.getCanonicalName())){
	        	new IllegalParameterException(text).handler(modelMap);
	        }else if(classN.equals(NullPointerException.class.getCanonicalName())){
	        	new IllegalParameterException("服务调用发生空指针异常").handler(modelMap);
	        }else{
	        	setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
	        }
		}
		request.setAttribute("msg", modelMap.get("msg"));
		response.setContentType("application/json;charset=UTF-8");
		byte[] bytes = JSON.toJSONBytes(modelMap, SerializerFeature.DisableCircularReferenceDetect);
		response.getOutputStream().write(bytes);
	}
}
