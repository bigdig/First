package com.xxl.job.executor.service.jobhandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ddfc.util.DateUtil;
import com.github.pagehelper.PageInfo;
import com.tfzq.tycrm.entity.TyCrmListedcompany;
import com.tfzq.tycrm.service.TyCrmListedcompanyService;
import com.tfzq.typlat.entity.TyListedcompany;
import com.tfzq.typlat.service.TyListedcompanyService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤： 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，
 * 注解value值对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "updateListedcompany")
@Component
public class UpdateListedcompanyJobHandler extends IJobHandler {
	private Logger logger = LoggerFactory.getLogger(UpdateListedcompanyJobHandler.class);
	@Autowired
	private TyListedcompanyService tyListedcompanyService;
	@Autowired
	private TyCrmListedcompanyService tyCrmListedcompanyService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {

		String lastestTime = null;
		if (StringUtils.isBlank(param)) {
			XxlJobLogger.log("增量导入上市公司数据! ");
			// 最底最后更新时间
			TyCrmListedcompany lastestLc = tyCrmListedcompanyService.selectMaxUpdateTime("");
			lastestTime = DateUtil.format(lastestLc.getUpdateTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
		} else if (StringUtils.equals(param, "All")) {
			XxlJobLogger.log("全量导入上市公司数据! ");
		}

		int num = 0;
		int pageNum = 1;
		int pageSize = 1000;
		while (true) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNum", pageNum);
			params.put("pageSize", pageSize);
			params.put("lastestTime", lastestTime);
			PageInfo<TyListedcompany> results = tyListedcompanyService.findByPage(params);
			for (TyListedcompany lc : results.getList()) {
				TyCrmListedcompany crmLc = new TyCrmListedcompany();

				BeanUtils.copyProperties(lc, crmLc);
				crmLc.setDataSrc("0");
				tyCrmListedcompanyService.insert(crmLc);
				logger.debug("更新" + crmLc.toString());

			}
			num = num + results.getList().size();
			pageNum++;
			XxlJobLogger.log("导入上市公司数据" + num + "/"+results.getTotal()+"条。");
			if (pageNum > results.getPages()) {
				break;
			}
		}
		return SUCCESS;
	}

}