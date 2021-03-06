package com.tfzq.wechatmsg;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) {
		// 新增用户成功 - 推送微信消息
		senMsg("oUBBZwTa7qxb0vP98X8WhAazdvUE");
	}

	static void senMsg(String openId) {
		WxTokenUtil tokenUtil = new WxTokenUtil();
		tokenUtil.setAppId("");
		tokenUtil.setAppSecret("");
		WxUserUtil userUtil = new WxUserUtil();
		userUtil.setTokenUtil(tokenUtil);
		// 用户是否订阅该公众号标识 (0代表此用户没有关注该公众号 1表示关注了该公众号)
		Integer state = userUtil.subscribeState(openId);
		// 绑定了微信并且关注了服务号的用户 , 注册成功-推送注册短信
		if (state == 1) {
			Map<String, TemplateData> param = new HashMap<>();
			param.put("first", new TemplateData("恭喜您注册成功！", "#696969"));
			param.put("keyword1", new TemplateData("15618551533", "#696969"));
			param.put("keyword2", new TemplateData("2017年05月06日", "#696969"));
			param.put("remark", new TemplateData("祝投资愉快！", "#696969"));
			// 注册的微信-模板Id
			String regTempId = "A4UjsYs_PXoJaAGZmtxPxH55dBYlj3E7k8XsG2c3wNs";
//					WX_TemplateMsgUtil.getWXTemplateMsgId(
//					WeiXinEnum.WX_TEMPLATE_MSG_NUMBER.USER_REGISTER_SUCCESS.getMsgNumber());
			// 调用发送微信消息给用户的接口
			JSONObject miniprogram = new JSONObject();
			miniprogram.put("appid", "wx5cf370a4997f448a");
			miniprogram.put("pagepath", "index");

			WxTemplateMsgUtil templateMsgUtil = new WxTemplateMsgUtil();
			templateMsgUtil.setTokenUtil(tokenUtil);
			templateMsgUtil.sendWechatMsgToUser(openId, regTempId, "",miniprogram, "#000000", templateMsgUtil.packJsonmsg(param));
		}
	}
}
