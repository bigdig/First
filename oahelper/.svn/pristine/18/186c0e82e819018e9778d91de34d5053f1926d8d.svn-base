package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.PropertiesUtil;
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
import com.tfzq.pr.model.generator.GoCardApply;
import com.tfzq.pr.model.generator.GoInform;
import com.tfzq.pr.model.generator.GoInformUser;
import com.tfzq.pr.model.pr.GoCardApplyBean;
import com.tfzq.service.GoCardApplyService;
import com.tfzq.service.GoInformService;
import com.tfzq.service.GoInformUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 名片申请控制类
 * 
 * @author yuzhitao 
 */
@RestController
@Api(value = "名片申请管理", description = "名片申请管理")
@RequestMapping(value = "pr/goCardApply", method = RequestMethod.POST)
public class GoCardApplyController extends BaseController {
	@Autowired
	private GoCardApplyService goCardApplyService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private GoInformService goInformService;
	@Autowired
	private GoInformUserService goInformUserService;
		
	// 查询名片申请
	@ApiOperation(value = "查询名片申请")
	//@RequiresPermissions("pr.goCardApply.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy","id desc");
		PageInfo<GoCardApplyBean> list = goCardApplyService.queryBeans(params);
		
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		//添加字典翻译
		Map<String,String> card_statusMap = sysDicService.queryDicByDicIndexKey("CARD_STATUS");
		addDictFromMap(dicts,"CARD_STATUS", card_statusMap);
		for (GoCardApplyBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getCardStatus())) {
				singleBean.setCardStatusText(card_statusMap.get(singleBean.getCardStatus().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list,dicts);
	}

	// 详细信息
	@ApiOperation(value = "名片申请详情")
	//@RequiresPermissions("pr.goCardApply.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoCardApplyBean goCardApplyBean = new GoCardApplyBean();
		Map<String,List<DictItem>> dicts = new HashMap<String,List<DictItem>>();
		if(StringUtils.isNotEmpty(id)){
			GoCardApply record = goCardApplyService.queryById(id);
			try {
	            BeanUtils.copyProperties(goCardApplyBean, record);
				
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();  
	        }catch (Exception e) {
				goCardApplyBean = null;
	        	e.printStackTrace();
	        }
	    }
		//添加字典翻译
		Map<String,String> card_statusMap = sysDicService.queryDicByDicIndexKey("CARD_STATUS");
		addDictFromMap(dicts,"CARD_STATUS", card_statusMap);
	    if(goCardApplyBean!=null){
			if (StringUtils.isNotEmpty(goCardApplyBean.getCardStatus())) {
				goCardApplyBean.setCardStatusText(card_statusMap.get(goCardApplyBean.getCardStatus().toString()));
			}
	    }
	    
		return setSuccessModelMap(modelMap, goCardApplyBean,dicts);
	}

	
	// 新增名片申请
	@ApiOperation(value = "添加名片申请")
	//@RequiresPermissions("pr.goCardApply.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoCardApply record = Request2ModelUtil.covert(GoCardApply.class, request);
		GoCardApply ret = goCardApplyService.add(record);
		
		//用户新增名片申请后，发送信息提醒审批人员进行审批
		if(cardNotice(ret)){
			//发送消息成功
			
		}else{
			//throw new IllegalParameterException("添加信息审批成功，但向审批人员推送提醒失败");
		}
		return setSuccessModelMap(modelMap);
	}

	// 修改名片申请
	@ApiOperation(value = "修改名片申请")
	//@RequiresPermissions("pr.goCardApply.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoCardApply record = Request2ModelUtil.covert(GoCardApply.class, request);
		goCardApplyService.update(record);
		
		//名片申请处理后，发送消息给名片申请人
		cardNoticeForApplicant(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除名片申请
	@ApiOperation(value = "删除名片申请")
	//@RequiresPermissions("pr.goCardApply.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoCardApply record = goCardApplyService.queryById(id);
		if(record!=null){
			goCardApplyService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
	
	private boolean cardNotice(GoCardApply record){
		//查询出所有具有审批权限的审批人员，插入goInformUser表中，推送通知   
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> roleIdsList=new ArrayList<String>();
		String role=PropertiesUtil.getString("card.role");//暂时只配置管理员审批
		roleIdsList.add(role);
		
		params.put("roleIdsList", roleIdsList);
		List<SysUser> results = sysUserService.findAutityUsers(params);
		
		//将审批人员信息放入goInfromUser表中
		if(results!=null&&results.size()>0){
			GoInform goinform=new GoInform(); 
			String cont = "您有新的名片制卡申请，请及时进行处理。提交人："+record.getUserName()+"。";
			goinform.setInformContent(cont);
//			goinform.setInformTitle("审批通知提醒");
			goinform.setInformTitle(cont);
			goinform.setExternalLink("");
			goinform.setSrcId(null);
			goinform.setSrcType("INFO");
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
	
	private void cardNoticeForApplicant(GoCardApply record){
		Map<String,String> card_statusMap = sysDicService.queryDicByDicIndexKey("CARD_STATUS");

		//将处理制卡信息放入goInfromUser表中
		GoInform goinform=new GoInform(); 
		String cont = "您的名片制卡申请状态为："+card_statusMap.get(record.getCardStatus().toString())+"。";
		goinform.setInformContent(cont);
		goinform.setInformTitle(cont);
		goinform.setExternalLink("");
		goinform.setSrcId(null);
		goinform.setSrcType("NOTICE");
		goinform.setInformGroup(null);
		goinform.setIsReply("NO");
		goInformService.add(goinform);
					
		GoInformUser goInformUser = new GoInformUser();
		goInformUser.setInformId(goinform.getId());
		goInformUser.setUserId(record.getUsereId());
		goInformUser.setUserName(record.getUserName());
		goInformUser.setIsRead("NO");
		goInformUser.setIsReplied("NO");
		goInformUser.setIsSend("0");
		goInformUserService.add(goInformUser);
				
	}
}
