package org.ibase4j.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.config.Resources;
import org.ibase4j.core.support.Assert;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysDic;
import org.ibase4j.model.generator.SysDicIndex;
import org.ibase4j.provider.SysDicProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

@Service
public class SysDicService {
	@Autowired
	private SysDicProvider provider;
	@Autowired
	private SysDicService sysDicService;

	public PageInfo<SysDicIndex> queryDicIndex(Map<String, Object> params) {
		return provider.queryDicIndex(params);
	}

	public PageInfo<SysDic> queryDic(Map<String, Object> params) {
		return provider.queryDic(params);
	}

	public void addDicIndex(SysDicIndex record) {
		record.setCreateBy(WebUtil.getCurrentUser());
		provider.updateDicIndex(record);
	}

	public void updateDicIndex(SysDicIndex record) {
		Assert.isNotBlank(record.getId(), Resources.getMessage("ID_IS_NULL"));
		record.setUpdateBy(WebUtil.getCurrentUser());
		provider.updateDicIndex(record);
	}

	public List<SysDic> selectByIndexId(String id) {
		Assert.isNotBlank(id, Resources.getMessage("ID_IS_NULL"));
	   return provider.selectByIndexId(id);
	}
	
	public void deleteDicIndex(String id) {
		Assert.isNotBlank(id, Resources.getMessage("ID_IS_NULL"));
		provider.deleteDicIndex(id);
	}

	public void addDic(SysDic record) {
		record.setCreateBy(WebUtil.getCurrentUser());
		record.setUpdateBy(WebUtil.getCurrentUser());
		provider.updateDic(record);
	}

	public void updateDic(SysDic record) {
		Assert.isNotBlank(record.getId(), Resources.getMessage("ID_IS_NULL"));
		record.setUpdateBy(WebUtil.getCurrentUser());
		provider.updateDic(record);
	}

	public void deleteDic(String id) {
		Assert.isNotBlank(id, Resources.getMessage("ID_IS_NULL"));
		provider.deleteDic(id);
	}
	
	public void deleteByIndexId(String id) {
		Assert.isNotBlank(id, Resources.getMessage("ID_IS_NULL"));
		provider.deleteByIndexId(id);
	}

	public SysDicIndex queryDicIndexById(String id) {
		return provider.queryDicIndexById(id);
	}

	public SysDic queryDicById(String id) {
		return provider.queryDicById(id);
	}
	//以code,text存储Map字典
	public Map<String, String> queryDicByDicIndexKey(String key) {
		Assert.notNull(key, Resources.getMessage("KEY_IS_NULL"));
		return provider.queryDicByDicIndexKey(key);
	}
	//以text,code存储Map字典
	public Map<String, String> queryDicByDicIndexKeyContrary(String key) {
		Assert.notNull(key, Resources.getMessage("KEY_IS_NULL"));
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> map = sysDicService.queryDicByDicIndexKey(key);
		for(String newKey : map.keySet()){
			resultMap.put(map.get(newKey), newKey);
		}
		return resultMap;
	}
	public void clearDicCache(String dicIndexId) {
		provider.clearDicCache(dicIndexId);
	}
}
