package org.ibase4j.dao.generator;

import java.util.List;

import org.ibase4j.core.base.BaseMapper;
import org.ibase4j.model.generator.PbPublishment;

/**
 * 由MyBatis Generator工具自动生成，请不要手动修改
 */
public interface PbPublishmentMapper extends BaseMapper<PbPublishment> {
	public void doPushTask();

	public List<PbPublishment> getPushDatas();

}