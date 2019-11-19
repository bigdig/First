/**
 * 
 */
package org.ibase4j.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.ibase4j.core.base.BaseExpandMapper;
import org.ibase4j.model.sys.SysUserBean;

/**
 * @author ShenHuaJie
 */
public interface SysUserExpandMapper extends BaseExpandMapper {

    String queryUserIdByThirdParty(@Param("provider") String provider, @Param("openId") String openId);

    List<SysUserBean> queryPbUser(Map<String, Object> params);
    
    List<String> queryDepUsers(Map<String, Object> params);

}
