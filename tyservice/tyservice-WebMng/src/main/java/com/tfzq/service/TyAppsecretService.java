package com.tfzq.service;

import java.util.Date;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.support.security.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyAppsecret;
import com.tfzq.ty.model.ty.TyAppsecretBean;
import com.tfzq.ty.provider.TyAppsecretProvider;

/**
 * @author pengtao 
 */
@Service
public class TyAppsecretService extends BaseService<TyAppsecretProvider, TyAppsecret> {
	@Autowired
	public void setProvider(TyAppsecretProvider provider) {
		this.provider = provider;
	}
	
    public PageInfo<TyAppsecretBean> queryBeans(Map<String, Object> params) {
        return provider.queryBeans(params);
    }
    
    public TyAppsecret queryByPlatSrc(String p) {
		return provider.queryByPlatSrc(p);
	}
    
    public static void main(String[] args) {
/*		Date date = new Date(1498618687411L);
		Date now = new Date();
		System.out.println(date.getTime());
		System.out.println(now.getTime());
		if(Math.abs(date.getTime()-now.getTime())<1000*60*5L){
			System.out.println("在当前时间范围内");
		}else{
			System.out.println("在当前时间范围外");
		}*/
		String url = "/openapi/read/getLastestStaff";
		Date date = new Date();
		System.out.println(date.getTime());
		String timestmap = String.valueOf(date.getTime());
		System.out.println(timestmap);
		String secretCode = "tymeeting$2017";
		String md = Md5Util.getMD5(url + timestmap + secretCode);
		System.out.println(md);
	}

}
