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
	//public static String designDir = "template/design/";
	public static String ftlDir = "template/main/";
	//public static String table.getCodeDir() = "D:\\codegene\\src\\";
	public static TableDesign table = null;

	public static void main(String[] args) throws Exception {
//		gene("inf_attachment.xls");
//		gene("inf_channel.xls");
//		gene("inf_upvote.xls");
//		gene("inf_homecontent.xls");
//		gene("inf_homesubject.xls");
//		gene("inf_channelclob.xls");
//		gene("inf_channelcontent.xls");
//		gene("inf_collect.xls");
//		gene("selectitem.xls");
//		gene("inf_category.xls");
//		gene("inf_comment.xls");
//		gene("inf_content.xls");
//		gene("inf_page.xls");
//		gene("inf_timepub2channel.xls");
//		gene("inf_appsecret.xls");
//		gene("inf_contentbody.xls");
//		gene("inf_subcontent.xls");
//		gene("inf_subject.xls");
//		gene("inf_editsection.xls");
//		gene("inf_channel.xls");
//		gene("inf_wechatlog.xls");
//		gene("signup.xls");
//		gene("ty_meeting.xls");
//		gene("ty_listedcompany.xls");
//		gene("ty_orgcustomer.xls");
//		gene("ty_customerlabel.xls");
//		gene("ty_custgroup.xls");
//		gene("ty_custgroupdetail.xls");
//		gene("ty_msgtemplate.xls");
//		gene("ty_appsecret.xls");
//		gene("ty_dcactivity.xls");
//		gene("ty_dcmorningcon.xls");
//		gene("ty_dcmorningstock.xls");
//		gene("ty_dcrecostock.xls");
//		gene("ty_dctopiccust.xls");
//		gene("ty_dctopic.xls");
//		gene("ty_orgprepcust.xls");
//		gene("ty_dcsalon.xls");
//		gene("ty_dcsaloncust.xls");
//		gene("ty_dccall.xls");
//		gene("ty_dccallcust.xls");
		
//		gene("ty_label.xls");
//		gene("ty_messagelog.xls");
//		gene("ty_serviceorg.xls");
//		gene("ty_stafflist.xls");
//		gene("sys_area.xls");
//		gene("sys_position.xls");
//		gene("sys_positionrole.xls");
//		gene("sys_rolebutton.xls");
//		gene("ty_activity.xls");
//		gene("ty_activitysign.xls");
//		gene("ty_activitystaff.xls");
//		gene("ty_expert.xls");
//		gene("ty_cfund.xls");
//		gene("ty_cfundstock.xls");
//		gene("ty_project.xls");
		gene("ty_activitystock.xls");
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
		//String className = StringUtils.substringBefore(file, ".");
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
//		root.put("className", table.getBeanName());
//		root.put("attrs", table.getFields());
		root.put("author", ploader.getAuthor());
		// 获取输出流（指定到控制台（标准输出））
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
		//扩展文件
		template = cfg.getTemplate("modelbean.ftl");
		root.put("packageName", ploader.getModelExpandPackageName());
		root.put("parentPackageName", ploader.getModelGeneratePackageName());
		outFileName = table.getBeanName() + "Bean.java";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
	}

	public static void geneProviderApi() throws Exception {
		String className = table.getBeanName() + "Provider";
		String beanObj = StringUtils.lowerCase(table.getBeanName().substring(0, 1))+table.getBeanName().substring(1);
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
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
	}
	
	public static void geneProviderImpl() throws Exception {
		String className = table.getBeanName() + "ProviderImpl";
		//String 
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
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
	}

	public static void geneMapper() throws Exception {
		String className = table.getBeanName() + "Mapper";
		//String beanObj = StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
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
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
		//扩展文件
		/**
		 * 
		template = cfg.getTemplate("mapperExpand.ftl");
		root.put("packageName", ploader.getMapperExpandPackageName());
		outFileName = table.getBeanName() + "ExpandMapper.java";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		 */

	}
	public static void geneMapperXml() throws Exception {
		String className = table.getBeanName() + "Mapper";
		//String beanObj = StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
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
		//root.put("tableName", table.getTableName());
		//root.put("className", className);
		//root.put("bean", table.getBeanName());
		//root.put("beanObj", table.getBeanObj());
		//root.put("attrs", table.getFields());
		// 获取输出流（指定到控制台（标准输出））
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
		//扩展文件
		/**
		 * 
		template = cfg.getTemplate("sql-mapperExpand.ftl");
		root.put("packageName", ploader.getMapperExpandPackageName());
		outFileName = table.getBeanName() + "ExpandMapper.xml";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		 */
	}

	
	public static void geneService() throws Exception {
		String className = table.getBeanName() + "Service";
		//String beanObj = StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
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
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		

	}
	public static void geneController() throws Exception {
		String className = table.getBeanName() + "Controller";
		//String beanObj = StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
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
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
		
	}
	public static void genePage() throws Exception {
		table.getBeanName();
		//String beanObj = StringUtils.lowerCase(beanName.substring(0, 1))+beanName.substring(1);
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
		//root.put("tableDesc", table.getTableDesc());
		root.put("author", ploader.getAuthor());
		//root.put("className", className);
		root.put("module", ploader.get("module"));
		//root.put("bean", table.getBeanName());
		//root.put("beanObj", table.getBeanObj());
		//root.put("attrs", table.getFields());
		// 获取输出流（指定到控制台（标准输出））
		OutputStream out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		
		//文件2
		template = cfg.getTemplate("xpage-listController.ftl");
		outFileName = "listController.js";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		//文件3
		template = cfg.getTemplate("xpage-update.ftl");
		outFileName = "update.html";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		//文件4
		template = cfg.getTemplate("xpage-updateController.ftl");
		outFileName = "updateController.js";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		//文件5
		template = cfg.getTemplate("xpage-view.ftl");
		outFileName = "view.html";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		//文件6
		template = cfg.getTemplate("xpage-viewController.ftl");
		outFileName = "viewController.js";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
		template.process(root, new OutputStreamWriter(out));
		out.flush();
		System.out.println("生成文件：" + outFileName);
		//文件7
		template = cfg.getTemplate("xpage-router.ftl");
		outFileName = "router.js";
		out = new FileOutputStream(new File(table.getCodeDir()
				+ outFileName));
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
