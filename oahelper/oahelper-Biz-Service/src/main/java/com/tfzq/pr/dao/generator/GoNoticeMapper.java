package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoNotice;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoNoticeBean;

/**
 *   @author yuzhitao 
 */
public interface GoNoticeMapper extends BaseMapper<GoNotice> {
	public List<GoNoticeBean> selectByCondition(Map<String, Object> params) ;

}