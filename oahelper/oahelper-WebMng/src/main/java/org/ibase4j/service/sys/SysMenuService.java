package org.ibase4j.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ibase4j.core.base.BaseService;
import org.ibase4j.model.generator.SysMenu;
import org.ibase4j.provider.SysMenuProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:16:07
 */
@Service
public class SysMenuService extends BaseService<SysMenuProvider, SysMenu> {
	@Autowired
    public void setProvider(SysMenuProvider provider) {
        this.provider = provider;
    }

    public List<Map<String, String>> getPermissions() {
        return provider.getPermissions();
    }
    
    public List<SysMenu> getAllList(){
    	return provider.getAllList();
    }
    
    public List<SysMenu> getAllListSorted(){
    	return provider.getAllListSorted();
    }
    
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("保护环境"); // 向列表中添加数据
		list.add("爱护地球"); // 向列表中添加数据
		list.add("从我做起"); // 向列表中添加数据
		list.add(1, "从我做起"); // 在第1+1个元素的位置添加数据
		List<String> list_ad = new ArrayList<String>();
		list_ad.add("公益广告1");
		list_ad.add("公益广告2");
		list_ad.add("公益广告3");
		// 将list中的全部元素添加到list_ad中
		System.out.println("添加是否成功：" + list_ad.addAll(1, list));
		// 通过循环输出列表中的内容
		for (int i = 0; i < list_ad.size(); i++) {
			System.out.println(i + ":" + list_ad.get(i));
		}
	}

}
