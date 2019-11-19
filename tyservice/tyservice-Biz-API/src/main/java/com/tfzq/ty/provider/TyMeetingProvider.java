package com.tfzq.ty.provider;

import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyMeeting;
import com.tfzq.ty.model.ty.TyMeetingBean;
/**
 *   @author pengtao 
 */

public interface TyMeetingProvider extends BaseProvider<TyMeeting> {
	
	public PageInfo<TyMeetingBean> queryBeans(Map<String, Object> params);
	
	//其他自定义的方法
}
