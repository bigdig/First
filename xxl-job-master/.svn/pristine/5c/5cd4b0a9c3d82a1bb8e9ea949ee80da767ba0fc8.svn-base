package com.xxl.job.executor.service.jobhandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ddfc.service.RedisUtil;
import com.ddfc.util.DateUtil;
import com.tfzq.tycrm.entity.TyActivity;
import com.tfzq.tycrm.entity.TyActivitysign;
import com.tfzq.tycrm.entity.TyActivitystock;
import com.tfzq.tycrm.entity.TyCrmListedcompany;
import com.tfzq.tycrm.entity.TyCustomerlabel;
import com.tfzq.tycrm.entity.TyLabel;
import com.tfzq.tycrm.service.TyActivityService;
import com.tfzq.tycrm.service.TyActivityStockService;
import com.tfzq.tycrm.service.TyCrmListedcompanyService;
import com.tfzq.tycrm.service.TyCustomerlabelService;
import com.tfzq.tycrm.service.TyLabelService;
import com.tfzq.utils.Constants;
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
@JobHandler(value = "updateActivity")
@Component
public class UpdateActivityJobHandler extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(UpdateActivityJobHandler.class);

	@Autowired
	private TyActivityService tyActivityService;
    @Autowired
    private RedisUtil redisService;
    @Autowired
    private TyLabelService tyLabelService;
    @Autowired
    private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private TyCrmListedcompanyService tyCrmListedcompanyService;
	@Autowired
	private TyActivityStockService tyActivityStockService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		Integer endDate = Integer.parseInt(DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYYYMMDD));
		Integer endTime = Integer.parseInt(DateUtil.getDateTime(DateUtil.DATE_PATTERN.HHMMSS));
		params.put("hasFinished", "1");
		params.put("endDate", endDate);
		params.put("endTime", endTime);
		params.put("activityStatusEx", "2");
		
		List<TyActivity> results = tyActivityService.findByCondition(params);
		XxlJobLogger.log("更新Activity活动状态["+endDate+","+endTime+"]! ，条数：" + results.size());
		List<TyLabel> sublabel = tyLabelService.getAllSubRecords();
		Map<String,TyLabel> labelMap = tranToLabelMap(sublabel);
		for(TyActivity act:results){
			//1.更新活动状态
			act.setActivityStatus("2");
			tyActivityService.update(act);
			
			//2.更新活动-用户参与状态
			TyActivitysign record = new TyActivitysign();
			record.setActivityId(act.getId());
			tyActivityService.updateSignByActivityId(record);
			//ty:tyActivity:2018032618045769500010,ty:tyActivity:2018032917274982400013
			String key ="ty:tyActivity:"+act.getId();
			//logger.info(key);
			redisService.delete(key);
			//redisService.add("test:"+act.getId(), act.getId());
			//3.把活动上的标签更新到客户上去
			List<String> custIds = tyActivityService.getCustIdsByActivityId(act.getId());
			if(StringUtils.isNotBlank(act.getLabels())){
				Set<String> labelSet = new HashSet<String>();
				labelSet.addAll(Arrays.asList(act.getLabels().split(" ")));
				if(custIds != null && custIds.size() > 0){
					for (String custId : custIds) {
						updateLabels(labelSet, custId, labelMap,Constants.LABEL_OTHER_CAT_ID,Constants.LABEL_OTHER_CAT_NAME);
					}
				}
			}
			//5.把活动上的上市公司也做为客户的标签
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("activityId", act.getId());
			List<TyActivitystock> stocks = tyActivityStockService.findByCondition(params2);
			Set<String> lcLabelSet = new HashSet<String>();
			for(TyActivitystock s : stocks){
				TyCrmListedcompany listCompany = tyCrmListedcompanyService.selectByPrimaryKey(s.getStockId());
				lcLabelSet.add(listCompany.getCompanyShortname());
			}
			if(custIds != null && custIds.size() > 0){
				for (String custId : custIds) {
					updateLabels(lcLabelSet, custId, labelMap,Constants.LABEL_LISTCOMPANY_CAT_ID,Constants.LABEL_LISTCOMPANY_CAT_NAME);
				}
			}
		}
		return SUCCESS;
	}
	
	private Map<String,TyLabel> tranToLabelMap(List<TyLabel> sublabels){
		 Map<String,TyLabel> map=new HashMap<String,TyLabel>();
		 for(TyLabel l:sublabels){
			 map.put(l.getLabelName(), l);
		 }
		 return map;
	}
	
	private void updateLabels(Collection<String> labels,String custId,Map<String,TyLabel> labelMaps,String catId,String catName){
		if(labels!=null && labels.size()>0){
			//如果标签名称已存在那么添加
			//如果不存在，先创建该标签到其他分类，然后再添加
			for (String labelName : labels) {
				TyLabel sub = labelMaps.get(labelName);
				if(sub!=null){
					addCustLabel(custId,sub.getId());
				}else{
					//先创建标签
					TyLabel lb = new TyLabel();
					lb.setLabelName(labelName);
					lb.setCatId(catId);
					lb.setCatName(catName);
					lb.setLabelLevel("1");
					lb.setPid("0");
					TyLabel newRecord = tyLabelService.add(lb);
					//再添加用户标签关系
					addCustLabel(custId,newRecord.getId());

				}
			}
		}
	}
	
	/**
	 * 添加用户标签关系
	 * @param custId
	 * @param labelId
	 */
	private void addCustLabel(String custId,String labelId){
		String id = StringUtils.leftPad(custId, 22, "0")+StringUtils.leftPad(labelId, 22, "0");
		TyCustomerlabel custLabel = tyCustomerlabelService.selectByPrimaryKey(id);
		if(custLabel == null){
			custLabel = new  TyCustomerlabel();
			custLabel.setCustomerId(custId);
			custLabel.setLabelId(labelId);
			custLabel.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
			custLabel.setValidCount(1);
			custLabel.setCreateBy("1");
			tyCustomerlabelService.add(custLabel);
		}else{
			custLabel.setActiveDate(Integer.parseInt(DateUtil.format(new Date(), DateUtil.DATE_PATTERN.YYYYMMDD)));
			custLabel.setValidCount(custLabel.getValidCount() + 1);
			tyCustomerlabelService.update(custLabel);
		}

	}

}