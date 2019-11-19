package com.tfzq.web;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysArea;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyListedcompanyService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyListedcompany;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyListedcompanyBean;
import com.tfzq.ty.model.ty.TyServiceorgBean;
import com.tfzq.util.HtmlUtils;
import com.tfzq.util.excel.WriteExcelUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 上市公司控制类
 * 
 * @author pengtao 
 */
@RestController
@Api(value = "上市公司管理", description = "上市公司管理")
@RequestMapping(value = "ty/tyListedcompany", method = RequestMethod.POST)
public class TyListedcompanyController extends BaseController {
	@Autowired
	private TyListedcompanyService tyListedcompanyService;
	@Autowired
	private  SysRoleService sysRoleService;
	@Autowired
	private  TyStafflistService tyStafflistService;
	// 查询上市公司
	@ApiOperation(value = "查询上市公司")
	//@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","stock_code asc");
		PageInfo<TyListedcompanyBean> list = tyListedcompanyService.queryBeans(params);
		
		//添加字典翻译
		return setSuccessModelMap(modelMap, list);
	}

	// 详细信息
	@ApiOperation(value = "上市公司详情")
	//@RequiresPermissions("ty.tyListedcompany.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyListedcompanyBean tyListedcompanyBean = new TyListedcompanyBean();
		Map<String,List<DictItem>> dicts = null;
		if(StringUtils.isNotEmpty(id)){
			TyListedcompany record = tyListedcompanyService.queryById(id);
			try {
	            BeanUtils.copyProperties(tyListedcompanyBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				tyListedcompanyBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
	    if(tyListedcompanyBean!=null){
	    }
	    
		return setSuccessModelMap(modelMap, tyListedcompanyBean,dicts);
	}

	
	// 新增上市公司
	@ApiOperation(value = "添加上市公司")
	//@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyListedcompany record = Request2ModelUtil.covert(TyListedcompany.class, request);
		record.setDataSrc("1");
		tyListedcompanyService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改上市公司
	@ApiOperation(value = "修改上市公司")
	//@RequiresPermissions("ty.tyListedcompany.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyListedcompany record = Request2ModelUtil.covert(TyListedcompany.class, request);
		tyListedcompanyService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除上市公司
	@ApiOperation(value = "删除上市公司")
	//@RequiresPermissions("ty.tyListedcompany.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyListedcompany record = tyListedcompanyService.queryById(id);
		if(record!=null){
			tyListedcompanyService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	
	// 批量导入上市公式
	@ApiOperation(value = "批量导入上市公司")
	//@RequiresPermissions("ty.tyListedcompany.add")
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public Object batchImport(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		List<String> list = new ArrayList<String>();
		int successNum=0;
			String fileName = multipartFile.getOriginalFilename();
			if (StringUtils.isBlank(fileName) || !isExcel(fileName)) {
				throw new IllegalParameterException("无法识别该文件");
			}
			String rootFilePath = HtmlUtils.getExcelTmpFilePath();
			File rootFile = new File(rootFilePath);
			if (!rootFile.isDirectory()) {
				rootFile.mkdirs();
			}
			String filePath = rootFilePath + File.separator + fileName;
			File file = new File(filePath);
			file.setWritable(true, false);
			try {
				multipartFile.transferTo(file);
			} catch (Exception e) {
				logger.error(fileName + "保存失败", e);
				throw new BusinessException("文件保存失败");
			}
			successNum = importListedCompExcel(file, list);

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("errorLogList", list);
		m.put("successNum", successNum);
		if(successNum == 0){
			return setModelMap(modelMap, HttpCode.BAD_REQUEST,m);
		}
		return setSuccessModelMap(modelMap, m);
	}
	
	
	
	private int importListedCompExcel(File excel, List<String> list) {
		String userId = WebUtil.getCurrentUser();
		// List<PbProdsharejour> list = new ArrayList<PbProdsharejour>();
		// 读取文件内容
		int successNum = 0;
		Workbook hssfWorkbook = null;
		int rowNum = 1;
		int currColNum = 0;
		boolean isListedCompExists = false;
		boolean readColFinished = false;
		try {
			hssfWorkbook = WriteExcelUtil.getWorkbok(excel.getAbsolutePath());// new
																				// Workbook(is);
			// 循环工作表Sheet（获取Excel中第一个Sheet）
			Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 循环行Row，从第2行开始
			List<TyListedcompany> allComps = tyListedcompanyService.getAllRecords();
			Map<String, TyListedcompany> compMap = new HashMap<String, TyListedcompany>();
			for (TyListedcompany comp : allComps) {
				compMap.put(comp.getStockName(), comp);
			}
			for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					try {
						isListedCompExists = false;
						readColFinished = false;
						currColNum = 0;
						String stock_simple_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 1;
						String stock_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						if (StringUtils.isNotBlank(stock_name)) {
							TyListedcompany company = compMap.get(stock_name);
							if (company != null) {
								isListedCompExists = true;
							}
						} else {
							continue;
						}

						currColNum = 2;
						String market_kind = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						currColNum = 3;
//						String cust_cat_str = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						String stock_code = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						String cust_cat = transCustCat(cust_cat_str);
						currColNum = 4;
						String company_short_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						String cust_status = transCustStatus(cust_status_str);
						currColNum = 5;
						String market_no = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
//						String org_level = transOrgLevel(org_level_str);
						currColNum = 6;
						String market_name = WriteExcelUtil.trimCellStringToNull(hssfRow, currColNum);
						
						readColFinished = true;
						if (isListedCompExists) {
							TyListedcompany record = new TyListedcompany();
							TyListedcompany oldrecord = compMap.get(stock_name);
							BeanUtils.copyProperties(record, oldrecord);
							
							if (stock_simple_name != null)
								record.setStockShortname(stock_simple_name);
							if (stock_name != null)
								record.setStockName(stock_name);
							if (market_kind != null)
								record.setMarketKind(market_kind);
							if (stock_code != null)
								record.setStockCode(stock_code);
							if (company_short_name != null)
								record.setCompanyShortname(company_short_name);
							if (market_no != null)
								record.setMarketNo(market_no);
							if (market_name != null)
								record.setMarketName(market_name);
							tyListedcompanyService.update(record);
//							updateCustomer(oldrecord, record);
						} else {
							TyListedcompany record = new TyListedcompany();
							record.setStockShortname(stock_simple_name);
							record.setStockName(stock_name);
							record.setMarketKind(market_kind);
							record.setStockCode(stock_code);
							record.setCompanyShortname(company_short_name);
							record.setMarketNo(market_no);
							record.setMarketName(market_name);
							record.setDeleteFlag("0");
							tyListedcompanyService.add(record);
						}
						successNum++;
					} catch (IllegalParameterException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (BusinessException e) {
						e.printStackTrace();
						list.add(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
						if(readColFinished){
							list.add("Excel文件单元格第" + (rowNum + 1) + "行数据处理异常;");
						}else{
							list.add("Excel文件单元格第" + (rowNum + 1) + "行,第" + (currColNum + 1) + "列解析异常;");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalParameterException("解析Excel文件内容失败");
		} finally {
			try {
				hssfWorkbook.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalParameterException("解析Excel文件内容失败");
			}
		}
		list.add(0, "成功"+(isListedCompExists?"更新":"新增" )+ successNum + "条数据;");
		return successNum;
	}
	
	/**
	 * 是否是Excel文件
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isExcel(String fileName) {
		if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
			return true;
		} else {
			return false;
		}
	}
}
