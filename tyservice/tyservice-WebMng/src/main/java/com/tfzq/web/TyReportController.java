package com.tfzq.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.MathUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysPositionroleBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysPositionroleService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyActivitystaffService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyReportService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyActivity;
import com.tfzq.ty.model.generator.TyCustListReport;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyReport;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.model.ty.TyActivitystaffBean;
import com.tfzq.ty.model.ty.TyStafflistBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 报表管理
 * @author lujun
 * @Date   2018年3月21日
 */
@RestController
@Api(value = "报表管理", description = "报表管理")
@RequestMapping(value = "ty/tyReport", method = RequestMethod.POST)
public class TyReportController extends BaseController{
	@Autowired 
	private SysUserService sysUserService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysPositionroleService sysPositionroleService;
	@Autowired
	private TyReportService tyReportService;
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyActivitystaffService tyActivitystaffService;
	
	private void transMarks(Map<String, Object> params) {
		//改为使用filterMarks，它是通过marks进行关系计算得到的标签集合
	    String marks = (String)params.get("filterMarks");
	    if(StringUtils.isNotBlank(marks)){
		  String[] split = marks.split(",");
		  params.remove("marks");
		  params.put("marks", Arrays.asList(split));
	    }else{
		  params.remove("marks");
	    }
	    params.remove("filterMarks");
	}

	
	/**
	 * 我的客户地域分布报表
	 */
	@ApiOperation(value = "我的客户地域分布报表")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custAreaDist")
	public Object custAreaDist(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  
	  transMarks(params);
	  
	  List<TyReport> tyReports = tyReportService.queryCustAreaDist(params);
	  
	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	/**
	 * 我的客户销售潜力分布报表
	 */
	@ApiOperation(value = "我的客户销售潜力分布报表")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custSaleDist")
	public Object custSaleDist(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  
	  transMarks(params);

	  List<TyReport> tyReports = tyReportService.queryCustSaleDist(params);
	  
	  Map<String, String> dicMap = sysDicService.queryDicByDicIndexKey("ORGLEVEL");
	  if(tyReports!=null && tyReports.size() > 0){
		  for (TyReport tyReport : tyReports) {
			  tyReport.setGroupName(dicMap.get(tyReport.getGroupName()));
		}
	  }

	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	/**
	 * 客户行业分布报表
	 */
	@ApiOperation(value = "客户行业分布报表")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custTradeDist")
	public Object custTradeDist(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  //
	  transMarks(params);

	  List<TyReport> tyReports = tyReportService.queryCustTradeDist(params);
	  
	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	
	/**
	 * 我的客户活跃趋势
	 */
	@ApiOperation(value = "我的客户活跃趋势")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custActiveTend")
	public Object custActiveTend(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  //
	  transMarks(params);

	  //查询满足条件的所有客户数量
	  Integer  totalCustNum = tyReportService.queryCustNum(params);
	  
	  //查询1个月内的满足条件的活跃用户数量
	  params.put("month", "-1");
	  Integer janActiveCustNum = tyReportService.queryCustNum(params);
	  
	  TyReport tyReport1 =new TyReport();
	  tyReport1.setGroupName("1个月内");
	  tyReport1.setActiveCustNum(janActiveCustNum);
	  tyReport1.setNoactiveCustNum(totalCustNum - janActiveCustNum);
	  
	  //查询3个月内满足条件的活跃用户数量
	  params.put("month", "-3");
	  Integer marActiveCustNum = tyReportService.queryCustNum(params);
	  TyReport tyReport2 =new TyReport();
	  tyReport2.setGroupName("3个月内");
	  tyReport2.setActiveCustNum(marActiveCustNum);
	  tyReport2.setNoactiveCustNum(totalCustNum - marActiveCustNum);
	  
	  //查询半年内满足条件的活跃用户数量
	  params.put("month", "-6");
	  Integer junActiveCustNum = tyReportService.queryCustNum(params);
	  TyReport tyReport3 =new TyReport();
	  tyReport3.setGroupName("半年内");
	  tyReport3.setActiveCustNum(junActiveCustNum);
	  tyReport3.setNoactiveCustNum(totalCustNum - junActiveCustNum);
	  
	  //查询一年内满足条件的活跃用户数量
	  params.put("month", "-12");
	  Integer decActiveCustNum = tyReportService.queryCustNum(params);
	  TyReport tyReport4 =new TyReport();
	  tyReport4.setGroupName("一年内");
	  tyReport4.setActiveCustNum(decActiveCustNum);
	  tyReport4.setNoactiveCustNum(totalCustNum - decActiveCustNum);
	  
	  List<TyReport> tyReports = new ArrayList<TyReport>();
	  tyReports.add(tyReport1);
	  tyReports.add(tyReport2);
	  tyReports.add(tyReport3);
	  tyReports.add(tyReport4);
	  
	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	
	/**
	 * 我的客户相关标签报表
	 */
	@ApiOperation(value = "我的客户相关标签报表")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custLabelDist")
	public Object custLabelDist(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  //
	  transMarks(params);

	  List<TyReport> tyReports = tyReportService.queryCustLabelDist(params);
	  
	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	/**
	 * 我的客户活动喜好分布
	 */
	@ApiOperation(value = "我的客户活动喜好分布")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/activityLike")
	public Object activityLike(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  //
	  transMarks(params);

	  List<TyReport> tyReports = tyReportService.queryActivityLike(params);
	  Map<String, String> dicMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
	  if(tyReports != null && tyReports.size() > 0){
		  for (TyReport tyReport : tyReports) {
			  tyReport.setGroupName(dicMap.get(tyReport.getGroupName()));
		}
	  }
	  
	  return setSuccessModelMap(modelMap, tyReports);
	}
	
	/**
	 * 客户画像搜索结果分页列表
	 */
	@ApiOperation(value = "客户画像搜索结果分页列表")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/custList")
	public Object custList(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  //String marks = (String)params.get("marks");
	  transMarks(params);

	  PageInfo<TyCustListReport> list = tyReportService.custList(params);
	  
	  //封装客户各类活动次数
	  List<TyCustListReport> tyCustListReports = list.getList();
	  if(tyCustListReports!=null && tyCustListReports.size() > 0){
		  for (TyCustListReport tyCustListReport : tyCustListReports) {
			  List<TyReport> tyRepotys  = tyReportService.queryCustActivityNum(tyCustListReport.getCustId());
			  if(tyRepotys!= null && tyRepotys.size() >0){
				  for (TyReport tyReport : tyRepotys) {
					  if("0".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum0(tyReport.getGroupNum());
						  continue;
					  }
					  if("1".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum1(tyReport.getGroupNum());
						  continue;
					  }
					  if("2".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum2(tyReport.getGroupNum());
						  continue;
					  }
					  if("3".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum3(tyReport.getGroupNum());
						  continue;
					  }
					  if("4".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum4(tyReport.getGroupNum());
						  continue;
					  }
					  if("5".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum5(tyReport.getGroupNum());
						  continue;
					  }
					  if("6".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum6(tyReport.getGroupNum());
						  continue;
					  }
					  if("7".equals(tyReport.getGroupName())){
						  tyCustListReport.setActivityNum7(tyReport.getGroupNum());
						  continue;
					  }
				}
			  }
		}
	  }
	  list.setList(tyCustListReports);
	  return setSuccessModelMap(modelMap, list);
	}
	
	
	/**
	 * 我的客户 - 主页
	 */
	@ApiOperation(value = "我的客户")
	@RequestMapping(value = "/read/myCust")
	public Object myCust(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	 
	  //查询我的客户总数
	  Integer myCustTotal = tyReportService.queryMyCustTotal(params);
	  //查询几月新增客户
	  Integer newAddCustNum = tyReportService.queryNewAddCustNum(params);
	  //查询几月参与活动的客户数
	  Integer joinActiCustNum = tyReportService.queryJoinActiCustNum(params);
	  
	  //计算客户参与活动率
	  Double joinActivityRate = MathUtil.divide(joinActiCustNum, myCustTotal, 2);
	  String joinActivityRateDesc = Double.toString(joinActivityRate*100)+"%";
	  
	  modelMap.put("newAddCustNum", newAddCustNum);
	  modelMap.put("myCustTotal", myCustTotal);
	  modelMap.put("joinActivityRateDesc", joinActivityRateDesc);
	  return setSuccessModelMap(modelMap);
	}
	
	/**
	 * 活动日程接口 - 主页
	 */
	@ApiOperation(value = "活动日程接口")
	@RequestMapping(value = "/read/activitySchedule")
	public Object activitySchedule(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  
	  params.put("delete_flag", "0");
	  List<TyActivityBean> list = tyActivityService.queryByCondition(params);
	  //添加字典翻译
	  Map<String, String> activitytypeMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
	  if(list != null && list.size() > 0 ){
		  for (TyActivityBean tyActivityBean : list) {
			  if (StringUtils.isNotEmpty(tyActivityBean.getActivityType())) {
					tyActivityBean.setActivityTypeText(activitytypeMap.get(tyActivityBean.getActivityType().toString()));
				}
		}
	  }
	  
	  return setSuccessModelMap(modelMap,list);
	}
	
	/**
	 * 活动分页列表接口 - 主页 , 客户详情 （可传客户id）
	 */
	@ApiOperation(value = "活动分页列表接口")
	@RequestMapping(value = "/read/activityList")
	public Object activityList(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "activityTypes[]", required = false) List<String> activityTypes) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  params.put("deleteFlag", "0");
	  params.put("activityTypes", activityTypes);
	  geneDataScope(params);
	  //查询（或该客户）所有活动统计数据(每类活动开展多少次)
	  List<TyReport> tyRepotys  = tyReportService.queryActivityNum(params);
	  
	 Map<String, String> dicMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
	 Map<String,Integer> dict =  new LinkedHashMap<>();
	 dict.put("全部", 0);
	 for(int i = 7 ; i >= 0 ; i--){
		 if(i == 3 || i == 4){  //过滤 晨会发言,语言微路演
			 continue;
		 }
		 dict.put(dicMap.get(i+""), 0);
	 }
	 Integer SumActivityNum = 0;
	 if(tyRepotys!=null && tyRepotys.size()>0){
		 for (TyReport tyReport : tyRepotys) {
			 if("3".equals(tyReport.getGroupName()) || "4".equals(tyReport.getGroupName())){  //过滤 晨会发言,语言微路演
				 continue;
			 }
			 //个人专项服务包含四个：晨会接入，电话服务，资料整理和其他专项服务
			 if("9".equals(tyReport.getGroupName()) || "10".equals(tyReport.getGroupName()) || 
					 "11".equals(tyReport.getGroupName()) || "12".equals(tyReport.getGroupName())) {
				 dict.put(Constants.ACTIVITY_PERSONAL_SERVICE, tyReport.getGroupNum() + 
						 (dict.get(Constants.ACTIVITY_PERSONAL_SERVICE) == null ? 0 : dict.get(Constants.ACTIVITY_PERSONAL_SERVICE)));
			 }else {
				 dict.put(dicMap.get(tyReport.getGroupName()), tyReport.getGroupNum());
			 }
			 SumActivityNum += tyReport.getGroupNum();
		}
	 }
	 dict.put("全部", SumActivityNum);
	 String  custId = (String) params.get("custId");
	 TyOrgcustomer cust = null;
	 if(StringUtils.isNotEmpty(custId)){
		 cust = tyOrgcustomerService.queryById(custId);
	 }
	 if(cust != null){ //传了客户id
		 params.put("custId", cust.getId());
	 }
	 //分页查询活动列表
	 params.put("orderBy", "begin_date desc");
	 params.put("exActivityType", "(3,4)");   //排除 晨会发言,语言微路演
	 PageInfo<TyActivityBean> list = tyActivityService.queryBeans(params);
	 PageInfo<Map<String,String>> result = new PageInfo<Map<String,String>>();
	 result.setPageNum(list.getPageNum());
	 result.setPageSize(list.getPageSize());
	 result.setSize(list.getSize());
	 result.setTotal(list.getTotal());
	 result.setPages(list.getPages());
	 List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
	 List<TyActivityBean> tyActivityBeans = list.getList();
	 
	 if(tyActivityBeans!=null && tyActivityBeans.size()>0){
		List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
		Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
		for (TyStafflist stf : allStaffs) {
			staffMap.put(stf.getId(), stf);
		}

		for (TyActivityBean tyActivityBean : tyActivityBeans) {
			 String beginDate = tyActivityBean.getBeginDate().toString();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			 String beginDateDesc = "";
			 try {
				Date parse = sdf.parse(beginDate);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
				beginDateDesc = sdf2.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("活动开始日期异常，异常活动id为："+tyActivityBean.getId());
				throw new IllegalParameterException("数据库日期数据异常，请联系管理员排查！");
			}
			
			String activityDesc = "";
			if(cust == null){   //表示主页查所有
				List<TyActivitysignBean> activitysignList = tyActivitysignService.queryByActId(tyActivityBean.getId());
				if(activitysignList != null){
					activityDesc += (beginDateDesc +": "+activitysignList.size()+"个客户参与了");
				}else{
					activityDesc += (beginDateDesc +": 0个客户参与了");
				}
				//查询该活动的 研究员（只取一个） 
				TyActivitystaffBean tyActivitystaffBean = null ;
				List<TyActivitystaffBean> stffs = tyActivitystaffService.queryByActId(tyActivityBean.getId());
				if(stffs != null && stffs.size() > 0 ){
					tyActivitystaffBean = stffs.get(0);
				}
				if(tyActivitystaffBean != null) {
					//通过研究员查找行业
					TyStafflist tyStafflist = staffMap.get(tyActivitystaffBean.getStaffId());//tyStafflistService.queryById(tyActivitystaffBean.getId());
					if(tyStafflist != null && tyStafflist.getDeptName() != null){
						activityDesc += (tyStafflist.getDeptName()+"行业下");
					}
					if(tyStafflist != null && tyStafflist.getStaffName() != null){
						activityDesc += (tyStafflist.getStaffName()+"组织的");
					}
				}
				activityDesc += dicMap.get(tyActivityBean.getActivityType());
				if(tyActivityBean.getTitle() != null){
					activityDesc += (":"+tyActivityBean.getTitle());
				}
			}else{   //表示查指定客户
				if(cust.getCustName() != null){
					activityDesc += (beginDateDesc +": "+cust.getCustName()+"参与了");
				}
				//查询该活动的 研究员（只取一个） 
				TyActivitystaffBean tyActivitystaffBean = null ;
				List<TyActivitystaffBean> stffs = tyActivitystaffService.queryByActId(tyActivityBean.getId());
				if(stffs != null && stffs.size() > 0 ){
					tyActivitystaffBean = stffs.get(0);
				}
				if(tyActivitystaffBean != null){
					//通过研究员查找行业
					TyStafflist tyStafflist = staffMap.get(tyActivitystaffBean.getStaffId());//tyStafflistService.queryById(tyActivitystaffBean.getId());
					if(tyStafflist != null && tyStafflist.getDeptName() != null){
						activityDesc += (tyStafflist.getDeptName()+"行业下");
					}
					if(tyStafflist != null && tyStafflist.getStaffName() != null){
						activityDesc += (tyStafflist.getStaffName()+"组织的");
					}
				}
				activityDesc += dicMap.get(tyActivityBean.getActivityType());
				if(tyActivityBean.getTitle() != null){
					activityDesc += (":"+tyActivityBean.getTitle());
				}
			}
			Map<String,String> map1 = new HashMap<String,String>();
			String activityType = dicMap.get(tyActivityBean.getActivityType());
			map1.put(activityType+","+tyActivityBean.getId(), activityDesc);
			resultList.add(map1);
		}
	  }
	  result.setList(resultList);
	  return setSuccessModelMap(modelMap,result,dict);
	}
	
	
	/**
	 * 客户维护活动详情
	 */
	@ApiOperation(value = "客户未读活动分页列表接口")
	@RequestMapping(value = "/read/customerActivityList")
	public Object customerActivityList(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "activityTypes[]", required = false) List<String> activityTypes) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  params.put("deleteFlag", "0");
	  params.put("activityTypes", activityTypes);
	  geneDataScope(params);
	  //查询（或该客户）所有活动统计数据(每类活动开展多少次)
	  List<TyReport> tyRepotys  = tyReportService.queryActivityNum(params);
	  
	 Map<String, String> dicMap = sysDicService.queryDicByDicIndexKey("ACTIVITYTYPE");
	 Map<String,Integer> dict =  new LinkedHashMap<>();
	 dict.put("全部", 0);
	 for(int i = 7 ; i >= 0 ; i--){
		 if(i == 3 || i == 4){  //过滤 晨会发言,语言微路演
			 continue;
		 }
		 dict.put(dicMap.get(i+""), 0);
	 }
	 Integer SumActivityNum = 0;
	 if(tyRepotys!=null && tyRepotys.size()>0){
		 for (TyReport tyReport : tyRepotys) {
			 if("3".equals(tyReport.getGroupName()) || "4".equals(tyReport.getGroupName())){  //过滤 晨会发言,语言微路演
				 continue;
			 }
			 //个人专项服务包含四个：晨会接入，电话服务，资料整理和其他专项服务
			 if("9".equals(tyReport.getGroupName()) || "10".equals(tyReport.getGroupName()) || 
					 "11".equals(tyReport.getGroupName()) || "12".equals(tyReport.getGroupName())) {
				 dict.put(Constants.ACTIVITY_PERSONAL_SERVICE, tyReport.getGroupNum() + 
						 (dict.get(Constants.ACTIVITY_PERSONAL_SERVICE) == null ? 0 : dict.get(Constants.ACTIVITY_PERSONAL_SERVICE)));
			 }else {
				 dict.put(dicMap.get(tyReport.getGroupName()), tyReport.getGroupNum());
			 }
			 SumActivityNum += tyReport.getGroupNum();
		}
	 }
	 dict.put("全部", SumActivityNum);
	 //分页查询活动列表
	 params.put("orderBy", "sign_date desc");
	 params.put("exActivityType", "(3,4)");   //排除 晨会发言,语言微路演
	 PageInfo<TyActivitysignBean> list = tyActivitysignService.queryBeans(params);
	 PageInfo<Map<String,String>> result = new PageInfo<Map<String,String>>();
	 result.setPageNum(list.getPageNum());
	 result.setPageSize(list.getPageSize());
	 result.setSize(list.getSize());
	 result.setTotal(list.getTotal());
	 result.setPages(list.getPages());
	 List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
	 List<TyActivitysignBean> TyActivitysignBeans = list.getList();
	 
	 if(TyActivitysignBeans!=null && TyActivitysignBeans.size()>0){
		List<TyStafflist> allStaffs = tyStafflistService.getAllRecords();
		Map<String, TyStafflist> staffMap = new HashMap<String, TyStafflist>();
		for (TyStafflist stf : allStaffs) {
			staffMap.put(stf.getId(), stf);
		}

		for (TyActivitysignBean tyActivitySignBean : TyActivitysignBeans) {
			TyActivity tyactivity = tyActivityService.queryById(tyActivitySignBean.getActivityId());
			 String beginDate = tyActivitySignBean.getSignDate().toString();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			 String beginDateDesc = "";
			 try {
				Date parse = sdf.parse(beginDate);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
				beginDateDesc = sdf2.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("活动开始日期异常，异常活动id为："+tyActivitySignBean.getActivityId());
				throw new IllegalParameterException("数据库日期数据异常，请联系管理员排查！");
			}
			
			String activityDesc = "";
			if(StringUtils.isNotBlank(tyActivitySignBean.getCustName())){   
					activityDesc += (beginDateDesc +": "+tyActivitySignBean.getCustName()+"参与了");
				//查询该活动的 研究员（只取一个） 
				TyActivitystaffBean tyActivitystaffBean = null ;
				List<TyActivitystaffBean> stffs = tyActivitystaffService.queryByActId(tyActivitySignBean.getActivityId());
				if(stffs != null && stffs.size() > 0 ){
					tyActivitystaffBean = stffs.get(0);
				}
				if(tyActivitystaffBean != null) {
					//通过研究员查找行业
					TyStafflist tyStafflist = staffMap.get(tyActivitystaffBean.getStaffId());//tyStafflistService.queryById(tyActivitystaffBean.getId());
					if(tyStafflist != null && tyStafflist.getDeptName() != null){
						activityDesc += (tyStafflist.getDeptName()+"行业下");
					}
					if(tyStafflist != null && tyStafflist.getStaffName() != null){
						activityDesc += (tyStafflist.getStaffName()+"组织的");
					}
				}
				activityDesc += dicMap.get(tyactivity.getActivityType());
				if(tyactivity.getTitle() != null){
					activityDesc += (":"+tyactivity.getTitle());
				}
			}
			Map<String,String> map1 = new HashMap<String,String>();
			String activityType = dicMap.get(tyactivity.getActivityType());
			map1.put(activityType+","+tyactivity.getId(), activityDesc);
			resultList.add(map1);
		}
	  }
	  result.setList(resultList);
	  return setSuccessModelMap(modelMap,result,dict);
	}
	
	/**
	 *  查询活动与会人员 - 主页
	 */
	@ApiOperation(value = "查询活动与会人员")
	@RequestMapping(value = "/read/queryActivityCust")
	public Object queryActivityCust(ModelMap modelMap,@RequestParam(value = "id", required = false) String id) {
	  
		List<String> custs = new ArrayList<>() ; 
		List<TyOrgcustomer> list = new ArrayList<>();
		if (StringUtils.isNotEmpty(id)) {
			List<TyActivitysignBean> activitysignList = tyActivitysignService.queryByActId(id);
			if(activitysignList!=null && activitysignList.size()>0){
				for (TyActivitysignBean tyActivitysignBean : activitysignList) {
					custs.add(tyActivitysignBean.getCustId());
				}
			}
		}
		if(custs != null && custs.size() > 0 ){
			for (String custId : custs) {
				TyOrgcustomer tyOrgcustomer = tyOrgcustomerService.queryById(custId);
				list.add(tyOrgcustomer);
			}
		}
	  
	  return setSuccessModelMap(modelMap,list);
	}
	
	/**
	 * 机构白名单到期个数
	 */
	@ApiOperation(value = "机构白名单到期个数")
	//@RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/getDeadlineOrgs")
	public Object getDeadlineOrgs(HttpServletRequest request, ModelMap modelMap) {
	  Map<String, Object> params = WebUtil.getParameterMap(request);
	  // 添加查询范围条件
	  geneDataScope(params);
	  
	  //设置白名单到期时间
	  geneWhiteDeadLine(params);
	  
	  Integer custNum = tyReportService.queryDeadlineOrgs(params);
	  
	  return setSuccessModelMap(modelMap, custNum);
	}
	
	
	
	
	
	
	
	/**
	 * 根据不同的角色设置不同的客户数据查询范围
	 * 
	 * @param params
	 */
	private void geneDataScope(Map<String, Object> params) {
		SysUser currUser = sysUserService.queryById(WebUtil.getCurrentUser());
		// 已有角色
		List<SysUserRoleBean> userrole = sysRoleService.queryRoleByUserId(currUser.getId());
		if (tyStafflistService.isSeniorseller(userrole)) {// 如果是高级销售
			// 1 找出所有销售职位
			List<String> roleList = new ArrayList<String>();
			roleList.add("seniorseller");
			roleList.add("seller");
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("roleList", roleList);
			params2.put("pageSize", 1000);
			PageInfo<SysPositionroleBean> spr = sysPositionroleService.queryBeans(params2);
			List<String> positionList = new ArrayList<String>();
			for (Iterator<SysPositionroleBean> iterator = spr.getList().iterator(); iterator.hasNext();) {
				SysPositionroleBean sprbean = iterator.next();
				positionList.add(sprbean.getPositionId());
			}
			// 2 找出所有销售职位对应的销售人员
			Map<String, Object> params3 = new HashMap<String, Object>();
			params3.put("positionList", positionList);
			TyStafflistBean selfStaff = tyStafflistService.queryByUserId(currUser.getAccount());
			params3.put("workAreaid", selfStaff.getWorkAreaid());

			List<TyStafflistBean> stafflist = tyStafflistService.queryByCondition(params3);
			List<String> salerList = new ArrayList<String>();
			for (Iterator<TyStafflistBean> iterator = stafflist.iterator(); iterator.hasNext();) {
				TyStafflistBean staffBean = iterator.next();
				salerList.add(staffBean.getId());
			}
			params.put("salerList", salerList); // 设置条件
		} else if (tyStafflistService.isSeller(userrole)) {// 如果是一般销售
			TyStafflistBean selfStaff = tyStafflistService.queryByUserId(currUser.getAccount());
			params.put("salerId", selfStaff.getId()); // 设置条件
		}
	}
	
	
	
	
	//设置结构白名单到期时间查询条件
	private void geneWhiteDeadLine(Map<String, Object> params) {
		if(params.get("whiteDeadline") != null) {
			Date now = new Date();
			switch(params.get("whiteDeadline").toString()) {
			
				case Constants.SERVICE_ORG_WHITELINE_ONE_WEEK : 
					Date sevenDayLater = DateUtil.addDate(now, Calendar.DATE, 7);
					params.put("whiteDeadlineEnd", Integer.parseInt(DateUtil.format(sevenDayLater, DateUtil.DATE_PATTERN.YYYYMMDD)));
					break;
					
				case Constants.SERVICE_ORG_WHITELINE_HALF_MONTH : 
					Date halfMonth = DateUtil.addDate(now, Calendar.DATE, 15);
					params.put("whiteDeadlineEnd", Integer.parseInt(DateUtil.format(halfMonth, DateUtil.DATE_PATTERN.YYYYMMDD)));
					break;
					
				case Constants.SERVICE_ORG_WHITELINE_OTHER : 
					System.out.println("机构白名单截止日期查询条件不正确");
					break;
			}
		}
	}
}
	
