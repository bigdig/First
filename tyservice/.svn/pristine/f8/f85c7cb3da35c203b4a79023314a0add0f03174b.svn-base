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
import com.tfzq.ty.model.generator.TyMessagelog;
import com.tfzq.ty.model.ty.TyMessagelogBean;
import com.tfzq.ty.provider.TyMessagelogProvider;

/**
 * @author pengtao 
 */
@Service
public class TyMessagelogService extends BaseService<TyMessagelogProvider, TyMessagelog> {
	@Autowired
	public void setProvider(TyMessagelogProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyMessagelogBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

	public int batchAdd(List<TyMessagelog> mlog) {
		int batchSize = 500;
		List<TyMessagelog> tmp= new ArrayList<TyMessagelog>();
		for(int i=0;i<mlog.size();i++){
			tmp.add(mlog.get(i));
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

	public List<TyMessagelogBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

	public List<String> queryReceiverName(Map<String, Object> params) {
		return provider.queryReceiverName(params);
	}

}
