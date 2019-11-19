package com.tfzq.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.core.util.DateUtil.DATE_PATTERN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyCustgroupdetail;
import com.tfzq.ty.model.ty.TyCustgroupdetailBean;
import com.tfzq.ty.provider.TyCustgroupdetailProvider;

/**
 * @author pengtao 
 */
@Service
public class TyCustgroupdetailService extends BaseService<TyCustgroupdetailProvider, TyCustgroupdetail> {
	@Autowired
	public void setProvider(TyCustgroupdetailProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyCustgroupdetailBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public List<String> getCustIdByGroupId(String groupId) {
		return provider.getCustIdByGroupId(groupId);
	}

	public void deleteByGroupId(String groupId) {
		provider.deleteByGroupId(groupId);
	}
    public List<TyCustgroupdetailBean> queryByCondition(Map<String, Object> params) {
    	List<TyCustgroupdetailBean> custlist =null;
    	if(params.get("custIds")!=null){
    		custlist = new ArrayList<TyCustgroupdetailBean>();
    		List<String> arrayList =(List<String>) params.get("custIds");
    		List<String> tempList = new ArrayList<String>();
    		for(int i=0;i<arrayList.size();i++){
    			tempList.add(arrayList.get(i));
    			if(i%500==0){
    				params.put("custIds", tempList);
    				custlist.addAll(provider.queryByCondition(params));
    				tempList.clear();
    			}
    			if(i==arrayList.size()-1&&tempList.size()!=0){
    				params.put("custIds", tempList);
    				custlist.addAll(provider.queryByCondition(params));
    				tempList.clear();
    			}
    		}
    	}else{
    		custlist=provider.queryByCondition(params);
    	}
        return custlist;
    }

	public int batchAdd(List<TyCustgroupdetail> groupDetailList) {
		int batchSize = 500;
		List<TyCustgroupdetail> tmp= new ArrayList<TyCustgroupdetail>();
		for(int i=0;i<groupDetailList.size();i++){
			tmp.add(groupDetailList.get(i));
			//每500条提交一次
			if(tmp.size() == batchSize){
				provider.batchAdd(tmp);
				tmp.clear();
			}
		}
		//最后提交一次
		if(tmp.size()>0){
			provider.batchAdd(tmp);
			tmp.clear();
		}
		return 0;
	}
	
	public String createId(String key) {
        String redisKey = "REDIS_TBL_" + key;
        String dateTime = DateUtil.getDateTime(DATE_PATTERN.YYYYMMDDHHMMSSSSS);
        return dateTime + StringUtils.right(StringUtils.leftPad(String.valueOf(RedisUtil.incr(redisKey)), 5, '0'), 5);
    }


}
