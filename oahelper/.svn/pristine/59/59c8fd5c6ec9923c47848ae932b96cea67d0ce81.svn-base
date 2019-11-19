package com.tfzq.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import org.ibase4j.core.base.BaseController;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.service.sys.SysDicService;
import org.ibase4j.core.support.DictItem;

import com.tfzq.pr.model.generator.GoVoteGroup;
import com.tfzq.pr.model.generator.MyVote;
import com.tfzq.pr.model.generator.MyVoteGroup;
import com.tfzq.pr.model.pr.GoVoteBean;
import com.tfzq.pr.model.pr.GoVoteGroupBean;
import com.tfzq.pr.model.pr.GoVoteItemBean;
import com.tfzq.service.GoVoteGroupService;
import com.tfzq.service.GoVoteItemService;
import com.tfzq.service.GoVoteService;
import com.tfzq.service.MyService;

/**
 * 投票分组控制类
 * 
 * @author yuzhitao
 */
@RestController
@Api(value = "投票分组管理", description = "投票分组管理")
@RequestMapping(value = "pr/goVoteGroup", method = RequestMethod.POST)
public class GoVoteGroupController extends BaseController {
	@Autowired
	private GoVoteGroupService goVoteGroupService;
	@Autowired
	private GoVoteService goVoteService;
	@Autowired
	private GoVoteItemService goVoteItemService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private MyService myService;

	// 查询投票分组
	@ApiOperation(value = "查询投票分组")
	// @RequiresPermissions("pr.goVoteGroup.read")
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		params.put("deleteFlag", "0");
		params.put("orderBy", "id desc");
		PageInfo<GoVoteGroupBean> list = goVoteGroupService.queryBeans(params);

		// String isClosed = request.getParameter("isClosed");

		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> open_closeMap = sysDicService.queryDicByDicIndexKey("OPEN_CLOSE");
		addDictFromMap(dicts, "OPEN_CLOSE", open_closeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		List<GoVoteGroupBean> filteList = new ArrayList<GoVoteGroupBean>();
		Date curr = new Date();
		for (GoVoteGroupBean singleBean : list.getList()) {
			if (StringUtils.isNotEmpty(singleBean.getIsOpen())) {
				singleBean.setIsOpenText(open_closeMap.get(singleBean.getIsOpen().toString()));
			}
			if (StringUtils.isNotEmpty(singleBean.getIsActivity())) {
				singleBean.setIsActivityText(yes_noMap.get(singleBean.getIsActivity().toString()));
			}
			//已经结束的不再返回
			if(singleBean.getEndTime().getTime()>curr.getTime()){				
				filteList.add(singleBean);
			}else{
				singleBean.setIsOpen("CLOSE");
				goVoteGroupService.update(singleBean);
			}
		}
		list.setList(filteList);
		return setSuccessModelMap(modelMap, list, dicts);
	}

