package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysUserService;
import org.ibase4j.core.support.DictItem;
import com.tfzq.pr.model.generator.GoAudity;
import com.tfzq.pr.model.generator.GoAudityRecord;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoAudityBean;
import com.tfzq.service.GoAudityRecordService;
import com.tfzq.service.GoAudityService;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;
import com.tfzq.util.PbFileUtils;

/**
 * 信息审批控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "信息审批管理", description = "信息审批管理")
@RequestMapping(value = "pr/goAudity", method = RequestMethod.POST)
public class GoAudityController extends BaseController {
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
	
	private static String AUDITY_URL="https://tytool.tfzq.com/#/preview/";

	// 查询信息审批
	@ApiOperation(value = "查询信息审批")
	// @RequiresPermissions("pr.goAudity.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "update_time desc");
		PageInfo<GoAudityBean> list = goAudityService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> audity_statusMap = sysDicService.queryDicByDicIndexKey("AUDITY_STATUS");
		addDictFromMap(dicts, "AUDITY_STATUS", audity_statusMap);
		for (GoAudityBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getAudityStatus())) {
				singleBean.setAudityStatusText(audity_statusMap.get(singleBean.getAudityStatus().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}
	

	// 查询信息审批
	@ApiOperation(value = "查询信息审批")
	// @RequiresPermissions("pr.goAudity.read")
	@RequestMapping(value = "/read/mylist")
	public Object getMylist(HttpServletRequest request, ModelMap modelMap) {
		
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("userId", WebUtil.getCurrentUser());
		params.put("orderBy", "update_time desc");
		PageInfo<GoAudityBean> list = goAudityService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> audity_statusMap = sysDicService.queryDicByDicIndexKey("AUDITY_STATUS");
		addDictFromMap(dicts, "AUDITY_STATUS", audity_statusMap);
		for (GoAudityBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getAudityStatus())) {
				singleBean.setAudityStatusText(audity_statusMap.get(singleBean.getAudityStatus().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "信息审批详情")
	// @RequiresPermissions("pr.goAudity.read")
	@RequestMapping(value = "/read/detail")
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

	// 新增信息审批
	@ApiOperation(value = "添加信息审批")
	// @RequiresPermissions("pr.goAudity.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoAudity record = Request2ModelUtil.covert(GoAudity.class, request);
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
		record.setUserId(user.getId());
		record.setUserName(user.getUserName());
		record.setAudityStatus("COMMITED");
		GoAudity ret =goAudityService.add(record);
		//生成审批提交记录
		GoAudityRecord audityRecord = new GoAudityRecord();
		audityRecord.setAudityId(ret.getId());
		audityRecord.setUserId(user.getId());
		audityRecord.setUserName(user.getUserName());
		audityRecord.setIsPassed("SUBMIT");
		audityRecord.setAudityComment(record.getAudityLink());
		audityRecord.setAudityPicture(record.getAudityPicture());
		goAudityRecordService.add(audityRecord);

		//用户提交信息后，发送信息提醒审批人员进行审批
		if(autityNotice(ret)){//发送消息成功
			
		}else{
			//throw new IllegalParameterException("添加信息审批成功，但向审批人员推送提醒失败");
		}
		return setSuccessModelMap(modelMap);
	}

	// 修改信息审批
	@ApiOperation(value = "修改信息审批")
	// @RequiresPermissions("pr.goAudity.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoAudity record = Request2ModelUtil.covert(GoAudity.class, request);
		GoAudity oldRecord = goAudityService.queryById(record.getId());
		SysUser user = sysUserService.queryById(WebUtil.getCurrentUser());
//		record.setUserId(user.getId());
//		record.setUserName(user.getUserName());
		oldRecord.setAudityLink(record.getAudityLink());
		oldRecord.setAudityPicture(record.getAudityPicture());
		oldRecord.setAudityStatus(record.getAudityStatus());
		oldRecord.setAudityTitle(record.getAudityTitle());
		
		goAudityService.update(oldRecord);
		//生成审批提交记录
		GoAudityRecord audityRecord = new GoAudityRecord();
		audityRecord.setAudityId(oldRecord.getId());
		audityRecord.setUserId(user.getId());
		audityRecord.setUserName(user.getUserName());
		audityRecord.setIsPassed("COMMIT");
		audityRecord.setAudityComment(record.getAudityLink());
		audityRecord.setAudityPicture(record.getAudityPicture());
		goAudityRecordService.add(audityRecord);

		//用户修改信息后，再次发送信息提醒给审批人员进行审批
		if(autityNotice(oldRecord)){//发送消息成功
			
		}else{
			//throw new IllegalParameterException("修改信息审批成功，但向审批人员推送提醒失败");
		}
		return setSuccessModelMap(modelMap);
	}

	// 删除信息审批
	@ApiOperation(value = "删除信息审批")
	// @RequiresPermissions("pr.goAudity.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoAudity record = goAudityService.queryById(id);
		if (record != null) {
			goAudityService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}

	@ApiOperation(value = "信息审批")
	// @RequiresPermissions("pr.goAudity.delete")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Object audity(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "audityId", required = false) String id) {
		String audityId = id;
		String audityuserName = request.getParameter("userName");
		String audityStatus = request.getParameter("audityStatus");
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

		String userId = WebUtil.getCurrentUser();
		SysUser user = sysUserService.queryById(userId);
		GoAudityRecord record = new GoAudityRecord();
		record.setAudityId(audityId);
		record.setUserId(user.getId());
		record.setUserName(user.getUserName());
		record.setIsPassed(isPassed);
		record.setAudityComment((StringUtils.isEmpty(audityComment)||StringUtils.equals(audityComment, "null"))?"":audityComment);
		goAudityRecordService.add(record);

		
//		Map<String, Object> params = WebUtil.getParameterMap(request);
//		params.put("userName", audityuserName);
		//将提交人信息放入goInfromUser表中
//		List<SysUser> lists = sysUserService.queryByCondition(params);
		//if(lists!=null&&lists.size()>0){
			//生成审批驳回/通过信息
		GoInform goinform=new GoInform();
		String contentText = audity.getAudityStatus().equals("RETURNED")?"被驳回":"已通过";
		String informContent ="您提交的审批信息("+audity.getAudityTitle()+")"+contentText+"，请注意查收。";
		goinform.setInformContent(informContent);
		//String titleText = audity.getAudityStatus().equals("RETURNED")?"驳回":"通过";
//		goinform.setInformTitle("审批"+titleText+"提醒");
		goinform.setInformTitle(informContent);
		goinform.setExternalLink("");
		goinform.setSrcId(null);
		goinform.setSrcType("AUDITY");
		goinform.setInformGroup(null);
		goinform.setIsReply("NO");
		goInformService.add(goinform);
			
			//for(SysUser list:lists){
		GoInformUser goInformUser = new GoInformUser();
		goInformUser.setInformId(goinform.getId());
		goInformUser.setUserId(audity.getUserId());
		goInformUser.setUserName(audity.getUserName());
		goInformUser.setIsRead("NO");
		goInformUser.setIsReplied("NO");
		goInformUser.setIsSend("0");
		goInformUserService.add(goInformUser);
			//}
		//}else{
			//提交人信息为空
		//}	
		return setSuccessModelMap(modelMap);
	}
	
	public boolean autityNotice(GoAudity record){
		//查询出所有具有审批权限的审批人员，插入goInformUser表中，推送通知   
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> roleIdsList=new ArrayList<String>();
		String role=PropertiesUtil.getString("autity.role");//暂时只配置管理员审批
		roleIdsList.add(role);
		
		params.put("roleIdsList", roleIdsList);
		List<SysUser> results = sysUserService.findAutityUsers(params);
		
		//将审批人员信息放入goInfromUser表中
		if(results!=null&&results.size()>0){
			//生成审批驳回信息
			GoInform goinform=new GoInform(); 
			String cont = "您有新的审批信息，请及时进行审批。提交人："+record.getUserName()+",标题："+record.getAudityTitle()+"。";
			goinform.setInformContent(cont);
//			goinform.setInformTitle("审批通知提醒");
			goinform.setInformTitle(cont);
			goinform.setExternalLink(AUDITY_URL +record.getId());
			goinform.setSrcId(null);
			goinform.setSrcType("AUDITY");
			goinform.setInformGroup(null);
			goinform.setIsReply("NO");
			goInformService.add(goinform);
			
			for(SysUser list:results){
				GoInformUser goInformUser = new GoInformUser();
				goInformUser.setInformId(goinform.getId());
				goInformUser.setUserId(list.getId());
				goInformUser.setUserName(list.getUserName());
				goInformUser.setIsRead("NO");
				goInformUser.setIsReplied("NO");
				goInformUser.setIsSend("0");
				goInformUserService.add(goInformUser);
			}
		}else{
			return false;
		}	
		return true;
	}

}
