package com.tfzq.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.exception.IllegalParameterException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tfzq.service.TyCustomerlabelService;
import com.tfzq.service.TyLabelService;
import com.tfzq.service.TyLabelnetService;
import com.tfzq.service.TyOrgcustomerService;
import com.tfzq.ty.model.generator.TyCustomerlabel;
import com.tfzq.ty.model.generator.TyLabel;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.ty.TyCustomerlabelBean;
import com.tfzq.ty.model.ty.TyLabelBean;
import com.tfzq.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标签控制类
 * 
 * @author pengtao
 */
@RestController
@Api(value = "标签管理", description = "标签管理")
@RequestMapping(value = "ty/tyLabel", method = RequestMethod.POST)
public class TyLabelController extends BaseController {
	@Autowired
	private TyLabelService tyLabelService;
	@Autowired
	private TyCustomerlabelService tyCustomerlabelService;
	@Autowired
	private TyOrgcustomerService tyOrgcustomerService;
	@Autowired
	private TyLabelnetService tyLabelnetService;

	// 查询标签分类
	@ApiOperation(value = "查询标签分类")
	// @RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/Catelist")
	public Object getCatelist(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("isCate", "1");
		params.put("orderBy", "to_number(order_num) asc");
		PageInfo<TyLabelBean> list = tyLabelService.queryBeans(params);

		Map<String, List<DictItem>> dicts = null;
		// 添加字典翻译
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 查询标签
	@ApiOperation(value = "查询标签")
	// @RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "to_number(order_num) asc");
		String pids = (String) params.get("pids");
		if (StringUtils.isNotEmpty(pids)) {
			String[] split = pids.split(",");
			params.put("pids", Arrays.asList(split));
		}
		PageInfo<TyLabelBean> list = tyLabelService.queryBeans(params);

		if (list.getList().size() > 0) {
			for (TyLabelBean singleBean : list.getList()) {
				if (StringUtils.isNotBlank(singleBean.getPreId())) {
					TyLabel preBean = tyLabelService.queryById(singleBean.getPreId());
					if (preBean != null) {
						singleBean.setPreName(preBean.getLabelName());
					}
				}
				if (StringUtils.isNotBlank(singleBean.getNextId())) {
					TyLabel nextBean = tyLabelService.queryById(singleBean.getNextId());
					if (nextBean != null) {
						singleBean.setNextName(nextBean.getLabelName());
					}
				}
			}
		}
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();

		// 添加字典翻译
		List<TyLabelBean> labelBeans = tyLabelService.getLabelCat();
		TyLabelBean labelbean = new TyLabelBean();
		labelbean.setId("0");
		labelbean.setLabelName("标签类别");
		labelBeans.add(0, labelbean);
		addDictFromModel(dicts, "LabelCats", labelBeans, "id", "labelName");

		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "标签详情")
	// @RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		TyLabelBean tyLabelBean = new TyLabelBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			TyLabel record = tyLabelService.queryById(id);
			try {
				BeanUtils.copyProperties(tyLabelBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				tyLabelBean = null;
				e.printStackTrace();
			}
		}

		// 添加字典翻译
		List<TyLabelBean> labelBeans = tyLabelService.getLabelCat();
		/*
		 * TyLabelBean labelbean= new TyLabelBean(); labelbean.setId("0");
		 * labelbean.setLabelName("标签类别"); labelBeans.add(0, labelbean);
		 */
		addDictFromModel(dicts, "LabelCats", labelBeans, "id", "labelName");

		// 添加字典翻译
		if (tyLabelBean != null) {
		}

		return setSuccessModelMap(modelMap, tyLabelBean, dicts);
	}

	// 新增标签
	@ApiOperation(value = "添加标签")
	// @RequiresPermissions("ty.tyLabel.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		TyLabel record = Request2ModelUtil.covert(TyLabel.class, request);
		// TODO 加去重验证
