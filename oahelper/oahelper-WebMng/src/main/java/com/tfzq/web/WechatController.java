package com.tfzq.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ibase4j.core.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tfzq.wechat.common.Consts;
import com.tfzq.wechat.common.WechatUser;
import com.tfzq.wechat.common.WechatUserUtil;

@Controller
@RequestMapping("/wechat")
public class WechatController extends BaseController{
	public static final Logger log = LoggerFactory.getLogger(WechatController.class);

	@RequestMapping(method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 微信会在配置的回调地址上加上signature,nonce,timestamp,echostr4个参数
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		log.info("微信传递的签名参数  signature：{} timestamp：{} nonce:{} echostr:{}", signature, timestamp, nonce, echostr);
		// 1).排序
		String sortString = WechatUserUtil.sort(Consts.SELF_CODE_WX_TOKEN, timestamp, nonce);
		// 2).加密
		String mytoken = WechatUserUtil.sha1(sortString);
		// 3).校验签名
		if (!StringUtils.isEmpty(mytoken) && mytoken.equals(signature)) {
			log.info("微信签名校验通过。");
			response.getWriter().println(echostr);
		} else {
			log.warn("微信签名校验失败。");
		}
	}

	@RequestMapping(path = "/oauth", method = RequestMethod.GET)
	public void oauth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String bindUrl = URLEncoder.encode("http://" + Consts.DOMAIN + "/wechat/bindUser.html", Consts.ENCODING);
		// snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid） snsapi_userinfo
		// （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
		String wechatAuthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=BIND_URL?response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect"
				.replaceAll("APPID", Consts.AppId).replaceAll("BIND_URL", bindUrl);
		response.sendRedirect(wechatAuthUrl);
	}

	@RequestMapping(path = "/bindOpenid", method = RequestMethod.GET)
	public Object user(String code,ModelMap modelMap) {
		try {
			if (!StringUtils.isEmpty(code)) {
				log.info("获取到的 wechat code:{}", code);
				// 获取openid
				String openid = WechatUserUtil.exchangeCode2OpenId(code);
				if (!StringUtils.isEmpty(openid)) {
					// 获取微信用户信息
					WechatUser wechatUser = WechatUserUtil.getWechatUser(openid);
					log.info("获取到微信用户信息:", wechatUser);
				}
			} else {
				log.info("未获取到来自  wechat 端的 code");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return setSuccessModelMap(modelMap);
	}

}
