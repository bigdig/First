package org.ibase4j.core.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ibase4j.core.Constants;
import org.ibase4j.core.support.Assert;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.core.util.WebUtil;
import com.github.pagehelper.PageInfo;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:47:58
 */
public abstract class BaseService<P extends BaseProvider<T>, T extends BaseModel> {
    protected Logger logger = LogManager.getLogger();
    protected P provider;
    /** 修改 */
    public void update(T record) {
        record.setUpdateBy(WebUtil.getCurrentUser()==null?"visitor":WebUtil.getCurrentUser());
        Assert.isNotBlank(record.getId(), "ID");
        provider.update(record);
    }

    /** 新增 */
    public T add(T record) {
        String uid = WebUtil.getCurrentUser();
        if (StringUtils.isBlank(record.getCreateBy())) {
            record.setCreateBy(uid == null ? "" : uid);
        }
        if (StringUtils.isBlank(record.getUpdateBy())) {
            record.setUpdateBy(uid == null ? "" : uid);
        } else if (StringUtils.isNotBlank(uid)) {
            record.setUpdateBy(uid);
        }
        T providerRecord = provider.add(record);
        return providerRecord;
    }

    /** 逻辑删除 */
    public void delete(String id) {
        Assert.isNotBlank(id, "ID");
        provider.delete(id, WebUtil.getCurrentUser());
    }
    /*物理删除*/
    public void deletePhysical(String id) {
    	Assert.isNotBlank(id, "ID");
    	provider.deletePhysical(id);
    }

    /** 根据Id查询 */
    @SuppressWarnings("unchecked")
    public T queryById(String id) {
        Assert.isNotBlank(id, "ID");
        StringBuilder sb = getCachePrefix();
        sb.append(":").append(id);
        T record = (T)RedisUtil.get(sb.toString());
        if (record == null) {
            record = provider.queryById(id);
        }
        return record;
    }
    /**
     * 缓存前缀
     * @return
     */
    public StringBuilder getCachePrefix(){
        StringBuilder sb = new StringBuilder(Constants.CACHE_NAMESPACE);
        String className = this.getClass().getSimpleName().replace("Service", "");
        sb.append(className.substring(0, 1).toLowerCase()).append(className.substring(1));
        return sb;
    }
    /**
     * 批量删除缓存 
     */
	public void batchDelCache() {
		String pre_str = getCachePrefix().toString();
		Set<Serializable> set = RedisUtil.keys(pre_str + "*");
		Iterator<Serializable> it = set.iterator();
		while (it.hasNext()) {
			String keyStr = (String) it.next();
			RedisUtil.del(keyStr);
		}
	}

	public List<T> getAllRecords() {
		return provider.getAllRecords();
	}

    /** 条件查询 */
    public PageInfo<T> query(Map<String, Object> params) {
    	if( provider == null) logger.error("error null");
        return provider.query(params);
    }
}
