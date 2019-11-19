package com.xxl.job.executor.service.jobhandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tfzq.tycrm.entity.GoInform;
import com.tfzq.tycrm.entity.GoInformUser;
import com.tfzq.tycrm.entity.SysUser;
import com.tfzq.tycrm.service.GoInformService;
import com.tfzq.tycrm.service.GoInformUserService;
import com.tfzq.tycrm.service.SysUserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler(value = "GenerateMorningInformJobHandler")
@Component
public class GenerateMorningInformJobHandler extends IJobHandler{

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private GoInformUserService goInformUserService;
	@Autowired
	private GoInformService goInformService;
	
	private Logger logger = LoggerFactory.getLogger(GenerateMorningInformJobHandler.class);
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		// TODO Auto-generated method stub
		// 定时任务开关的状态为开时才执行定时任务
		boolean result = insertInformUserInfo(param);
		if (result) {
			return SUCCESS;
		} else {
			return FAIL;
		}
	}
	
	private boolean insertInformUserInfo(String skipHolidays) {
		boolean result = false;
		//判断该日期是否是周末。不是：执行下一步；是：不执行
		if(!isWeekend(skipHolidays) ){
			//晨会提醒信息
			GoInform goinform=new GoInform();
			goinform.setInformContent("晨会即将开始，请及时打卡，谢谢！");
			goinform.setInformTitle("晨会打卡提醒");
			goinform.setExternalLink("");
			goinform.setSrcId(null);
			goinform.setSrcType("MEETING");
			goinform.setInformGroup(null);
			goinform.setIsReply("NO");
			
			goInformService.insert(goinform);
			//获取sysUser表中所有的用户信息
			List<SysUser> list=sysUserService.findAll();//findAll
			if(list!=null&&list.size()>0){
				for(SysUser user:list){
					if(!user.getEnable()){
						continue;
					}
					GoInformUser goInformUser = new GoInformUser();
					
					goInformUser.setInformId(goinform.getId());
					goInformUser.setUserId(user.getId());
					goInformUser.setUserName(user.getUserName());
					goInformUser.setIsRead("NO");
					goInformUser.setIsReplied("NO");
					goInformUser.setIsSend("0");
	
					goInformUserService.insert(goInformUser);
				}
				result=true;
			}
		}		
		return result;
	}
	
	//判断当前日期是否是周末，是返回true;
	public static boolean isWeekend(String skipHolidays) {	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar today = Calendar.getInstance();
		String d = sdf.format(today.getTime());
		if(StringUtils.isNotEmpty(skipHolidays)&&skipHolidays.contains(d)){
			return true;
		}
		if(today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
			return true;
		}else{
			return false;
		}		
	}	
}