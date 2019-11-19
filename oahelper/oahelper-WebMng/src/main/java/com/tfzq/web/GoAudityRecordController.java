package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.tfzq.pr.model.generator.GoAudityRecord;
import com.tfzq.pr.model.generator.MyInform;
import com.tfzq.pr.model.pr.GoAudityRecordBean;
import com.tfzq.service.GoAudityRecordService;
import com.tfzq.util.PbFileUtils;

/**
 * 信息审批记录控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "信息审批记录管理", description = "信息审批记录管理")
@RequestMapping(value = "pr/goAudityRecord", method = RequestMethod.POST)
public class GoAudityRecordController extends BaseController {
	@Autowired
	private GoAudityRecordService goAudityRecordService;
	@Autowired
	private SysDicService sysDicService;

	// 查询信息审批记录
	@ApiOperation(value = "查询信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoAudityRecordBean> list = goAudityRecordService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISPASSED");
		addDictFromMap(dicts, "ISPASSED", yes_noMap);
		for (GoAudityRecordBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getIsPassed())) {
				singleBean.setIsPassedText(yes_noMap.get(singleBean.getIsPassed().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 查询信息审批记录
	@ApiOperation(value = "查询信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByCondition(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		List<GoAudityRecordBean> list = goAudityRecordService.queryByCondition(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISPASSED");
		addDictFromMap(dicts, "ISPASSED", yes_noMap);
		for (GoAudityRecordBean singleBean : list) {
			if (StringUtils.isNotEmpty(singleBean.getIsPassed())) {
				singleBean.setIsPassedText(yes_noMap.get(singleBean.getIsPassed().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 查询信息审批记录
	@ApiOperation(value = "导出信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.read")
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public Object export(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "audityId,id ");
		List<GoAudityRecordBean> list = goAudityRecordService.queryByCondition(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISPASSED");
		//addDictFromMap(dicts, "ISPASSED", yes_noMap);

		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("审核记录");
			HSSFRow row = sheet.createRow((short) (0));
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("处理人");
			cell = row.createCell(1);
			cell.setCellValue("操作");
			cell = row.createCell(2);
			cell.setCellValue("标题");
			cell = row.createCell(3);
			cell.setCellValue("审批内容/意见");
			cell = row.createCell(4);
			cell.setCellValue("附件");
			cell = row.createCell(5);
			cell.setCellValue("处理时间");

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				GoAudityRecordBean audityRecord = list.get(i);
				row = sheet.createRow((short) (i + 1));
				cell = row.createCell(0);
				cell.setCellValue(audityRecord.getUserName());
				cell = row.createCell(1);
				if (StringUtils.isNotEmpty(audityRecord.getIsPassed())) {
					audityRecord.setIsPassedText(yes_noMap.get(audityRecord.getIsPassed().toString()));
				}
				cell.setCellValue(audityRecord.getIsPassedText());
				cell = row.createCell(2);
				cell.setCellValue(audityRecord.getAudityTitle());
				cell = row.createCell(3);
				cell.setCellValue((StringUtils.isEmpty(audityRecord.getAudityComment())||StringUtils.equals(audityRecord.getAudityComment(),"null"))?"":audityRecord.getAudityComment());
				cell = row.createCell(4);
				StringBuffer urls = new StringBuffer();
				if(StringUtils.isNotEmpty(audityRecord.getAudityPicture())){
					String[] files = audityRecord.getAudityPicture().split(",");
					for(String f:files){
						urls.append("https://tytool.tfzq.com");
						if(!f.startsWith("/")){
							urls.append("/");
						}
						urls.append(f);
					}
				}
				cell.setCellValue(urls.toString());
				cell = row.createCell(5);
				cell.setCellValue(df.format(audityRecord.getCreateTime()));

			}
			// response.setContentType("application/octet-stream");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=audity.xls");

			OutputStream os = response.getOutputStream();

			wb.write(os);
			wb.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	// 详细信息
	@ApiOperation(value = "信息审批记录详情")
	// @RequiresPermissions("pr.goAudityRecord.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoAudityRecordBean goAudityRecordBean = new GoAudityRecordBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoAudityRecord record = goAudityRecordService.queryById(id);
			try {
				BeanUtils.copyProperties(goAudityRecordBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goAudityRecordBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISPASSED");
		addDictFromMap(dicts, "ISPASSED", yes_noMap);
		if (goAudityRecordBean != null) {
			if (StringUtils.isNotEmpty(goAudityRecordBean.getIsPassed())) {
				goAudityRecordBean.setIsPassedText(yes_noMap.get(goAudityRecordBean.getIsPassed().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goAudityRecordBean, dicts);
	}

	// 新增信息审批记录
	@ApiOperation(value = "添加信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoAudityRecord record = Request2ModelUtil.covert(GoAudityRecord.class, request);
		goAudityRecordService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改信息审批记录
	@ApiOperation(value = "修改信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoAudityRecord record = Request2ModelUtil.covert(GoAudityRecord.class, request);
		goAudityRecordService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除信息审批记录
	@ApiOperation(value = "删除信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoAudityRecord record = goAudityRecordService.queryById(id);
		if (record != null) {
			goAudityRecordService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
