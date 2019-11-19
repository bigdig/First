package ${packageName};

import java.util.Map;
import java.util.List;

import org.ibase4j.core.base.BaseProviderImpl;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import ${beanGenPackage}.${bean};
import ${beanCusPackage}.${bean}Bean;
import ${apiPackage}.${bean}Provider;
import ${mapperPackage}.${bean}Mapper;

/**
 *   @author ${author} 
 */
@CacheConfig(cacheNames = "${beanObj}")
//@DubboService(interfaceClass = ${bean}Provider.class)
@Service
public class ${bean}ProviderImpl extends BaseProviderImpl<${bean}> implements ${bean}Provider {
	//@Autowired
	//private ${bean}ExpandMapper ${beanObj}ExpandMapper;

	public PageInfo<${bean}> query(Map<String, Object> params) {
		this.startPage(params);
		return getPage(getMapper().queryIds(params));
	}
	
	public PageInfo<${bean}Bean> queryBeans(Map<String, Object> params) {
		this.startPage(params);
		Page<String> userIds = getMapper().queryIds(params);
		PageInfo<${bean}Bean> pageInfo = getPage(userIds, ${bean}Bean.class);
		return pageInfo;
	}

	public List<${bean}Bean> queryByCondition(Map<String, Object> params) {
		List<${bean}Bean> results = ((${bean}Mapper)getMapper()).selectByCondition(params);
		return results;
	}

}
