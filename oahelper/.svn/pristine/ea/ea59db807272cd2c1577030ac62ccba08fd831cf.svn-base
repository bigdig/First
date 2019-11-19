package com.tfzq.util;

import java.io.File;
import java.io.FilenameFilter;

public class CustomeFilenameFilter implements FilenameFilter{
	
	private String suffix;
	public CustomeFilenameFilter(String suffix){
		this.suffix = suffix;
	}
	public CustomeFilenameFilter(){
		this.suffix="txt";
	}
	
	/**
	 * @param args
	 * 
	 * 实现功能; 实现FilenameFilter接口,定义出指定的文件筛选器
	 * 
	 */
	@Override
	// 重写accept方法,测试指定文件是否应该包含在某一文件列表中
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		// 创建返回值
		boolean flag = true;
		// 定义筛选条件
		// endWith(String str);判断是否是以指定格式结尾的
		if (name.toLowerCase().endsWith(this.suffix)) {

		} else {
			flag = false;
		}
		// 返回定义的返回值
		// 当返回true时,表示传入的文件满足条件
		return flag;
	}
}
