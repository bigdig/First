package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfzq.pr.model.generator.GoAudity;
import com.tfzq.pr.model.generator.GoAudityRecord;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoAudityBean;
import com.tfzq.pr.model.pr.GoAudityRecordBean;
import com.tfzq.service.GoAudityRecordService;
import com.tfzq.service.GoAudityService;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 信息审批控制类，提供H5页面做审核的接口
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "信息审批管理", description = "信息审批管理")
@RequestMapping(value = "bizspace/goAudityH5", method = RequestMethod.POST)
public class GoAudityH5Controller extends BaseController {
	@Autowired
	private GoAudityService goAudityService;
	@Autowired
	private GoAudityRecordService goAudityRecordService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private GoInformService goInformService;
	@Autowired
	private GoInformUserService goInformUserService;

	// 查询信息审批记录
	@ApiOperation(value = "查询信息审批记录")
	// @RequiresPermissions("pr.goAudityRecord.read")
	@RequestMapping(value = "/getAudityRecord")
	public Object getAudityRecord(HttpServletRequest request, ModelMap modelMap) {
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

	// 详细信息
	@ApiOperation(value = "信息审批详情")
	@RequestMapping(value = "/getAudityDetail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoAudityBean goAudityBean = new GoAudityBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoAudity record = goAudityService.queryById(id);
			try {
				BeanUtils.copyProperties(goAudityBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goAudityBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> audity_statusMap = sysDicService.queryDicByDicIndexKey("AUDITY_STATUS");
		addDictFromMap(dicts, "AUDITY_STATUS", audity_statusMap);
		if (goAudityBean != null) {
			if (StringUtils.isNotEmpty(goAudityBean.getAudityStatus())) {
				goAudityBean.setAudityStatusText(audity_statusMap.get(goAudityBean.getAudityStatus().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goAudityBean, dicts);
	}

	@ApiOperation(value = "信息审批")
	// @RequiresPermissions("pr.goAudity.delete")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Object audity(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "audityId", required = false) String audityId,
			@RequestParam(value = "audityUserId", required = false) String audityUserId) {
		//String audityId = audityId;
//		String audityuserName = request.getParameter("userName");
//		String audityStatus = request.getParameter("audityStatus");
		String isPassed = request.getParameter("isPassed");
		String audityComment = request.getParameter("audityComment");

		GoAudity audity = goAudityService.queryById(audityId);
		if(audity.getAudityStatus().equals("PASSED") || audity.getAudityStatus().equals("RETURNED")){
			throw new IllegalParameterException("该条记录已经被其他老师处理,请勿重复处理。");
		}
		if (isPassed.equals("YES")) {
			audity.setAudityStatus("PASSED");
		} else {
			audity.setAudityStatus("RETURNED");//审批驳回，生成消息提醒推送给提交人
		}
		goAudityService.update(audity);

		String userId = audityUserId;//WebUtil.getCurrentUser();
		SysUser user = sysUserService.queryById(userId);
		if(user==null){
			throw new IllegalParameterException("用户信息不存在");
		}
		GoAudityRecord record = new GoAudityRecord();
		record.setAudityId(audityId);
		record.setUserId(user.getId());
		record.setUserName(user.getUserName());
		record.setIsPassed(isPassed);
		record.setAudityComment(
				(StringUtils.isEmpty(audityComment) || StringUtils.equals(audityComment, "null")) ? "" : audityComment);
		goAudityRecordService.add(record);

		// 生成审批驳回/通过信息
		GoInform goinform = new GoInform();
		String contentText = audity.getAudityStatus().equals("RETURNED") ? "被驳回" : "已通过";
		String informContent ="您提交的审批信息("+audity.getAudityTitle()+")"+contentText+"，请注意查收。";
		goinform.setInformContent(informContent);
		// String titleText =
		// audity.getAudityStatus().equals("RETURNED")?"驳回":"通过";
		// goinform.setInformTitle("审批"+titleText+"提醒");
		goinform.setInformTitle(informContent);
		goinform.setExternalLink("");
		goinform.setSrcId(null);
		goinform.setSrcType("AUDITY");
		goinform.setInformGroup(null);
		goinform.setIsReply("NO");
		goInformService.add(goinform);

		GoInformUser goInformUser = new GoInformUser();
		goInformUser.setInformId(goinform.getId());
		goInformUser.setUserId(audity.getUserId());
		goInformUser.setUserName(audity.getUserName());
		goInformUser.setIsRead("NO");
		goInformUser.setIsReplied("NO");
		goInformUser.setIsSend("0");
		goInformUserService.add(goInformUser);
		
		goAudityService.update(audity);
		return setSuccessModelMap(modelMap);
	}
}
