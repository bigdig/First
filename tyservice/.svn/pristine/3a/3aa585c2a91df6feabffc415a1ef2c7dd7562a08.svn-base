package ${packageName};

import java.util.Map;

import org.ibase4j.core.base.BaseService;
//import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import ${beanGenPackage}.${bean};
import ${beanCusPackage}.${bean}Bean;
import ${apiPackage}.${bean}Provider;

/**
 * @author ${author} 
 */
@Service
public class ${bean}Service extends BaseService<${bean}Provider, ${bean}> {
	//@DubboReference
	@Autowired
	public void setProvider(${bean}Provider provider) {
		this.provider = provider;
	}
	
    public PageInfo<${bean}Bean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }

}
