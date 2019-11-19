package com.tfzq.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ibase4j.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 客户标签控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "客户标签管理", description = "客户标签管理")
@RequestMapping(value = "ty/tyCustomerlabel", method = RequestMethod.POST)
public class TyCustomerlabelController extends BaseController {
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private TyLabelService tyLabelService;

	// 查询客户标签
	@ApiOperation(value = "查询客户标签")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "customerId", required = true) String customerId) {
		List<TyCustomerlabelBean> list = tyCustomerlabelService.queryByCustId(customerId);
		Map<String,TyCustomerlabelBean> catMap = new LinkedHashMap<String,TyCustomerlabelBean>();
		for(TyCustomerlabelBean lb:list){
			if(catMap.containsKey(lb.getCatId())){
				TyCustomerlabelBean cat = catMap.get(lb.getCatId());
				cat.getChildren().add(lb);
			}else{
				TyCustomerlabelBean cat = new TyCustomerlabelBean();
				cat.setCatId(lb.getCatId());
				cat.setCatName(lb.getCatName());
				cat.getChildren().add(lb);
				catMap.put(lb.getCatId(), cat);
			}
		}
		// 添加字典翻译
		return setSuccessModelMap(modelMap, catMap.values());
	}

//	// 详细信息
//	@ApiOperation(value = "客户标签详情")
//	@RequiresPermissions("ty.tyCustomerlabel.read")
//	@RequestMapping(value = "/read/detail")
//	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
//		TyCustomerlabelBean tyCustomerlabelBean = new TyCustomerlabelBean();
//		Map<String, List<DictItem>> dicts = null;
//		if (StringUtils.isNotEmpty(id)) {
//			TyCustomerlabel record = tyCustomerlabelService.queryById(id);
//			try {
//				BeanUtils.copyProperties(tyCustomerlabelBean, record);
//
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				tyCustomerlabelBean = null;
//				e.printStackTrace();
//			}
//		}
//		// 添加字典翻译
//		if (tyCustomerlabelBean != null) {
//		}
//
//		return setSuccessModelMap(modelMap, tyCustomerlabelBean, dicts);
//	}

	// 新增客户标签
	@ApiOperation(value = "添加客户标签")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap
//			, @RequestParam(value = "labelId", required = false) String labelId
			, @RequestParam(value = "labelName[]", required = true) String[] labelName
			, @RequestParam(value = "customerId", required = true) String customerId) {
		
		//先删除
//		tyCustomerlabelService.deleteByCustId(customerId);
		for(String singleLable:labelName){
			List<TyLabel> labelList = tyLabelService.selectByLabelName(singleLable);
			TyLabel label = null;
			if(labelList==null || labelList.size()==0){
				label = new TyLabel();
				label.setCatId(Constants.LABEL_OTHER_CAT_ID);
				label.setCatName(Constants.LABEL_OTHER_CAT_NAME);
				label.setLabelLevel("1");
				label.setLabelName(singleLable);
				label.setPid(Constants.LABEL_OTHER_CAT_ID);
				TyLabel result = tyLabelService.add(label);
				label.setId(result.getId());
			}else{
				label = labelList.get(0);
			}
			TyCustomerlabel cl = new TyCustomerlabel();
			cl.setCustomerId(customerId);
			cl.setLabelId(label.getId());
			cl.setScore(1.0);
			tyCustomerlabelService.add(cl);
		}
		return setSuccessModelMap(modelMap);
	}

//	// 修改客户标签
//	@ApiOperation(value = "修改客户标签")
//	@RequiresPermissions("ty.tyCustomerlabel.update")
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	public Object update(HttpServletRequest request, ModelMap modelMap) {
//		TyCustomerlabel record = Request2ModelUtil.covert(TyCustomerlabel.class, request);
//		tyCustomerlabelService.update(record);
//		return setSuccessModelMap(modelMap);
//	}
//
//	// 删除客户标签
//	@ApiOperation(value = "删除客户标签")
//	@RequiresPermissions("ty.tyCustomerlabel.delete")
//	@RequestMapping(value = "/delete", method = RequestMethod.POST)
//	public Object delete(HttpServletRequest request, ModelMap modelMap,
//			@RequestParam(value = "id", required = false) String id) {
//		TyCustomerlabel record = tyCustomerlabelService.queryById(id);
//		if (record != null) {
//			tyCustomerlabelService.deletePhysical(id);
//		}
//		return setSuccessModelMap(modelMap);
//	}
}