//		if (record.getCatId().equals("0")) { // 添加类别
//			List<TyLabelBean> labelBeans = tyLabelService.getLabelCat();
//			for (Iterator iterator = labelBeans.iterator(); iterator.hasNext();) {
//				TyLabelBean tyLabelBean = (TyLabelBean) iterator.next();
//				if (tyLabelBean.getLabelName().equals(record.getLabelName())) {
//					throw new BusinessException("类别已存在");
//				}
//			}
//		} else {// 添加类别下的标签
//			List<TyLabel> labels = tyLabelService.getAllSubRecords();
//			for (Iterator iterator = labels.iterator(); iterator.hasNext();) {
//				TyLabel tyLabel = (TyLabel) iterator.next();
//				if (tyLabel.getLabelName().equals(record.getLabelName())) {
//					throw new BusinessException("标签已存在");
//				}
//			}
//		}
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyLabel> labels = tyLabelService.getRecordsByCondition(params);
		for (Iterator iterator = labels.iterator(); iterator.hasNext();) {
			TyLabel tyLabel = (TyLabel) iterator.next();
			if (tyLabel.getLabelName().equals(record.getLabelName())) {
				throw new BusinessException("存在同名标签");
			}
		}
		TyLabel newRecord = tyLabelService.add(record);
		return setSuccessModelMap(modelMap, newRecord);
	}

	// 新增客户标签（其他类别）
	@ApiOperation(value = "新增客户标签")
	// @RequiresPermissions("ty.tyLabel.add")
	@RequestMapping(value = "/addCustLabel", method = RequestMethod.POST)
	public Object addCustLabel(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelName", required = true) String labelName,
			@RequestParam(value = "custId", required = true) String custId) {
		TyLabelBean resultLabel = new TyLabelBean();
		// 查询是否该客户已经有这个标签
		// Map<String, Object> pm = new HashMap<String, Object>();
		// pm.put("customerId", custId);
		// pm.put("pageSize", "1000");
		// pm.put("orderBy","id desc");
		List<TyCustomerlabelBean> labelBeans = tyCustomerlabelService.queryByCustId(custId);
		if (labelBeans.size() > 0) {
			for (TyCustomerlabelBean labelBean : labelBeans) {
				if (labelBean.getLabelId() != null) {
					TyLabel tl = tyLabelService.queryById(labelBean.getLabelId());
					if (labelName.equals(tl.getLabelName())) {
						resultLabel.setIsExists("1");
						return setSuccessModelMap(modelMap, resultLabel);
					}
				}
			}
		}
		resultLabel.setIsExists("0");
		int isExist = 0;
		// List<TyLabel> sublabel = tyLabelService.getAllSubRecords();
		List<TyLabel> subList = tyLabelService.selectByLabelName(labelName);
		if (subList != null && subList.size() > 0) {
			TyLabel sub = subList.get(0);
			TyCustomerlabel label = new TyCustomerlabel();
			label.setCustomerId(custId);
			label.setLabelId(sub.getId());
			tyCustomerlabelService.add(label);

			resultLabel.setId(sub.getId());
			resultLabel.setLabelName(sub.getLabelName());
			resultLabel.setCatId(sub.getCatId());
			resultLabel.setCatName(sub.getCatName());

			isExist = 1;
		}
		if (isExist == 0) {
			TyLabel lb = new TyLabel();
			lb.setLabelName(labelName);
			lb.setCatId(Constants.LABEL_OTHER_CAT_ID);
			lb.setCatName(Constants.LABEL_OTHER_CAT_NAME);
			lb.setLabelLevel("1"); // 默认加成其他类别下的一级标签
			lb.setPid("0");
			TyLabel newRecord = tyLabelService.add(lb);

			resultLabel.setId(newRecord.getId());
			resultLabel.setLabelName(newRecord.getLabelName());
			resultLabel.setCatId(newRecord.getCatId());
			resultLabel.setCatName(newRecord.getCatName());

			TyCustomerlabel label = new TyCustomerlabel();
			label.setCustomerId(custId);
			label.setLabelId(newRecord.getId());
			tyCustomerlabelService.add(label);
		}

		// 更新客户mark
		TyOrgcustomer customer = tyOrgcustomerService.queryById(custId);
		if (StringUtils.isBlank(customer.getMark())) {
			customer.setMark(labelName);
		} else if (!customer.getMark().contains(labelName)) {
			customer.setMark(customer.getMark() + Constants.LABEL_SEPERATOR + labelName);
		}
		tyOrgcustomerService.update(customer);
		return setSuccessModelMap(modelMap, resultLabel);
	}

	// 修改标签
	@ApiOperation(value = "修改标签")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		TyLabel record = Request2ModelUtil.covert(TyLabel.class, request);
		TyLabel oldRecord = tyLabelService.queryById(record.getId());
		String oldLabelName = oldRecord.getLabelName();
		Map<String, Object> params = new HashMap<String, Object>();
		List<TyLabel> labels = tyLabelService.getRecordsByCondition(params);
		for (Iterator iterator = labels.iterator(); iterator.hasNext();) {
			TyLabel tyLabel = (TyLabel) iterator.next();
			if ((!record.getId().equals(tyLabel.getId()))
					&& tyLabel.getLabelName().equals(record.getLabelName())) {
				throw new BusinessException("存在同名标签");
			}
		}
