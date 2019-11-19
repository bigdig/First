package com.xxl.job.executor.service.jobhandler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tfzq.pr.entity.PrNews;
import com.tfzq.pr.entity.PrRealPublicopinion;
import com.tfzq.pr.service.PrRealPublicopinionService;
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
@JobHandler(value="prNewsRobotJobHandler")
@Component
public class PrNewsRobotJobHandler extends IJobHandler {
	@Autowired
	private PrRealPublicopinionService prRealPublicopinionService;

    @Value("${yqjc.companyId}")
	private String companyId;
    @Value("${yqjc.userName}")
	private String userName;
    @Value("${yqjc.secret}")
	private String secret;
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		String token = YqUtils.getToken(userName, secret);
		String[] params = StringUtils.split(param,",");
		String dataTime= (params == null||params.length<1)?null:params[0];
		String interval= (params == null||params.length<2)?null:params[1];
		int pageNum=1;
		int pageSize=100;
		boolean exeFlag = true;
		do{
			List<PrNews> news = YqUtils.realYq(token,dataTime,interval,pageSize,pageNum,companyId);
			if(news.size()==0){
				break;
			}
			else{
				for(PrNews prNews:news){
					PrRealPublicopinion record = new PrRealPublicopinion();
					BeanUtils.copyProperties(prNews, record);
					PrRealPublicopinion old = prRealPublicopinionService.selectByPrimaryKey(record.getId());
					if(old!=null){
						exeFlag = false;
						break;
					}else{
						prRealPublicopinionService.insert(record);
					}
					//record.setId(record.getNewsId());
				}
			}
			pageNum++;
		}while(exeFlag);
		//add 
		return SUCCESS;
	}

}