	// 详细信息
	@ApiOperation(value = "投票分组详情")
	// @RequiresPermissions("pr.goVoteGroup.read")
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) String id) {
		GoVoteGroupBean goVoteGroupBean = new GoVoteGroupBean();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		if (StringUtils.isNotEmpty(id)) {
			GoVoteGroup record = goVoteGroupService.queryById(id);
			try {
				BeanUtils.copyProperties(goVoteGroupBean, record);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goVoteGroupBean = null;
				e.printStackTrace();
			}
		}
		// 添加字典翻译
		Map<String, String> open_closeMap = sysDicService.queryDicByDicIndexKey("OPEN_CLOSE");
		addDictFromMap(dicts, "OPEN_CLOSE", open_closeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);
		if (goVoteGroupBean != null) {
			if (StringUtils.isNotEmpty(goVoteGroupBean.getIsOpen())) {
				goVoteGroupBean.setIsOpenText(open_closeMap.get(goVoteGroupBean.getIsOpen().toString()));
			}
			if (StringUtils.isNotEmpty(goVoteGroupBean.getIsActivity())) {
				goVoteGroupBean.setIsActivityText(yes_noMap.get(goVoteGroupBean.getIsActivity().toString()));
			}
		}

		return setSuccessModelMap(modelMap, goVoteGroupBean, dicts);
	}

	// 新增投票分组
	@ApiOperation(value = "添加投票分组")
	// @RequiresPermissions("pr.goVoteGroup.add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, ModelMap modelMap) {
		GoVoteGroup record = Request2ModelUtil.covert(GoVoteGroup.class, request);
		goVoteGroupService.add(record);
		return setSuccessModelMap(modelMap);
	}

	// 修改投票分组
	@ApiOperation(value = "修改投票分组")
	// @RequiresPermissions("pr.goVoteGroup.update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		GoVoteGroup record = Request2ModelUtil.covert(GoVoteGroup.class, request);
		goVoteGroupService.update(record);
		return setSuccessModelMap(modelMap);
	}

	// 删除投票分组
	@ApiOperation(value = "删除投票分组")
	// @RequiresPermissions("pr.goVoteGroup.delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) {
		GoVoteGroup record = goVoteGroupService.queryById(id);
		if (record != null) {
			goVoteGroupService.deletePhysical(id);
		}
		return setSuccessModelMap(modelMap);
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ApiOperation(value = "导出投票详情")
	public void downloadFile(HttpServletResponse response, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) throws Exception {
		List<MyVote> votes = myService.selectByVoteGroupId(id);

		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("投票情况");
			HSSFRow row = sheet.createRow((short) (0));
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("投票名称");
			cell = row.createCell(1);
			cell.setCellValue("投票项");
			cell = row.createCell(2);
			cell.setCellValue("序号");
			cell = row.createCell(3);
			cell.setCellValue("投票内容");
			cell = row.createCell(4);
			cell.setCellValue("投票人");

			for (int i = 0; i < votes.size(); i++) {
				MyVote vote = votes.get(i);
				row = sheet.createRow((short) (i + 1));
				cell = row.createCell(0);
				if (!"".equals(vote.getVoteGroupName()) && vote.getVoteGroupName() != null) {
					cell.setCellValue(vote.getVoteGroupName());
				}

				cell = row.createCell(1);
				if (!"".equals(vote.getVoteName()) && vote.getVoteName() != null) {
					cell.setCellValue(vote.getVoteName());
				}

				cell = row.createCell(2);
				if (!"".equals("" + vote.getSortNo()) && vote.getSortNo() != null) {
					cell.setCellValue(vote.getSortNo());
				}

				cell = row.createCell(3);
				if (!"".equals(vote.getVoteItemContent()) && vote.getVoteItemContent() != null) {
					cell.setCellValue(vote.getVoteItemContent());
				}

				cell = row.createCell(4);
				if (!"".equals(vote.getUserName()) && vote.getUserName() != null) {
					cell.setCellValue(vote.getUserName());
				}
			}
			// response.setContentType("application/octet-stream");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=vote_detail.xls");

			OutputStream os = response.getOutputStream();

			wb.write(os);
			wb.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	@RequestMapping(value = "/selectUserVote", method = RequestMethod.GET)
	@ApiOperation(value = "查看用户投票情况")
	public Object selectUserVote(HttpServletResponse response, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "userId", required = false) String userId) throws Exception {

		List<MyVote> myVote = myService.selectUserVote(id, userId);
		GoVoteGroup voteGroup = goVoteGroupService.queryById(id);

		Map<String, Object> params = new TreeMap<String, Object>();
		params.put("voteGroupId", id);
		params.put("orderBy", "sort_no asc");
		List<GoVoteBean> votes = goVoteService.queryByCondition(params);

		List<GoVoteItemBean> voteItems = new ArrayList<GoVoteItemBean>();
		for (GoVoteBean vote : votes) {
			Map<String, Object> p = new TreeMap<String, Object>();
			p.put("voteId", vote.getId());
			params.put("orderBy", "sort_no asc");
			List<GoVoteItemBean> vi = goVoteItemService.queryByCondition(p);
			voteItems.addAll(vi);
		}

		MyVoteGroup myVoteGroup = new MyVoteGroup();
		myVoteGroup.setMyVote(myVote);
		myVoteGroup.setVoteGroup(voteGroup);
		myVoteGroup.setVotes(votes);
		myVoteGroup.setVoteItems(voteItems);
		return setSuccessModelMap(modelMap, myVoteGroup);
	}

	@RequestMapping(value = "/selectUserAllVote", method = RequestMethod.GET)
	@ApiOperation(value = "查看用户所有投票情况")
	public Object selectUserAllVote(HttpServletResponse response, ModelMap modelMap,
			@RequestParam(value = "isActivity", required = false) String isActivity,
			@RequestParam(value = "userId", required = false) String userId) throws Exception {

		// 查询到用户进行的所有活动或者投票的id
		List<MyVote> myVotes = myService.selectUserAllVote(isActivity, userId);
		// 用来存放查询到的所有活动或者投票详情
		List<GoVoteGroupBean> list = new ArrayList<GoVoteGroupBean>();
		Map<String, List<DictItem>> dicts = new HashMap<String, List<DictItem>>();
		// 添加字典翻译
		Map<String, String> open_closeMap = sysDicService.queryDicByDicIndexKey("OPEN_CLOSE");
		addDictFromMap(dicts, "OPEN_CLOSE", open_closeMap);
		Map<String, String> yes_noMap = sysDicService.queryDicByDicIndexKey("YES_NO");
		addDictFromMap(dicts, "YES_NO", yes_noMap);

		for (MyVote myVote : myVotes) {
			String voteGroupId = myVote.getVoteGroupId();
			GoVoteGroup record = goVoteGroupService.queryById(voteGroupId);
			GoVoteGroupBean goVoteGroupBean = new GoVoteGroupBean();
			try {
				BeanUtils.copyProperties(goVoteGroupBean, record);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				goVoteGroupBean = null;
				e.printStackTrace();
			}

			if (goVoteGroupBean != null) {
				if (StringUtils.isNotEmpty(goVoteGroupBean.getIsOpen())) {
					goVoteGroupBean.setIsOpenText(open_closeMap.get(goVoteGroupBean.getIsOpen().toString()));
				}
				if (StringUtils.isNotEmpty(goVoteGroupBean.getIsActivity())) {
					goVoteGroupBean.setIsActivityText(yes_noMap.get(goVoteGroupBean.getIsActivity().toString()));
				}
				list.add(goVoteGroupBean);
			}
		}

		return setSuccessModelMap(modelMap, list, dicts);
	}

	@RequestMapping(value = "/selectVoteGroup", method = RequestMethod.GET)
	@ApiOperation(value = "查看投票内容")
	public Object selectVoteGroup(HttpServletResponse response, ModelMap modelMap,
			@RequestParam(value = "id", required = false) String id) throws Exception {
		GoVoteGroup voteGroup = goVoteGroupService.queryById(id);

		Map<String, Object> params = new TreeMap<String, Object>();
		params.put("voteGroupId", id);
		params.put("orderBy", "sort_no asc");
		List<GoVoteBean> votes = goVoteService.queryByCondition(params);

		List<GoVoteItemBean> voteItems = new ArrayList<GoVoteItemBean>();
		for (GoVoteBean vote : votes) {
			Map<String, Object> p = new TreeMap<String, Object>();
			p.put("voteId", vote.getId());
			params.put("orderBy", "sort_no asc");
			List<GoVoteItemBean> vi = goVoteItemService.queryByCondition(p);
			voteItems.addAll(vi);
		}

		MyVoteGroup myVoteGroup = new MyVoteGroup();
		myVoteGroup.setVoteGroup(voteGroup);
		myVoteGroup.setVotes(votes);
		myVoteGroup.setVoteItems(voteItems);
		return setSuccessModelMap(modelMap, myVoteGroup);
	}

}
