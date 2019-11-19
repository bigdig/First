package org.ibase4j.web.sys;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.support.ftp.SftpClient;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.UploadUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysResource;
import org.ibase4j.model.sys.SysResourceBean;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.service.sys.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;

/**
 * 静态资源控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "静态资源管理", description = "静态资源管理")
@RequestMapping(value = "sys/sysResource", method = RequestMethod.POST)
public class SysResourceController extends BaseController {
    private static final String TEMPLATE_DIR = "/WEB-INF/classes/template/";
	@Autowired
	private SysResourceService sysResourceService;
	@Autowired
	private SysDicService sysDicService;
	private static Map<String,SftpClient> sFtpClientMap = new HashMap<String,SftpClient>();

	// 查询静态资源
	@ApiOperation(value = "查询静态资源")
	@RequiresPermissions("sys.sysResource.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "resource_type,id desc");
		PageInfo<SysResourceBean> list = sysResourceService.queryBeans(params);

		Map<String, List<DictItem>> dicts = null;
		// 添加字典翻译
		Map<String, String> resourcetypeMap = sysDicService.queryDicByDicIndexKey("RESOURCETYPE");
		for (SysResourceBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getResourceType())) {
				singleBean.setResourceTypeText(resourcetypeMap.get(singleBean.getResourceType().toString()));
			}
		}
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "静态资源详情")
	@RequiresPermissions("sys.sysResource.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysResourceBean sysResourceBean = new SysResourceBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			SysResource record = sysResourceService.queryById(id);
			try {
				BeanUtils.copyProperties(sysResourceBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> resourcetypeMap = sysDicService.queryDicByDicIndexKey("RESOURCETYPE");
		addDictFromMap(dicts, "RESOURCETYPE", resourcetypeMap);
		if (sysResourceBean != null) {
			if (StringUtils.isNotEmpty(sysResourceBean.getResourceType())) {
				sysResourceBean.setResourceTypeText(resourcetypeMap.get(sysResourceBean.getResourceType().toString()));
			}
		}

		return setSuccessModelMap(modelMap, sysResourceBean, dicts);
	}

	// 新增静态资源
	@ApiOperation(value = "添加静态资源")
	@RequiresPermissions("sys.sysResource.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		SysResource record = Request2ModelUtil.covert(SysResource.class, request);
		if (record.getResourceType().equals("1")||record.getResourceType().equals("2")||
				record.getResourceType().equals("3")||record.getResourceType().equals("4")){
			record.setId(record.getResourceType());
		}
		sysResourceService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改静态资源
	@ApiOperation(value = "修改静态资源")
	@RequiresPermissions("sys.sysResource.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysResource record = Request2ModelUtil.covert(SysResource.class, request);
		sysResourceService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改静态资源
	@ApiOperation(value = "修改静态资源")
	@RequiresPermissions("sys.sysResource.update")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Object upload(MultipartHttpServletRequest request, ModelMap modelMap)  {
		
		String id = request.getParameter("id");
		Map<String, String> fileNames = UploadUtil.uploadFile(request);
		String fileActualName = fileNames.keySet().toArray()[0].toString();
		String fileName = fileNames.values().toArray()[0].toString();
		SysResource record = sysResourceService.queryById(id);
		if (record.getResourceType().equals("0")) {
			// 静态直接上传覆盖
			String src = UploadUtil.getUploadDir(request) + File.separator + fileName;
			transferFileByFtp(record,src);
		}else if (record.getResourceType().equals("1")||record.getResourceType().equals("2")||
				record.getResourceType().equals("3")||record.getResourceType().equals("4")){
			//恒生、讯投程序
			record.setResourceFile(fileActualName);
			record.setUpdateTime(new Date());
			String src = UploadUtil.getUploadDir(request) + File.separator + fileName;
			transferFileByFtp(record,src);
			//根据freemaker生成html片段
			String templateDir = UploadUtil.getUploadDir(request,TEMPLATE_DIR);
			String templateFile = "download.ftl";
			String targetFile = UploadUtil.getUploadDir(request) + File.separator + "download.html";
			Map<String, Object> params = generateSoftConfig(record);
			generateFile(params , templateDir,templateFile,targetFile);
			SysResource temp = new SysResource();
			temp.setResourcePath(PropertiesUtil.getString("webfile.softpage.dir"));
			temp.setResourceFile("download.html");
			transferFileByFtp(temp,targetFile);
			
			sysResourceService.update(record);
			
		}
		return setSuccessModelMap(modelMap);
	}
	
	/**
	 * 生成软件配置参数
	 * @param currentRecord
	 * @return
	 */
	private Map<String, Object> generateSoftConfig(SysResource currentRecord){
		Map<String, Object> params = new HashMap<String, Object>();
		String[] softIds = {"1","2","3","4"};
		for(String id:softIds){
			if(!StringUtils.equals(id, currentRecord.getId())){
				SysResource temp = sysResourceService.queryById(id);
				if(temp!=null){
					params.put("date_"+id, DateUtil.format(temp.getUpdateTime()));
					params.put("name_"+id, temp.getResourceFile());
				}
			}
		}
		params.put("date_"+currentRecord.getId(), DateUtil.format(currentRecord.getUpdateTime()));
		params.put("name_"+currentRecord.getId(), currentRecord.getResourceFile());
		return params;
	}

	
	/**
	 * 根据freemaker生成相应文件
	 * @param params
	 * @param templateFile
	 * @param targetFile
	 */
	private void generateFile(Map<String, Object> params ,String templateDir,String templateFile,String targetFile){
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

		// 2.设置模板文件目录
		try {
			File dir = new File(templateDir);
			cfg.setDirectoryForTemplateLoading(dir);
			// 获取模板（template）
			Template template = cfg.getTemplate(templateFile);
			// 获取输出流（指定到控制台（标准输出））
			OutputStream out = new FileOutputStream(new File(targetFile));
			template.process(params, new OutputStreamWriter(out));
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalParameterException("配置错误，无法根据模板生成相应文件！");
		}

	}
	
	private void transferFileByFtp(SysResource record,String srcFile){
		List<SftpClient> webHosts;
		try {
			webHosts = initFtpClients();
		} catch (JSchException e) {
			throw new IllegalParameterException("没有配置目标Web服务器，无法进行同步！");
		}
		String dst = record.getResourcePath() + record.getResourceFile();
		for (SftpClient ftp : webHosts) {
			ftp.put(srcFile, dst);
			ftp.disconnect();
		}

	}
	
	private List<SftpClient> initFtpClients() throws JSchException{
		String hostConfigs = PropertiesUtil.getString("webfile.sftp.hosts");
		String[] hosts = hostConfigs.split(",");
		List<SftpClient> webHosts = new ArrayList<SftpClient>();
		for (String host : hosts) {
			String ipAndPort = StringUtils.substringBefore(host, "@");
			String userAndPass = StringUtils.substringAfter(host, "@");
			String ip = StringUtils.substringBefore(ipAndPort, ":");
			int port = Integer.parseInt(StringUtils.substringAfter(ipAndPort, ":"));
			String userName = StringUtils.substringBefore(userAndPass, ":");
			String password = StringUtils.substringAfter(userAndPass, ":");
			SftpClient existFtp = sFtpClientMap.get(ipAndPort);
			if (existFtp!=null){
				existFtp.connect();
				webHosts.add(existFtp);
			}else{
				SftpClient ftp = new SftpClient(ip, port, userName, password);
				webHosts.add(ftp);
			}
		}
		return webHosts;
	}

	// 删除静态资源
	@ApiOperation(value = "删除静态资源")
	@RequiresPermissions("sys.sysResource.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		SysResource record = sysResourceService.queryById(id);
		if (record != null) {
			sysResourceService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}
}