//		transModel(oldRecord, record);
		if("0".equals(oldRecord.getCatId())){
			oldRecord.setCatName(record.getLabelName());
		}
		oldRecord.setLabelName(record.getLabelName());
		tyLabelService.update(oldRecord);
		tyLabelService.updateCustomerlabel(oldRecord);
		tyOrgcustomerService.updateBatchMark(oldLabelName, record.getLabelName());
		return setSuccessModelMap(modelMap);
	}

	// 修改标签（递归式移动所属标签到目标标签名下）
	@ApiOperation(value = "移动标签")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/moveLabels", method = RequestMethod.POST)
	public Object moveLabels(HttpServletRequest request, ModelMap modelMap) {
		TyLabel record = Request2ModelUtil.covert(TyLabel.class, request);
		// 判断父标签是否为空
		if (StringUtils.isBlank(record.getPid())) {
			throw new BusinessException("父标签不能为空");
		}
		TyLabel parentLabel = tyLabelService.queryById(record.getPid());
		TyLabel oldRecord = tyLabelService.queryById(record.getId());
		if (record.getId().equals(record.getPid())) {
			throw new IllegalParameterException("标签与父标签不允许相同。");
		}

		List<TyLabelBean> subTreeLabels = tyLabelService.getcurrentSubLabels(record.getId());
		Map<String, TyLabelBean> nodeMap = new HashMap<String, TyLabelBean>();
		int parentLevel = Integer.parseInt(parentLabel.getLabelLevel());
		int curLevel = Integer.parseInt(oldRecord.getLabelLevel());
		int maxLevel = curLevel;
		for (TyLabelBean node : subTreeLabels) {
			int nodeLevel = Integer.parseInt(node.getLabelLevel());
			if (nodeLevel > maxLevel) {
				maxLevel = nodeLevel;
			}
			nodeMap.put(node.getLabelName(), node);
		}
		if (nodeMap.containsKey(parentLabel.getLabelName())) {
			throw new IllegalParameterException("不允许将标签移动到自己的子孙标签中。");
		}
		// 总共三级标签
		if (parentLevel + 1 + maxLevel - curLevel > 3) {
			throw new IllegalParameterException(oldRecord.getLabelName() + "下还有子孙标签，移动后超出三级标签的层次。");
		}
		// 设置新的标签等级
		record.setLabelLevel("" + (parentLevel + 1));
		// 计算原有标签和新标签之间的的级别差
		int levelOffset = parentLevel + 1 - curLevel;
		// 递归查询所有的后代标签
		if (!CollectionUtils.isEmpty(subTreeLabels)) {
			for (TyLabelBean tyLabelBean : subTreeLabels) {
				tyLabelBean.setCatId("0".equals(parentLabel.getCatId())?parentLabel.getId():parentLabel.getCatId());
				tyLabelBean.setCatName(parentLabel.getCatName());
				tyLabelBean.setLabelLevel("" + (Integer.parseInt(tyLabelBean.getLabelLevel()) + levelOffset));
				tyLabelService.update(tyLabelBean);
			}
		}
		//transModel(oldRecord, record); // 迁移父标签
		oldRecord.setLabelLevel(record.getLabelLevel());
		oldRecord.setCatId("0".equals(parentLabel.getCatId())?parentLabel.getId():parentLabel.getCatId());
		oldRecord.setCatName(parentLabel.getCatName());
		oldRecord.setPid(parentLabel.getId());
		tyLabelService.update(oldRecord);
		return setSuccessModelMap(modelMap);
	}

	// 批量移动标签分组
	@ApiOperation(value = "批量移动标签分组")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Object batchUpdate(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelIds[]", required = true) List<String> labelIds,
			@RequestParam(value = "catId", required = true) String catId) {
		TyLabel catRecord = tyLabelService.queryById(catId);
		if (catRecord != null && labelIds.size() > 0) {
			for (String labelId : labelIds) {
				TyLabel record = tyLabelService.queryById(labelId);
				if (record != null) {
					record.setCatId(catId);
					record.setCatName(catRecord.getCatName());
					tyLabelService.update(record);
				}
			}
		}
		return setSuccessModelMap(modelMap);
	}

	// 批量修改标签名称
	@ApiOperation(value = "批量修改标签名称")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/batchUpdateName", method = RequestMethod.POST)
	public Object batchUpdateName(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelIds[]", required = true) List<String> labelIds,
			@RequestParam(value = "labelNames[]", required = true) List<String> labelNames) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("labelIdList", labelIds);
		List<TyLabel> labels = tyLabelService.getSubRecordsByCondition(params);

		for (int i = 0; i < labelIds.size(); i++) {
			if (StringUtils.isNotBlank(labelIds.get(i))) {
				TyLabel record = tyLabelService.queryById(labelIds.get(i));
				if (record != null && StringUtils.isNotBlank(labelNames.get(i))) {
					for (Iterator iterator = labels.iterator(); iterator.hasNext();) {
						TyLabel tyLabel = (TyLabel) iterator.next();
						if (tyLabel.getLabelName().equals(labelNames.get(i))) {
							throw new BusinessException("标签已存在");
						}
					}
					record.setLabelName(labelNames.get(i));
					tyLabelService.update(record);
				}
			}
		}
		return setSuccessModelMap(modelMap);
	}

	// 批量修改标签组排序
	@ApiOperation(value = "批量修改标签组排序")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/batchUpdateOrder", method = RequestMethod.POST)
	public Object batchUpdateOrder(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "ids[]", required = true) List<String> ids,
			@RequestParam(value = "sorts[]", required = true) List<String> sorts) {
		if (ids == null || sorts == null || ids.size() != sorts.size()) {
			throw new IllegalParameterException("参数错误");
		}
		for (int i = 0; i < ids.size(); i++) {
			TyLabel label = tyLabelService.queryById(ids.get(i));
			label.setOrderNum(sorts.get(i));
			tyLabelService.update(label);
		}
		return setSuccessModelMap(modelMap);
	}

	// 新增标签分类
	@ApiOperation(value = "添加标签分类")
	// @RequiresPermissions("ty.tyLabel.add")
	@RequestMapping(value = "/addCate", method = RequestMethod.POST)
	public Object addCate(HttpServletRequest request, ModelMap modelMap) {
		TyLabel record = Request2ModelUtil.covert(TyLabel.class, request);
		record.setId(tyLabelService.getId());// 手工设置ID
		// 设置默认值
		record.setCatId("0");
		record.setLabelName(record.getCatName());

		Map<String, Object> params = new HashMap<String, Object>();
		List<TyLabel> labels = tyLabelService.getRecordsByCondition(params);
		for (Iterator iterator = labels.iterator(); iterator.hasNext();) {
			TyLabel tyLabel = (TyLabel) iterator.next();
			if (tyLabel.getLabelName().equals(record.getLabelName())) {
				throw new BusinessException("存在同名标签");
			}
		}
		
		tyLabelService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改标签分类
	@ApiOperation(value = "修改标签分类")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/updateCate", method = RequestMethod.POST)
	public Object updateCate(HttpServletRequest request, ModelMap modelMap) {
		TyLabel record = Request2ModelUtil.covert(TyLabel.class, request);
		record.setLabelName(record.getCatName());
		tyLabelService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除标签
	@ApiOperation(value = "删除标签")
	// @RequiresPermissions("ty.tyLabel.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		TyLabel record = tyLabelService.queryById(id);
		if (record != null) {
			tyLabelService.deletePhysical(id);
			tyLabelnetService.deleteByLabelId(id);

			// 查找同名标签
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("accurateLabelName", record.getLabelName());
			List<TyLabel> existSameNameLabels = tyLabelService.getRecordsByCondition(params);
			if (!CollectionUtils.isEmpty(existSameNameLabels)) {
				TyLabel sameNameLabel = existSameNameLabels.get(0);

				// 更新对应的客户标签（旧标签id替换为新的标签id,其中主键也需要改）
				Map<String, Object> params1 = new HashMap<>();
				params1.put("oldLabelId", record.getId());
				params1.put("newLabelId", sameNameLabel.getId());
				tyCustomerlabelService.transferLableId(params1);
			} else {
				// 没有同名标签则需要删除客户标签关系
				tyCustomerlabelService.deleteByLabelId(id);
			}
			// 存在子标签
			// 查询当前标签是否存在下级标签，如果存在，更新对应的客户标签关系，关联标签关系，最后删除子孙节点
			List<TyLabelBean> labels = tyLabelService.getcurrentSubLabels(id);
			if (labels != null && labels.size() > 0) {
				// 删除子孙标签并且删除对应的关联标签关系
				for (TyLabel tyLabel : labels) {
					tyLabelService.deletePhysical(tyLabel.getId());
					tyLabelnetService.deleteByLabelId(tyLabel.getId());
					tyCustomerlabelService.deleteByLabelId(tyLabel.getId());
				}
			}
			tyOrgcustomerService.updateBatchMark(record.getLabelName(),"");
		}
		return setSuccessModelMap(modelMap);
	}

	// 批量删除标签
	@ApiOperation(value = "批量删除标签")
	// @RequiresPermissions("ty.tyLabel.delete")
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public Object batchDelete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelIds[]", required = true) List<String> labelIds) {
		for (String labelId : labelIds) {
			TyLabel record = tyLabelService.queryById(labelId);
			if (record != null) {
				tyLabelService.deletePhysical(labelId);
				tyCustomerlabelService.deleteByLabelId(labelId);
			}
		}
		return setSuccessModelMap(modelMap);
	}

