package com.tfzq.generate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestGeneWord {
	public static String ftlDir = "template/main/";
	public static void main(String[] args) throws Exception {
//		String outFileName = "恒生PB系统产品报告表8.9" + ".doc";
		String outFileName = "授权书" + ".doc";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
//		Template template = cfg.getTemplate("恒生PB系统产品报告表8.9.xml");
		Template template = cfg.getTemplate("授权书.xml");
		// 建立数据模型（Map）
		Map<String, Object> root=new HashMap<String, Object>();
//		onlineRootInit(root);
		authRootInit(root);

		// 获取输出流（指定到控制台（标准输出））
		OutputStream out = new FileOutputStream(new File("D:/" + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}
	
	public static Map<String, Object> onlineRootInit(Map<String, Object> root) throws Exception {
		  root.put("opt_online_1", "你好");
		  root.put("opt_online_2", "你好");
		  root.put("opt_online_3", "你好");
		  root.put("opt_online_4", "你好");
		  root.put("opt_online_5", "你好");
		  root.put("opt_online_6", "你好");
		  root.put("opt_online_7", "你好");
		  root.put("opt_online_8", "你好");
		  root.put("opt_online_9", "你好");
		  root.put("opt_online_10", "你好");
		  root.put("opt_online_11", "你好");
		  root.put("opt_online_12", "你好");
		  root.put("opt_online_13", "你好");
		  root.put("opt_online_14", "你好");
		  root.put("opt_online_15", "你好");
		  root.put("opt_online_16", "你好");
		  root.put("opt_online_17", "你好");
		  root.put("opt_online_18", "你好");
		  root.put("opt_online_19", "你好");
		  root.put("opt_online_20", "你好");
		  root.put("opt_online_21", "你好");
		  root.put("opt_online_22", "你好");
		  root.put("opt_online_23", "你好");
		  root.put("opt_online_36", "你好");
		  root.put("opt_online_37", "你好");
		  root.put("opt_online_38", "你好");
		  root.put("opt_online_39", "你好");
		  root.put("opt_online_40", "你好");
		  root.put("opt_online_41", "你好");
		  root.put("opt_online_42", "你好");
		  root.put("opt_online_43", "你好");
		  root.put("opt_online_44", "你好");
		  root.put("opt_online_45", "你好");
		  root.put("opt_online_46", "你好");
		  root.put("opt_online_47", "你好");
		  root.put("opt_online_48", "你好");
		  root.put("opt_online_49", "你好");
		  root.put("opt_online_50", "你好");
		  root.put("opt_online_51", "你好");
		  root.put("opt_online_52", "你好");
		  root.put("opt_online_53", "你好");
		  
		  root.put("opt_online_55", "你好");
		  
		  root.put("opt_online_radio_1", "指令管理模式");
		  root.put("opt_online_radio_2", "不同意");
		  root.put("opt_online_radio_3", "不同意");
		  
		  root.put("opt_online_check_1", "1");
		  root.put("opt_online_check_2", "1");
		  root.put("opt_online_check_3", "0");
		  root.put("opt_online_check_4", "1");
		  root.put("opt_online_check_5", "0");
		  root.put("opt_online_check_6", "1");
		  root.put("opt_online_check_7", "0");
		  root.put("opt_online_check_8", "1");
		  root.put("opt_online_check_9", "0");
		  
		return root;
	}
	public static Map<String, Object> authRootInit(Map<String, Object> root) throws Exception {
		  root.put("opt_auth_1", "aaa公司");
		  root.put("opt_auth_2", "aaa公司");
		return root;
	}

}
