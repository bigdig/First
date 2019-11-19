package com.ddfc.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface BaseService<T extends BaseModel> {
	/**
	 * 根据条件查询所有记录
	 * @param params
	 * @return
	 */
    List<T> findByCondition(Map<String, Object> params);

    /**
     * 根据条件分页查询
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    PageInfo<T> findByPage(Map<String,Object> params);

    List<T> findAll();
    
    void insert(T person);

    int delete(String id);

	T selectByPrimaryKey(String id);

	int update(T record);
	
}