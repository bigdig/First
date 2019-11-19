package org.ibase4j.core.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.ibase4j.core.config.Resources;
import org.ibase4j.core.exception.LoginException;
import org.ibase4j.core.support.login.UsernamePasswordCaptchaToken;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysAuthorizeService;
import org.ibase4j.service.sys.SysSessionService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;

/**
 * 权限检查类
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:44:45
 */
public class Realm extends AuthorizingRealm {
	private final Logger logger = LogManager.getLogger();
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysSessionService sysSessionService;
	@Autowired
	private SysAuthorizeService sysAuthorizeService;

	private static String[] ACTIONS = { "read", "add", "update", "delete", "open", "close" };

	// 权限
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String userId = WebUtil.getCurrentUser();
		sysUserService.queryById(userId);
		// if (sysUser.getUserType() != 1) {
		// userId = null;
		// }
		List<String> list = sysAuthorizeService.queryPermissionByUserId(userId);
		for (String permission : list) {
			if (StringUtils.isNotBlank(permission)) {
				// 添加基于Permission的权限信息
				if (permission.endsWith("*")) {
					for (String action : ACTIONS) {
						String perm = StringUtils.replace(permission, "*", action);
						info.addStringPermission(perm);
					}
				} else {
					info.addStringPermission(permission);
				}
			}
		}
		// 添加用户权限
		info.addStringPermission("user");
		return info;
	}

	private PageInfo<SysUser> queryUserByAccount(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("countSql", 0);
		params.put("enable", 1);
		params.put("account", account);
		PageInfo<SysUser> pageInfo = sysUserService.query(params);
		return pageInfo;
	}

	private StringBuilder transPwd(char[] password) {
		StringBuilder sb = new StringBuilder(100);
		for (int i = 0; i < password.length; i++) {
			sb.append(password[i]);
		}
		return sb;
	}

	private AuthenticationInfo handleAdLogin(UsernamePasswordCaptchaToken token, boolean bindWx) {
		StringBuilder sb = transPwd(token.getPassword());// new
															// StringBuilder(100);
		boolean adLogined = AdHelper.check(token.getUsername(), sb.toString());
		if (adLogined) {
			PageInfo<SysUser> pageInfo = queryUserByAccount(token.getUsername());// sysUserService.query(params);
			if (pageInfo.getSize() == 1) {
				SysUser user = pageInfo.getList().get(0);
				if (bindWx) {
					// 第一次登录没有wxid，校验手机号和密码，并更新用户wxid和头像
					if (!StringUtils.equals(user.getAvatar(), token.getAvatar())
							|| !StringUtils.equals(user.getWxId(), token.getWxId())) {
						user.setWxId(token.getWxId());
						user.setAvatar(token.getAvatar());
						sysUserService.update(user);
					}
				}
				WebUtil.saveCurrentUser(user.getId());
				saveSession(user.getAccount());
				AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), sb.toString(),
						user.getUserName());
				return authcInfo;
			} else {
				logger.warn("AD User Exists, but No user in system: {}", token.getUsername());
				return null;
			}
		} else {
			logger.warn("AD USER [{}] PASSWORD IS WRONG: {}", token.getUsername(), sb.toString());
			return null;
		}
	}

	private AuthenticationInfo handleAccountLogin(UsernamePasswordCaptchaToken token, boolean bindWx) {
		
		PageInfo<SysUser> pageInfo = queryUserByAccount(token.getUsername());// sysUserService.query(params);
		if (pageInfo.getSize() == 1) {
			SysUser user = pageInfo.getList().get(0);
			StringBuilder sb = transPwd(token.getPassword());
			if (user.getLocked()) {
				logger.warn("USER [{}] IS Locked: {}", token.getUsername(), sb.toString());
				return null;
			}
			if (user.getPassword().equals(sb.toString())) {
				if (bindWx) {
					// 第一次登录没有wxid，校验手机号和密码，并更新用户wxid和头像
					if (!StringUtils.equals(user.getAvatar(), token.getAvatar())
							|| !StringUtils.equals(user.getWxId(), token.getWxId())) {
						user.setWxId(token.getWxId());
						user.setAvatar(token.getAvatar());
						sysUserService.update(user);
					}
				}
				
				WebUtil.saveCurrentUser(user.getId());
				saveSession(user.getAccount());
				AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
						user.getUserName());
				return authcInfo;
			}
			logger.warn("USER [{}] PASSWORD IS WRONG: {}", token.getUsername(), sb.toString());
			return null;
		} else {
			logger.warn("No user: {}", token.getUsername());
			return null;
		}
	}
	
	private AuthenticationInfo handleWxAccountLogin(UsernamePasswordCaptchaToken token) {
		PageInfo<SysUser> pageInfo = queryUserByWxId(token.getWxId());// sysUserService.query(params);
		if (pageInfo.getSize() >= 1) {
			SysUser user = pageInfo.getList().get(0);
			//StringBuilder sb = transPwd(token.getPassword());
			if (user.getLocked()) {
				logger.warn("USER [{}] IS Locked: {}", token.getUsername());
				return null;
			}
			WebUtil.saveCurrentUser(user.getId());
			saveSession(user.getAccount());
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
					user.getUserName());
			return authcInfo;

		} else {
			logger.warn("No WxId user: {}", token.getUsername());
			return null;
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

	private AuthenticationInfo handleDataPlatLogin(UsernamePasswordCaptchaToken token) {
		String dpAuthUrl = PropertiesUtil.getString("dp.url.authServlet") + "?token=" + token.getDptoken();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(dpAuthUrl);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"), e);
		}
		String dpresult = "";
		try {
			HttpEntity entity = response.getEntity();
			dpresult = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"), e);
		}

		if ("access_denied".equals(dpresult)) {
			return null;
		} else {
			PageInfo<SysUser> pageInfo = queryUserByAccount(dpresult);// sysUserService.query(params);
			if (pageInfo.getSize() == 1) {
				SysUser user = pageInfo.getList().get(0);
				if (user.getLocked()) {
					return null;
				}
				WebUtil.saveCurrentUser(user.getId());
				saveSession(user.getAccount());
				token.setUsername(user.getAccount());
				token.setPassword(user.getPassword().toCharArray());
				AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
						user.getUserName());
				return authcInfo;
			} else {
				logger.warn("No user: {}", dpresult);
				return null;
			}
		}
	}

	// 登录验证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
		// 微信登录，( AD登录校验或者 用户名密码校验)
		System.out.println(token.toString());
		if (StringUtils.isNotBlank(token.getWxId())) {
			//微信账号直接登录成功
			if(token.getExistWx()){
				System.out.println("微信账号直接登录成功");
				AuthenticationInfo wxAuth = handleWxAccountLogin(token);
				return wxAuth;
			}
			// AD登录 + 小程序绑定
			if (token.getAdlogin()) {
				System.out.println("AD登录 + 小程序绑定");
				AuthenticationInfo adAuth = handleAdLogin(token, true);
				return adAuth;
			} else {
				// 账号登录+ 小程序绑定
				System.out.println("账号登录+ 小程序绑定");
				AuthenticationInfo accountAuth = handleAccountLogin(token, true);
				return accountAuth;
			}
			// WEB AD登录校验
		} else if (token.getAdlogin()) {
			AuthenticationInfo adAuth = handleAdLogin(token, false);
			return adAuth;
		} else {
			if (StringUtils.isNoneBlank(token.getDptoken())) {
				return handleDataPlatLogin(token);
			} else {
				AuthenticationInfo accountAuth = handleAccountLogin(token, false);
				return accountAuth;
			}
		}

	}

	/** 保存session */
	private void saveSession(String account) {
		// 踢出用户
		/*
		sysSessionService.deleteByAccount(account);
		SysSession record = new SysSession();
		record.setAccount(account);
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		record.setSessionId(session.getId().toString());
		String host = (String) session.getAttribute("HOST");
		record.setIp(StringUtils.isBlank(host) ? session.getHost() : host);
		record.setStartTime(session.getStartTimestamp());
		sysSessionService.update(record);
		*/
	}
}
