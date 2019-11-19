package com.tfzq.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author pengtao
 *
 */
public class CodeGene {
	private static GeneProperty ploader = new GeneProperty();
	// public static String designDir = "template/design/";
	public static String ftlDir = "template/main/";
	// public static String table.getCodeDir() = "D:\\codegene\\src\\";
	public static TableDesign table = null;

	public static void main(String[] args) throws Exception {
//		gene("go_user_favorite.xls");
//		gene("go_inform.xls");
//		gene("go_inform_user.xls");
//		gene("go_group.xls");
//		gene("go_user_group.xls");
//		gene("go_notice.xls");
//		gene("go_morning_meeting.xls");
//		gene("go_user_morning_meeting.xls");
//		gene("go_file_archive.xls");
//		gene("go_remind.xls");
//		gene("go_remind_inform.xls");
//		gene("go_card_apply.xls");
//		gene("go_vote_group.xls");
//		gene("go_vote.xls");
//		gene("go_vote_item.xls");
		gene("go_vote_user.xls");
//		gene("go_audity.xls");
//		gene("go_audity_record.xls");
	}

	public static void initGene(String file) {
		// 导入配置
		try {
			ploader.load(new FileInputStream("template/gene.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 解析CSV
		table = ExcelUtils.readXls(file);
		// 输出目录
		File dir = new File(table.getCodeDir());
		dir.mkdirs();
	}

	public static void geneBean() throws Exception {
		// String className = StringUtils.substringBefore(file, ".");
		String outFileName = table.getBeanName() + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("model.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getModelGeneratePackageName());
		root.put("table", table);
		// root.put("className", table.getBeanName());
		// root.put("attrs", table.getFields());
		root.put("author", ploader.getAuthor());
		// 获取输出流（指定到控制台（标准输出））

		OutputStream out = new FileOutputStream(new File(table.getCodeDir() + outFileName));
		// OutputStream out = new FileOutputStream(new File(table.getCodeDir() +
		// outFileName));

		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

		// 扩展文件
		template = cfg.getTemplate("modelbean.ftl");
		root.put("packageName", ploader.getModelExpandPackageName());
		root.put("parentPackageName", ploader.getModelGeneratePackageName());
		outFileName = table.getBeanName() + "Bean.java";
		new File(table.getCodeDir() + "bean" + File.separator).mkdirs();
		out = new FileOutputStream(new File(table.getCodeDir() + "bean" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
	}

	public static void geneProviderApi() throws Exception {
		String className = table.getBeanName() + "Provider";
		String beanObj = StringUtils.lowerCase(table.getBeanName().substring(0, 1)) + table.getBeanName().substring(1);
		String outFileName = className + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("provider.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getProviderApiPackageName());
		root.put("author", ploader.getAuthor());
		root.put("className", className);
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("bean", table.getBeanName());
		root.put("beanObj", beanObj);
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "provider" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(
				new File(table.getCodeDir() + "provider" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}

	public static void geneProviderImpl() throws Exception {
		String className = table.getBeanName() + "ProviderImpl";
		// String
		String outFileName = className + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("providerImpl.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getProviderImplPackageName());
		root.put("author", ploader.getAuthor());
		root.put("className", className);
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("apiPackage", ploader.getProviderApiPackageName());
		root.put("mapperPackage", ploader.getMapperGeneratePackageName());
		root.put("bean", table.getBeanName());
		root.put("beanObj", table.getBeanObj());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "providerImpl" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(
				new File(table.getCodeDir() + "providerImpl" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}

	public static void geneMapper() throws Exception {
		String className = table.getBeanName() + "Mapper";
		// String beanObj = StringUtils.lowerCase(beanName.substring(0,
		// 1))+beanName.substring(1);
		String outFileName = className + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("mapper.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getMapperGeneratePackageName());
		root.put("author", ploader.getAuthor());
		root.put("className", className);
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("bean", table.getBeanName());
		root.put("beanObj", table.getBeanObj());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "mapper" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(new File(table.getCodeDir() + "mapper" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

		// 扩展文件
		/**
		 * 
		 * template = cfg.getTemplate("mapperExpand.ftl"); root.put("packageName",
		 * ploader.getMapperExpandPackageName()); outFileName = table.getBeanName() +
		 * "ExpandMapper.java"; out = new FileOutputStream(new File(table.getCodeDir() +
		 * outFileName)); template.process(root, new OutputStreamWriter(out));
		 * out.flush(); System.out.println("生成文件：" + outFileName);
		 */

	}

	public static void geneMapperXml() throws Exception {
		String className = table.getBeanName() + "Mapper";
		// String beanObj = StringUtils.lowerCase(beanName.substring(0,
		// 1))+beanName.substring(1);
		String outFileName = className + ".xml";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("sql-mapper.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getMapperGeneratePackageName());
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("author", ploader.getAuthor());
		root.put("table", table);
		// root.put("tableName", table.getTableName());
		// root.put("className", className);
		// root.put("bean", table.getBeanName());
		// root.put("beanObj", table.getBeanObj());
		// root.put("attrs", table.getFields());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "mapperXML" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(
				new File(table.getCodeDir() + "mapperXML" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

		// 扩展文件
		/**
		 * 
		 * template = cfg.getTemplate("sql-mapperExpand.ftl"); root.put("packageName",
		 * ploader.getMapperExpandPackageName()); outFileName = table.getBeanName() +
		 * "ExpandMapper.xml"; out = new FileOutputStream(new File(table.getCodeDir() +
		 * outFileName)); template.process(root, new OutputStreamWriter(out));
		 * out.flush(); System.out.println("生成文件：" + outFileName);
		 */
	}

	public static void geneService() throws Exception {
		String className = table.getBeanName() + "Service";
		// String beanObj = StringUtils.lowerCase(beanName.substring(0,
		// 1))+beanName.substring(1);
		String outFileName = className + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("service.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getServicePackageName());
		root.put("author", ploader.getAuthor());
		root.put("className", className);
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("apiPackage", ploader.getProviderApiPackageName());
		root.put("bean", table.getBeanName());
		root.put("beanObj", table.getBeanObj());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "service" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(
				new File(table.getCodeDir() + "service" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}

	public static void geneController() throws Exception {
		String className = table.getBeanName() + "Controller";
		// String beanObj = StringUtils.lowerCase(beanName.substring(0,
		// 1))+beanName.substring(1);
		String outFileName = className + ".java";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("controller.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getControllerPackageName());
		root.put("table", table);
		root.put("tableDesc", table.getTableDesc());
		root.put("author", ploader.getAuthor());
		root.put("className", className);
		root.put("module", ploader.get("module"));
		root.put("beanGenPackage", ploader.getModelGeneratePackageName());
		root.put("beanCusPackage", ploader.getModelExpandPackageName());
		root.put("servicePackage", ploader.getServicePackageName());
		root.put("bean", table.getBeanName());
		root.put("beanObj", table.getBeanObj());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "controller" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(
				new File(table.getCodeDir() + "controller" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}

	public static void genePage() throws Exception {
		table.getBeanName();
		// String beanObj = StringUtils.lowerCase(beanName.substring(0,
		// 1))+beanName.substring(1);
		String outFileName = "list.html";

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		cfg.setDirectoryForTemplateLoading(new File(ftlDir));
		// 获取模板（template）
		Template template = cfg.getTemplate("xpage-list.ftl");
		// 建立数据模型（Map）
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", ploader.getControllerPackageName());
		root.put("table", table);
		// root.put("tableDesc", table.getTableDesc());
		root.put("author", ploader.getAuthor());
		// root.put("className", className);
		root.put("module", ploader.get("module"));
		// root.put("bean", table.getBeanName());
		// root.put("beanObj", table.getBeanObj());
		// root.put("attrs", table.getFields());
		// 获取输出流（指定到控制台（标准输出））
		new File(table.getCodeDir() + "page" + File.separator).mkdirs();
		OutputStream out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

		// 文件2
		template = cfg.getTemplate("xpage-listController.ftl");
		outFileName = "listController.js";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		// 文件3
		template = cfg.getTemplate("xpage-update.ftl");
		outFileName = "update.html";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		// 文件4
		template = cfg.getTemplate("xpage-updateController.ftl");
		outFileName = "updateController.js";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		// 文件5
		template = cfg.getTemplate("xpage-view.ftl");
		outFileName = "view.html";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		// 文件6
		template = cfg.getTemplate("xpage-viewController.ftl");
		outFileName = "viewController.js";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		// 文件7
		template = cfg.getTemplate("xpage-router.ftl");
		outFileName = "router.js";
		out = new FileOutputStream(new File(table.getCodeDir() + "page" + File.separator + outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);

	}

	public static void geneBatch() throws Exception {
		File fDir = new File(ExcelUtils.designDir);
		System.out.println("设计目录：" + fDir.getAbsolutePath());
		for (String file : fDir.list()) {
			gene(file);
		}
	}

	public static void gene(String file) throws Exception {
		initGene(file);
		geneBean();
		geneProviderApi();
		geneProviderImpl();
		geneMapper();
		geneMapperXml();
		geneService();
		geneController();
		genePage();
	}

}
