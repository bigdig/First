package org.ibase4j.web.sys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.support.fastdfs.FileManager;
import org.ibase4j.core.support.fastdfs.FileModel;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hanvoncloud.HwCloud;
import com.hanvoncloud.common.LanguageEnum;
import com.tfzq.pb.model.generator.PbFile;
import com.tfzq.service.PbFileService;
import com.tfzq.util.HtmlUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件上传控制器
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:11:42
 */
@RestController
@Api(value = "文件上传接口", description = "文件上传接口")
@RequestMapping(value = "/uploadfile")
public class UploadController extends BaseController {
	@Autowired
	private PbFileService pbFileService;

	// 上传文件(支持批量)
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	@ApiOperation(value = "上传文件")
	public Object uploadFile(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		Map<String, String> fileNames = UploadUtil.uploadFile(request);
		if (fileNames.size() > 0) {
			List<PbFile> files = new ArrayList<PbFile>();
			for (Entry<String, String> entry : fileNames.entrySet()) {
				PbFile pbFile = new PbFile();
				pbFile.setActualName(entry.getKey());
				pbFile.setId(entry.getValue());

				// 上传到Fastdfs
				FileModel fileModel = UploadUtil.remove2DFS("pbfile", pbFile.getId(), UploadUtil.getUploadDir(request) + File.separator + pbFile.getId());
				pbFile.setGroupName(fileModel.getGroupName());
				pbFile.setFileName(fileModel.getRemoteFileName());
				pbFileService.add(pbFile);

				files.add(pbFile);
			}
			modelMap.put("files", files);
			return setSuccessModelMap(modelMap);
		} else {
			setModelMap(modelMap, HttpCode.BAD_REQUEST);
			modelMap.put("msg", "请选择要上传的文件！");
			return modelMap;
		}
	}


	@RequestMapping(value = "/down", method = RequestMethod.GET)
	@ApiOperation(value = "下载文件")
	public String downloadFile(String fileId, HttpServletResponse response, ModelMap modelMap) throws Exception {

		PbFile file = pbFileService.queryById(fileId);
		try {
			byte[] content = FileManager.downloadFile(file.getGroupName(), file.getFileName());
			OutputStream os = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(file.getActualName(), "UTF-8"));
			os.write(content);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}
	
	
	/**
	 * 识别名片
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ocrToCard",method = RequestMethod.POST)
	@ApiOperation(value = "识别名片")
	public Object toCard(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		Map<String, String> fileNames = UploadUtil.uploadFile(request);
		Map<String,Object> data = new HashMap<>();
		String srcFile="";
		if (fileNames.size() > 0) {
			for (Entry<String, String> entry : fileNames.entrySet()) {
				String id = entry.getValue();
				srcFile = UploadUtil.getUploadDir(request) + File.separator + id;
			}
			
			//您在汉王云开发者中心获取的key
			String key = PropertiesUtil.getString("hwocr.card.key");//"9f7bec55-2272-4eb6-aff5-cb12631a64f1";  
			//语言code，AUTO:自动识别语言且带坐标 CHNS:中文简体  CHNT：中文繁体  EN:英文  
			String language = LanguageEnum.AUTO.getCode();

			String result = HwCloud.getInstance().recgBcard(key, language, srcFile);
			JSONObject obj = JSON.parseObject(result);
			if(!StringUtils.equals(obj.getString("code"), "0")){
				String msg = obj.getString("result");
				throw new IllegalParameterException(msg);
			}
			
			return setSuccessModelMap(modelMap,obj);
		} else {
			setModelMap(modelMap, HttpCode.BAD_REQUEST);
			modelMap.put("msg", "请选择要上传的文件！");
			return modelMap;
		}
	}
	// 上传活动文件(支持批量)
	@RequestMapping(value = "/activityfile",method = RequestMethod.POST)
	@ApiOperation(value = "上传活动文件")
	public Object uploadActFile(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		Map<String, String> fileNames = UploadUtil.uploadFile(request);
		Map<String,Object> data = new HashMap<>();
		String addAttachs = "";
		String addAttachNames = "";
		String tempDestDir = "";
		if (fileNames.size() > 0) {
			for (Entry<String, String> entry : fileNames.entrySet()) {
				String id = entry.getValue();
				File srcFile = new File(UploadUtil.getUploadDir(request) + File.separator + id);
				tempDestDir = HtmlUtils.getActivityAttachFullFilePath();
				addAttachs += (StringUtils.isEmpty(addAttachs) ? ( HtmlUtils.getActivityAttachRelativeFilePath() + File.separator + id ): (","+HtmlUtils.getActivityAttachRelativeFilePath() + File.separator + id));
				addAttachNames += (StringUtils.isEmpty(addAttachNames) ? entry.getKey() : (","+entry.getKey()));
				File destDir = new File(tempDestDir);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			data.put("addAttachs", addAttachs);
			data.put("addAttachNames", addAttachNames);
			return setSuccessModelMap(modelMap,data);
		} else {
			setModelMap(modelMap, HttpCode.BAD_REQUEST);
			modelMap.put("msg", "请选择要上传的文件！");
			return modelMap;
		}
	}
	
	@RequestMapping(value = "/downActFile", method = RequestMethod.GET)
	@ApiOperation(value = "下载文件")
	public String downActFile(String fileUrl, String fileName, HttpServletResponse response, ModelMap modelMap) throws Exception {
		String filePath = PropertiesUtil.getString("ty.file.rootdir") + fileUrl;

		try {
			byte[] content = FileUtils.readFileToByteArray(new File(filePath));
			OutputStream os = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8"));
			os.write(content);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}
}
