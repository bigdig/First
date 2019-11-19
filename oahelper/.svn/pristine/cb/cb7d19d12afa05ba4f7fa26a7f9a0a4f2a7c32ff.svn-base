package com.tfzq.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.util.PropertiesUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件传输控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "文件传输管理", description = "文件传输管理")
@RequestMapping(value = "pr/goUpload", method = RequestMethod.POST)
public class MyUploadController extends BaseController {

	// 上传文件(支持批量)
	@RequestMapping(value = "/inform", method = RequestMethod.POST)
	@ApiOperation(value = "上传图片")
	public Object uploadImage(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		return doUpload(request, response, modelMap, "inform_images");
	}

	@RequestMapping(value = "/fileArchive", method = RequestMethod.POST)
	@ApiOperation(value = "上传文件")
	public Object uploadFile(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		return doUpload(request, response, modelMap, "file_archives");
	}

	private Object doUpload(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			String path) {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");

		ServletContext servletContext = request.getServletContext();

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iterator = multiRequest.getFileNames();

			String relativeDir = PropertiesUtil.getString("machine.node") + File.separator +"go_files" + File.separator + path;

//			String pathDir = servletContext.getRealPath(File.separator + relativeDir);
			String pathDir = PropertiesUtil.getString("file.rootdir") + File.separator
					+  relativeDir;
			
			File dirFile = new File(pathDir);
			if (!dirFile.isDirectory()) {
				dirFile.mkdirs();
			}
			List<MyUploadFileBean> files = new ArrayList<MyUploadFileBean>();
			while (iterator.hasNext()) {
				String key = iterator.next();
				MultipartFile multipartFile = multiRequest.getFile(key);
				if (multipartFile != null) {

					String name = multipartFile.getOriginalFilename();
					String fileName = System.currentTimeMillis() + name.substring(name.indexOf("."), name.length());

					String filePath = pathDir + File.separator + fileName;
					MyUploadFileBean fb = null;
					File file = new File(filePath);
					file.setWritable(true, false);
					try {
						multipartFile.transferTo(file);
						String fileUrl = relativeDir + File.separator + fileName;
						fb = new MyUploadFileBean(name, fileUrl);
						files.add(fb);
					} catch (Exception e) {
						logger.error(fileName + "保存失败", e);
					}

				}
			}
			modelMap.put("files", files);
			return setSuccessModelMap(modelMap);
		} else {
			setModelMap(modelMap, HttpCode.BAD_REQUEST);
			modelMap.put("msg", "请选择要上传的文件！");
			return modelMap;
		}
	}
}
