package com.xxl.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tfzq.pr.entity.PrMonitoring;
import com.tfzq.pr.entity.PrVitality;
import com.tfzq.pr.service.PrMonitoringService;
import com.tfzq.pr.service.PrVitalityService;
import com.tfzq.utils.YqUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="prVitalityJobHandler")
@Component
public class PrVitalityJobHandler extends IJobHandler {
	@Autowired
	private PrVitalityService prVitalityService;
	@Autowired
	private PrMonitoringService prMonitoringService;

    //@Value("${yqjc.companyId}")
	private String companyId;
    @Value("${yqjc.userName}")
	private String userName;
    @Value("${yqjc.secret}")
	private String secret;
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		List<PrMonitoring> prMonList = new ArrayList<>();
		List<PrMonitoring> allDatatype = prMonitoringService.findAll();
		for(int i=0;i<allDatatype.size();i++){
			if(allDatatype.get(i).getDataType().equals("3")){
				prMonList.add(allDatatype.get(i));
			}
		}
		for(int i=0;i<prMonList.size();i++){
			companyId = prMonList.get(i).getId();
			String token = YqUtils.getToken(userName, secret);
			String[] params = StringUtils.split(param,",");
			String dataTime= (params == null||params.length<1)?null:params[0];
			String interval= (params == null||params.length<2)?null:params[1];
			int length = 0;
			int k = 1;
			List<PrVitality> record = YqUtils.getVitality(token,dataTime,interval,companyId);
			for (PrVitality prVitality : record) {
				if(!prVitality.getAmount().equals("0")){
					if(StringUtils.isNotEmpty(interval)){ 
						length = Integer.valueOf(interval);
					}
					String strNum = String.format("%04d", length);
					String strID = String.format("%04d", k);
					prVitality.setId(prVitality.getBusiDate().replace("-", "")+strNum+strID+companyId);
					prVitalityService.insert(prVitality);
					k++;
				}
			}
		}
		return SUCCESS;
	}
}
