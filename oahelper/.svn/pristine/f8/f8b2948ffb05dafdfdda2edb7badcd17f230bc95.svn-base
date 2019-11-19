package com.ddfc.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddfc.service.RedisUtil;
import com.ddfc.util.DateUtil;
import com.ddfc.util.DateUtil.DATE_PATTERN;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T extends BaseModel> implements BaseService<T>{
    @Autowired
    private RedisUtil redisService;

    @Autowired
    private BaseMapper<T> mapper;

	public BaseMapper<T> getMapper() {
		// TODO Auto-generated method stub
		return mapper;
	}

    
	@Override
	public List<T> findByCondition(Map<String, Object> params) {
		return mapper.findByCondition(params);
	}

	@Override
	public PageInfo<T> findByPage(Map<String, Object> params) {
		int pageNum = params.get("pageNum") == null ? 1 : Integer.parseInt((String.valueOf(params.get("pageNum"))));
		int pageSize = params.get("pageSize") == null ? 10 : Integer.parseInt((String.valueOf(params.get("pageSize"))));

		PageHelper.startPage(pageNum, pageSize);
		return new PageInfo<T>(mapper.findByCondition(params));
	}
	
	
	@Override
	public List<T> findAll() {
		return mapper.selectAll();
	}

	@Override
	@Transactional
	public T insert(T record) {
		if (StringUtils.isBlank(record.getId())) {
			String key = record.getClass().getSimpleName();
			record.setId(createId(key));
			record.setCreateTime(new Date());
			if(record.getUpdateTime()==null){
				record.setUpdateTime(new Date());
			}
		} else{
			if(record.getUpdateTime()==null){				
				record.setUpdateTime(new Date());
			}
		}

		mapper.insert(record);
		return record;
	}

	@Override
	@Transactional
	public int delete(String id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public T selectByPrimaryKey(String id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int update(T record) {
		return mapper.updateByPrimaryKey(record);
	}
	
    /** 生成主键策略 */
    public String createId(String key) {
        String redisKey = "REDIS_TBL_" + key;
        String dateTime = DateUtil.getDateTime(DATE_PATTERN.YYYYMMDDHHMMSSSSS);
        return dateTime + StringUtils.right(StringUtils.leftPad(String.valueOf(redisService.incr(redisKey)), 5, '0'), 5);
    }
    /** 生成主键策略 */
    public String createConcatId(String ...strings) {
    	return concatIds("-",strings);
    }
    /** 生成主键策略 */
    public String concatIds(String separator,String ...strings) {
    	StringBuffer sb = new StringBuffer();
    	for(String s:strings){
    		sb.append(s).append(separator);
    	}
    	sb.replace(sb.length()-separator.length(), sb.length(), "");
    	return sb.toString();
    }
}
