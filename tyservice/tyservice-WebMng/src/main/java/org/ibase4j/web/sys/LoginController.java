package org.ibase4j.web.sys;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.config.Resources;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.exception.LoginException;
import org.ibase4j.core.support.Assert;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.support.login.LoginHelper;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysParam;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysUserBean;
import org.ibase4j.model.sys.SysUserRoleBean;
import org.ibase4j.service.sys.SysDeptService;
import org.ibase4j.service.sys.SysParamService;
import org.ibase4j.service.sys.SysRoleService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyActivityService;
import com.tfzq.service.TyActivitysignService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.service.TyStafflistService;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyActivityBean;
import com.tfzq.ty.model.ty.TyActivitysignBean;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.util.WxUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户登录
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:11:21
 */
@RestController
@Api(value = "登录接口", description = "登录接口")
public class LoginController extends BaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private TyActivityService tyActivityService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyActivitysignService tyActivitysignService;
	@Autowired
	private SysRoleService sysRoleService;

	// 登录
	@ApiOperation(value = "用户登录")
	@PostMapping("/login")
	public Object login(ModelMap modelMap,
			@ApiParam(required = true, value = "登录帐号") @RequestParam(value = "account", required = false) String account,
			@ApiParam(required = true, value = "登录密码") @RequestParam(value = "password", required = false) String password,
			@ApiParam(required = false, value = "AD登录") @RequestParam(value = "adlogin", required = false) String adlogin) {
		Assert.notNull(account, "ACCOUNT");
		Assert.notNull(password, "PASSWORD");
//		if(account.equals("superadmin")){
//			return "success";
//		}
		boolean ad = "true".equals(adlogin)?true:false;
		String pwd = "";
		if(ad){
			pwd=password;
		}else{
			pwd=sysUserService.encryptPassword(password);
		}
		if (LoginHelper.login(account, pwd,ad)) {
			return setSuccessModelMap(modelMap);
		}
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}
	
	// 数据平台登录
	@ApiOperation(value = "数据平台用户登录")
	@GetMapping("/dpauth")
	public void dpauth(ModelMap modelMap,HttpServletResponse response,HttpServletRequest request,
			@ApiParam(required = true, value = "令牌") @RequestParam(value = "dptoken", required = false) String dptoken,
			@ApiParam(required = true, value = "跳转路径") @RequestParam(value = "path", required = false) String path ) {
		if(StringUtils.isBlank(dptoken) || StringUtils.isBlank(path)){
			throw new IllegalParameterException(Resources.getMessage("LOGIN_FAIL"));
		}
		String reDedirectUrl ="";
		if(StringUtils.isNotBlank(WebUtil.getCurrentUser())){
			reDedirectUrl = PropertiesUtil.getString("dp.url.redirect")+path+"?from=dp";
//			reDedirectUrl = PropertiesUtil.getString("dp.url.redirect")+path;
			try {
				response.sendRedirect(reDedirectUrl);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}else{
			if (LoginHelper.login(dptoken)) {
				reDedirectUrl = PropertiesUtil.getString("dp.url.redirect")+path+"?from=dp";
//				reDedirectUrl = PropertiesUtil.getString("dp.url.redirect")+path;
				try {
					response.sendRedirect(reDedirectUrl);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}else{
				throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
			}
		}
	}
	private PageInfo<SysUser> queryUserByWxId(String wxId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("countSql", 0);
		params.put("enable", 1);
		params.put("wxId", wxId);
		PageInfo<SysUser> pageInfo = sysUserService.query(params);
		return pageInfo;
	}

	// 微信用户登录
	@ApiOperation(value = "微信用户登录")
	@PostMapping("/wxlog")
	public Object wxlog(ModelMap modelMap,
			@ApiParam(required = false, value = "登录帐号") @RequestParam(value = "account", required = false) String account,
			@ApiParam(required = false, value = "登录密码") @RequestParam(value = "password", required = false) String password,
			@ApiParam(required = false, value = "AD登录") @RequestParam(value = "adlogin", required = false) String adlogin,
			@ApiParam(required = true, value = "微信code") @RequestParam(value = "wxCode", required = false) String wxCode,
			@ApiParam(required = false, value = "微信头像") @RequestParam(value = "avatar", required = false) String avatar) {
		//Assert.notNull(account, "ACCOUNT");
		//Assert.notNull(password, "PASSWORD");
		Assert.notNull(wxCode, "WXCODE");
		Map<String,Object> data = new HashMap<>();
		//根据code获取openId
		SysParam appidBean = sysParamService.queryById("ty.activity.weixin.appid");
		SysParam secrectcodeBean = sysParamService.queryById("ty.activity.weixin.appsecret");
		try {
			boolean ad = "true".equals(adlogin)?true:false;
			String encryptPassword = password;
			if(!ad){
				encryptPassword = sysUserService.encryptPassword(password);
			}
			JSONObject wxInfo = WxUtils.getWxprogramUserInfo(wxCode, appidBean.getParamValue(),secrectcodeBean.getParamValue());
			String wxId = (String) wxInfo.get("openid");
			//通过微信号查询账号及密码，并传递给Realm做校验
			boolean existWx = false;
			if(StringUtils.isNotBlank(wxId)&&StringUtils.isBlank(account)&&StringUtils.isBlank(password)){				
				PageInfo<SysUser> pageInfo = queryUserByWxId(wxId);
				if(pageInfo.getSize()>=1){
					//找到该微信绑定的账号
					existWx=true;
					account = pageInfo.getList().get(0).getAccount();
					encryptPassword = pageInfo.getList().get(0).getPassword();
				}else{
					throw new LoginException("该微信没有绑定任何账号。");
				}
			}
			if(LoginHelper.login(account,encryptPassword,wxId,ad,avatar,existWx)){
				String userId = WebUtil.getCurrentUser();
				TyStafflist staff = tyStafflistService.queryById(userId);
				if(staff == null){
					SysUser user = sysUserService.queryById(userId);
					SysUserBean userBean = new SysUserBean();
					BeanUtils.copyProperties(userBean, user);
					SysDept sysDept = sysDeptService.queryById(user.getDeptId());
					userBean.setDeptName(sysDept == null ? "" : sysDept.getDeptName());
					userBean.setAccount("");
					userBean.setPassword("");
					data.put("user", userBean);
					return setSuccessModelMap(modelMap,data);
				}
				//统计当前用户的所有活动个数和活跃客户
//				Map<String,Object>params = new HashMap<>();
//				params.put("createById", userId);
//				List<TyActivityBean> myActivityList = 
				int myActivityList = tyActivityService.queryMyActCount(userId);//.queryByCondition(params);
				//统计当前用户（研究员或者销售）的活跃客户
//				Map<String, Object> params1 = new HashMap<String, Object>();
//				params1.put("deleteFlag", "0");
//				params1.put("salerId", userId);
//				params1.put("activeDatetime", Integer.parseInt(DateUtil.format(DateUtil.addDate(new Date(), Calendar.DATE, -100), DateUtil.DATE_PATTERN.YYYYMMDD)));
//				List<TyOrgcustomerBean> list = tyOrgcustomerService.queryByCondition(params1);
				int activateCustomers = tyOrgcustomerService.queryMyActiveCustCount(userId);
//				if (list.size() > 0) {
//					for (TyOrgcustomerBean singleBean : list) {
//						if (StringUtils.isNotEmpty(singleBean.getCustMobile())) {
//							Map<String, Object> param = new HashMap<String, Object>();
//							param.put("custTel", singleBean.getCustMobile());
//							List<TyActivitysignBean> tylist = tyActivitysignService.queryByCondition(param);
//							singleBean.setSignNum(tylist.size());
//							
//						}
//
//					}
//				}
				//统计我参加的活动
//				Map<String,Object> params2  = new HashMap<>();
//				params2.put("custById", userId);
//				List<TyActivitysignBean> list2 = tyActivitysignService.queryByCondition(params2);
				//判断当前用户是否是销售或者研究员
				List<SysUserRoleBean> queryRoleByUserId = sysRoleService.queryRoleByUserId(WebUtil.getCurrentUser());
				boolean isSeller = false;
				isSeller |= tyStafflistService.isSeniorseller(queryRoleByUserId);  //高级销售
				if(!isSeller){
					isSeller  |= tyStafflistService.isSeller(queryRoleByUserId);
				}
				
				data.put("isSeller", isSeller);
				data.put("mySignActList", 0);
				data.put("user", staff);
				data.put("activateCustomers", activateCustomers);
				data.put("myActivityList", myActivityList);
				return setSuccessModelMap(modelMap,data);
			}
		} catch (IOException e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
		} catch (IllegalAccessException e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
		} catch (InvocationTargetException e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
		} 
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}
	

	// 登出
	@ApiOperation(value = "用户登出")
	@PostMapping("/logout")
	public Object logout(ModelMap modelMap) {
		SecurityUtils.getSubject().logout();
		return setSuccessModelMap(modelMap);
	}

	// 注册
	@ApiOperation(value = "用户注册")
	@PostMapping("/regin")
	public Object regin(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "password", required = false) String password) {
		SysUser sysUser = Request2ModelUtil.covert(SysUser.class, request);
		Assert.notNull(sysUser.getAccount(), "ACCOUNT");
		Assert.notNull(sysUser.getPassword(), "PASSWORD");
		sysUser.setPassword(sysUserService.encryptPassword(sysUser.getPassword()));
		sysUserService.add(sysUser);
		if (LoginHelper.login(account, password,false)) {
			return setSuccessModelMap(modelMap);
		}
		throw new IllegalArgumentException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 没有登录
	@ApiOperation(value = "没有登录")
	@RequestMapping("/unauthorized")
	public Object unauthorized(ModelMap modelMap) {
		SecurityUtils.getSubject().logout();
		return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
	}

	// 没有权限
	@ApiOperation(value = "没有权限")
	@RequestMapping("/forbidden")
	public Object forbidden(ModelMap modelMap) {
		return setModelMap(modelMap, HttpCode.FORBIDDEN);
	}
}