//	private void transModel(TyLabel oldrecord, TyLabel newRecord) {
//		if (newRecord.getCatId() != null) {
//			oldrecord.setCatId(newRecord.getCatId());
//		}
//		if (newRecord.getCatName() != null) {
//			oldrecord.setCatName(newRecord.getCatName());
//		}
//		if (newRecord.getLabelName() != null) {
//			oldrecord.setLabelName(newRecord.getLabelName());
//		}
//		if (newRecord.getOrderNum() != null) {
//			oldrecord.setOrderNum(newRecord.getOrderNum());
//		}
//		if (newRecord.getLabelLevel() != null) {
//			oldrecord.setLabelLevel(newRecord.getLabelLevel());
//			;
//		}
//		if (newRecord.getPid() != null) {
//			oldrecord.setPid(newRecord.getPid());
//		}
//		if (newRecord.getOrderNum() != null) {
//			oldrecord.setOrderNum(newRecord.getOrderNum());
//		}
//		if (newRecord.getPreId() != null) {
//			oldrecord.setPreId(newRecord.getPreId());
//		} else {
//			oldrecord.setPreId("");
//		}
//		if (newRecord.getNextId() != null) {
//			oldrecord.setNextId(newRecord.getNextId());
//		} else {
//			oldrecord.setNextId("");
//		}
//
//	}

	// 分页查询子级标签
	/**
	 * 分页改为不分页
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "分页查询子级标签")
	// @RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/getSubLabel")
	public Object getSubLabel(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		String pid = (String) params.get("pid");
		if (StringUtils.isNotEmpty(pid)) { // 点击标签
			TyLabel tyLabel = tyLabelService.queryById(pid);
			Integer levelMargin = Integer.parseInt((String) params.get("labelLevel"))
					- Integer.parseInt(tyLabel.getLabelLevel());
			params.remove("labelLevel");
			params.put("levelMargin", levelMargin);
		}

		PageInfo<TyLabelBean> pageinfo = tyLabelService.querySubLabel(params);

		return setSuccessModelMap(modelMap, pageinfo);
	}

	// 获取当前子孙标签和关联标签
	@ApiOperation(value = "分页查询子级标签")
	// @RequiresPermissions("ty.tyLabel.read")
	@RequestMapping(value = "/getSubLabelsAndBindLabels")
	public Object getSubLabelsAndBindLabels(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelIds[]", required = true) List<String> labelIds) {
		if (CollectionUtils.isEmpty(labelIds)) {
			throw new BusinessException("标签不能为空！");
		}
		// 查询对应子孙标签和关联标签
		Map<String, Object> data = new HashMap<>();
		 Set<TyLabelBean> subLabels = new HashSet<>();
		 Set<TyLabelBean> bindLabels = new HashSet<>();
		for (String labelId : labelIds) {
			//子孙
			List<TyLabelBean> currentSubLabels = tyLabelService.getcurrentSubLabels(labelId);
			subLabels.addAll(currentSubLabels);
			// 关联  + 被关联
			List<TyLabelBean> currentBindLabels2 = tyLabelService.getBindRelatedLabels(labelId);
			bindLabels.addAll(currentBindLabels2);
		}
		data.put("subLabels", subLabels);
		data.put("bindLabels", bindLabels);
		return setSuccessModelMap(modelMap, data);
	}

	// 修改标签分类
	@ApiOperation(value = "修改标签分类")
	// @RequiresPermissions("ty.tyLabel.update")
	@RequestMapping(value = "/searchLabelByName", method = RequestMethod.POST)
	public Object searchLabelByName(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "labelName", required = true) String labelName) {
		if (StringUtils.isBlank(labelName)) {
			throw new BusinessException("标签名称不能为空！");
		}
		Map<String, Object> params = WebUtil.getParameterMap(request);
		PageInfo<TyLabelBean> queryBeans = tyLabelService.queryBeans(params);
		return setSuccessModelMap(modelMap, queryBeans);
	}
}
