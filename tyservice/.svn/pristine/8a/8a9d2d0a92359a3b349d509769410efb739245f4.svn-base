package com.tfzq.ty.provider;

import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseProvider;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyServiceorglabel;
import com.tfzq.ty.model.ty.TyServiceorglabelBean;
/**
 *   @author pengtao 
 */

public interface TyServiceorglabelProvider extends BaseProvider<TyServiceorglabel> {
	
	public PageInfo<TyServiceorglabelBean> queryBeans(Map<String, Object> params);

	public int deleteByOrgId(String orgId);

	public int deleteByLabelId(String labelId);

	public List<TyServiceorglabelBean> queryByOrgId(String orgId);
	
	//其他自定义的方法
}
