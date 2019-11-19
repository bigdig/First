package org.ibase4j.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.support.HttpCode;
import org.ibase4j.core.support.fastdfs.FileManager;
import org.ibase4j.core.support.fastdfs.FileModel;
import org.ibase4j.core.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tfzq.pb.model.generator.PbFile;
import com.tfzq.service.PbFileService;

/**
 * 文件上传控制器
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:11:42
 */
@RestController
@Api(value = "文件上传接口", description = "文件上传接口")
@RequestMapping(value = "/bizspace/uploadfile")
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
	@ApiOperation(value = "上传文件")
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
}
