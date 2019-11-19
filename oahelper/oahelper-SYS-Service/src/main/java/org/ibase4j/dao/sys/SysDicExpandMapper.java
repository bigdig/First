package org.ibase4j.dao.sys;

import java.util.List;
import java.util.Map;

import org.ibase4j.model.generator.SysDic;

import com.github.pagehelper.Page;

public interface SysDicExpandMapper {

	Page<String> queryDicIndex(Map<String, Object> params);

	Page<String> queryDic(Map<String, Object> params);

	List<SysDic> selectByIndexId(String id);

}
