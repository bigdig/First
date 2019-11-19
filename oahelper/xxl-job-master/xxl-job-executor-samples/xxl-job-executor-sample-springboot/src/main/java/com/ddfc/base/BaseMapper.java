package com.ddfc.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;

public interface BaseMapper<T extends BaseModel> {

	int deleteByPrimaryKey(String id);

	int insert(T record);

	T selectByPrimaryKey(String id);

	int updateByPrimaryKey(T record);

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	List<T> selectAll();

	/**
	 * 分页查询数据
	 * 
	 * @return
	 */
	Page<T> findByCondition(Map<String,Object> params);
}
