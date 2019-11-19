package org.ibase4j.web.sys;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.ibase4j.core.Constants;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.config.Resources;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.exception.LoginException;
import org.ibase4j.core.support.Assert;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.support.login.LoginHelper;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDept;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysUserBean;
import org.ibase4j.service.sys.SysUserService;
import org.ibase4j.service.sys.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Producer;
import com.tfzq.service.SmsService;

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
	private SysDeptService sysDeptService;

	private Producer captchaProducer = null;
	@Autowired
	private SmsService smsService;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	@RequestMapping("/bizspace/captcha-image")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();
		// store the text in the session
		SecurityUtils.getSubject().getSession().setAttribute(org.ibase4j.core.Constants.KEY_CAPTCHA, capText);
		
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}

	// 登录
	@ApiOperation(value = "用户登录")
	@PostMapping("/bizspace/login")
	public Object login(
			ModelMap modelMap,
			@ApiParam(required = true, value = "登录帐号") @RequestParam(value = "account", required = false) String account,
			@ApiParam(required = true, value = "登录密码") @RequestParam(value = "password", required = false) String password,
			@ApiParam(required = true, value = "登录验证码") @RequestParam(value = "captcha", required = false) String captcha) {
		Assert.notNull(account, "ACCOUNT");
		Assert.notNull(password, "PASSWORD");

		if (LoginHelper
				.login(account, sysUserService.encryptPassword(password),captcha)) {
			SysUser sysUser = sysUserService.queryById(WebUtil.getCurrentUser());
			SysDept company = sysDeptService.queryById(sysUser
					.getDeptId());
			SysUserBean bean = new SysUserBean();
			bean.setId(sysUser.getId());
			bean.setDeptName(company.getDeptName());
			bean.setUserName(sysUser.getUserName());
			//bean.setPdmodFlag(sysUser.getPdmodFlag());
			//bean.setCompany(company);
			return setSuccessModelMap(modelMap,bean);
		}
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 登出
	@ApiOperation(value = "用户登出")
	@PostMapping("/bizspace/logout")
	public Object logout(ModelMap modelMap) {
		SecurityUtils.getSubject().logout();
		return setSuccessModelMap(modelMap);
	}


	// 没有登录
	@ApiOperation(value = "没有登录")
	@GetMapping("/bizspace/unauthorized")
	public Object unauthorized(ModelMap modelMap) {
		SecurityUtils.getSubject().logout();
		return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
	}

	// 没有权限
	@ApiOperation(value = "没有权限")
	@GetMapping("/bizspace/forbidden")
	public Object forbidden(ModelMap modelMap) {
		return setModelMap(modelMap, HttpCode.FORBIDDEN);
	}
	
	@ApiOperation(value = "验证账号")
	@RequestMapping(value = "/bizspace/findpwd/validaccount")
	public Object validAccount(ModelMap modelMap,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "captcha", required = false) String captcha) {
		String exitCode = (String) SecurityUtils.getSubject().getSession()
				.getAttribute(Constants.KEY_CAPTCHA);
		if(!StringUtils.equals(captcha, exitCode)){
			throw new IllegalParameterException("验证码校验错误！");
		}else{
			SecurityUtils.getSubject().getSession().removeAttribute(org.ibase4j.core.Constants.KEY_SMS);
			//System.out.println(account);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orderBy", "id");
			params.put("phone", account);
			PageInfo<SysUser> users = sysUserService.query(params);
			if(users.getList().size()!=1){
				throw new IllegalParameterException("不存在该账号！");
			}else{
				SecurityUtils.getSubject().getSession().setAttribute(org.ibase4j.core.Constants.KEY_PHONE, users.getList().get(0).getPhone());
			}
		}
		return setSuccessModelMap(modelMap);
	}
	
	// 详细信息
	@ApiOperation(value = "修改信息验证短信")
	@RequestMapping(value = "/bizspace/findpwd/getsms")
	public Object getSms(ModelMap modelMap,
			@RequestParam(value = "captcha", required = false) String captcha) {
		String phone = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(org.ibase4j.core.Constants.KEY_PHONE));
		if(phone==null){
			throw new IllegalParameterException("不存在此手机号，不允许发送短信！");
		}
		String sms = smsService.sendShortMsg(phone);
		System.out.println(sms);
		SecurityUtils.getSubject().getSession().setAttribute(org.ibase4j.core.Constants.KEY_SMS, sms);
		return setSuccessModelMap(modelMap);
	}
	
	// 详细信息
	@ApiOperation(value = "验证短信")
	@RequestMapping(value = "/bizspace/findpwd/validsms")
	public Object validSms(ModelMap modelMap,
			@RequestParam(value = "captcha", required = false) String captcha) {
		String sms = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(org.ibase4j.core.Constants.KEY_SMS));
		if(!smsService.validSms(sms,captcha)){
			throw new IllegalParameterException("验证码校验错误！");
		}else{
			SecurityUtils.getSubject().getSession().setAttribute(org.ibase4j.core.Constants.KEY_SMS,"success");
		}
		
		return setSuccessModelMap(modelMap);
	}
	@ApiOperation(value = "验证短信")
	@RequestMapping(value = "/bizspace/findpwd/modpwd")
	public Object modPwd(ModelMap modelMap,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "captcha", required = false) String captcha) {
		String sms = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(org.ibase4j.core.Constants.KEY_SMS));
		if("success".equals(sms)){
			String phone = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(org.ibase4j.core.Constants.KEY_PHONE));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orderBy", "id");
			params.put("phone", phone);
			PageInfo<SysUser> users = sysUserService.query(params);
			
			SysUser oldRecord = sysUserService.queryById(users.getList().get(0).getId());
			String encryPwd = sysUserService.encryptPassword(password);
			//oldRecord.setPdmodFlag("1");
			oldRecord.setPassword(encryPwd);
			sysUserService.update(oldRecord);
			
			SecurityUtils.getSubject().getSession().removeAttribute(org.ibase4j.core.Constants.KEY_SMS);
		}else{
			throw new IllegalParameterException("您还未通过校验！");
		}
		return setSuccessModelMap(modelMap);
	}


}
