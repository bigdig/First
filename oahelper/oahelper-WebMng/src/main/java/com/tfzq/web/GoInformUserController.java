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
import org.ibase4j.core.util.Request2ModelUtil;
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

import com.github.pagehelper.PageInfo;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoInformUserBean;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;
import com.tfzq.service.MyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 通知消息-用户控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "通知消息-用户管理", description = "通知消息-用户管理")
@RequestMapping(value = "pr/goInformUser", method = RequestMethod.POST)
public class GoInformUserController extends BaseController {
	@Autowired
	private GoInformUserService goInformUserService;
	@Autowired
	private GoInformService goInformService;
	@Autowired
	private MyService myService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;
	// 查询通知消息-用户
	@ApiOperation(value = "查询通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoInformUserBean> list = goInformUserService.queryBeans(params);

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISREPLIED");
		addDictFromMap(dicts, "ISREPLIED", yes_noMap);
		for (GoInformUserBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getIsRead())) {
				singleBean.setIsReadText(yes_noMap.get(singleBean.getIsRead().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsReplied())) {
				singleBean.setIsRepliedText(yes_noMap.get(singleBean.getIsReplied().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 通过消息ID查询：通知消息-用户
	@ApiOperation(value = "条件查询通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.read")
	@RequestMapping(value = "/selectByCondition")
	public Object selectByInform(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("orderBy", "userName desc");
		List<GoInformUserBean> users = goInformUserService.queryByCondition(params);
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISREPLIED");
		addDictFromMap(dicts, "ISREPLIED", yes_noMap);

		for (GoInformUserBean singleBean : users) {
			if (StringUtils.isNotEmpty(singleBean.getIsRead())) {
				singleBean.setIsReadText(yes_noMap.get(singleBean.getIsRead().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsReplied())) {
				singleBean.setIsRepliedText(yes_noMap.get(singleBean.getIsReplied().toString()));
			}
		}
		return setSuccessModelMap(modelMap, users, dicts);
	}

	// <if test="equipList != null">
	// and (select 1 from sc_roomequip t2,sc_equipment t3
	// where t2.equip_id = t3.id
	// and t2.room_id = t.id
	// <foreach collection="equipList" open=" and t3.equip_type in(" close=")"
	// item="id" separator=",">
	// #{id}
	// </foreach>
	// )
	// </if>

	@ApiOperation(value = "查询通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.read")
	@RequestMapping(value = "/selectUserInform")
	public Object selectUserInform(HttpServletRequest request,
			@RequestParam(value = "userId", required = false) String userId, ModelMap modelMap) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalParameterException("用户编号不允许为空");
		}
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("userId", userId);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoInformUserBean> list = goInformUserService.queryBeans(params);
		for (GoInformUserBean informUser : list.getList()) {
			informUser.setInform(goInformService.queryById(informUser.getInformId()));
		}
		// List<MyInform> myInforms = myService.selectUserInform(userId);
		return setSuccessModelMap(modelMap, list);
	}

	@ApiOperation(value = "用户确认消息")
	// @RequiresPermissions("pr.goInformUser.read")
	@RequestMapping(value = "/confirm")
	public Object confirm(HttpServletRequest request,
			 @RequestParam(value = "id", required = true) String id,
			 @RequestParam(value = "isReplied", required = true) String isReplied,
			 @RequestParam(value = "remark", required = false) String remark,
			ModelMap modelMap) {
		GoInformUser record = goInformUserService.queryById(id);

		record.setIsRead("YES");
		record.setIsReplied(isReplied);
		record.setRemark(remark);
		goInformUserService.update(record);
		
		//对流程提醒模块中的消息，确认后，把主记录中的备注字段进行完善
		GoInform goInform = goInformService.queryById(record.getInformId());
		if(StringUtils.startsWith(goInform.getSrcType(), "RMD_")){
			StringBuffer goInformRemark = new StringBuffer();
			goInformRemark.append(record.getUserName());
			goInformRemark.append("DISAGREE".equals(isReplied)?"有异议":"已确认");
			goInformRemark.append("。"+remark);
			goInform.setRemark(goInformRemark.toString());
			goInformService.update(goInform);
		}
		
		//生成提醒消息
		if("DISAGREE".equals(isReplied)){
			GoInform lastGoinform = goInformService.queryById(record.getInformId());
			SysUser creater = sysUserService.queryById(lastGoinform.getCreateBy());
			//生成有异议的信息
			GoInform goinform=new GoInform();
			goinform.setInformContent(record.getUserName()+"对"+lastGoinform.getInformTitle()+"有异议，请注意处理。异议理由："+remark);
			goinform.setInformTitle(record.getUserName()+"对"+lastGoinform.getInformTitle()+"有异议。");
			goinform.setExternalLink("");
			goinform.setSrcId(null);
			goinform.setSrcType("INFO");
			goinform.setInformGroup(null);
			goinform.setIsReply("NO");
			goInformService.add(goinform);
				
			GoInformUser goInformUser = new GoInformUser();
			goInformUser.setInformId(goinform.getId());
			goInformUser.setUserId(creater.getId());
			goInformUser.setUserName(creater.getUserName());
			goInformUser.setIsRead("NO");
			goInformUser.setIsReplied("NO");
			goInformUser.setIsSend("0");
			goInformUserService.add(goInformUser);
		}
		
		return setSuccessModelMap(modelMap, null);
	}

	// 详细信息
	@ApiOperation(value = "通知消息-用户详情")
	// @RequiresPermissions("pr.goInformUser.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoInformUserBean goInformUserBean = new GoInformUserBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoInformUser record = goInformUserService.queryById(id);
			try {
				BeanUtils.copyProperties(goInformUserBean, record);
				goInformUserBean.setInform(goInformService.queryById(goInformUserBean.getInformId()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goInformUserBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("ISREPLIED");
		addDictFromMap(dicts, "ISREPLIED", yes_noMap);
		if (goInformUserBean != null) {
			if (StringUtils.isNotEmpty(goInformUserBean.getIsRead())) {
				goInformUserBean.setIsReadText(yes_noMap.get(goInformUserBean.getIsRead().toString()));
			}
			if (StringUtils.isNotEmpty(goInformUserBean.getIsReplied())) {
				goInformUserBean.setIsRepliedText(yes_noMap.get(goInformUserBean.getIsReplied().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goInformUserBean, dicts);
	}

	// 新增通知消息-用户
	@ApiOperation(value = "添加通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoInformUser record = Request2ModelUtil.covert(GoInformUser.class, request);
		goInformUserService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改通知消息-用户
	@ApiOperation(value = "修改通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoInformUser record = Request2ModelUtil.covert(GoInformUser.class, request);
		goInformUserService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除通知消息-用户
	@ApiOperation(value = "删除通知消息-用户")
	// @RequiresPermissions("pr.goInformUser.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoInformUser record = goInformUserService.queryById(id);
		if (record != null) {
			goInformUserService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
