package org.ibase4j.scheduler.task.impl;
import java.util.Date;

import org.ibase4j.provider.PushPublishmentProvider;
import org.ibase4j.scheduler.task.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * 定时任务
 */
@Repository
public class pushPublishmentJobImpl implements TaskScheduler{
	private static Logger logger = LoggerFactory.getLogger(pushPublishmentJobImpl.class);
	@Autowired
	private PushPublishmentProvider pushPublishmentProvider;	
	
	public void execute(){
		//定时任务开关的状态为开时才执行定时任务
		logger.info("Start the push task at "+new Date());
		pushPublishmentProvider.doPushTask();
		logger.info("Finish the push task at "+new Date());
	}
	
}
