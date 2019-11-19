package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.GoInformUser;
import org.ibase4j.core.base.BaseMapper;
import com.tfzq.pr.model.pr.GoInformUserBean;

/**
 *   @author yuzhitao 
 */
public interface GoInformUserMapper extends BaseMapper<GoInformUser> {
	public List<GoInformUserBean> selectByCondition(Map<String, Object> params) ;

	public void updateByInformId(Map<String, Object> params);

}