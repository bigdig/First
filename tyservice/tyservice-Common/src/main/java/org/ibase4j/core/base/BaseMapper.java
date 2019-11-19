package org.ibase4j.core.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;

/**
 * @author ShenHuaJie
 * @version 2016年6月3日 下午2:30:14
 */
public interface BaseMapper<T extends BaseModel> {
	List<T> selectAll();

	int deleteByPrimaryKey(String id);

	T selectByPrimaryKey(String id);

	int insert(T record);

	int updateByPrimaryKey(T record);
	
	/** 条件分页查询 */
	Page<String> queryIds(Map<String, Object> params);

}
