package com.tfzq.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.exception.BusinessException;
import org.ibase4j.core.support.DictItem;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.PropertiesUtil;
import org.ibase4j.core.util.RedisUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.service.sys.SysDicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tfzq.ty.model.generator.TyOrgcustomer;
import com.tfzq.ty.model.generator.TyOrgcustomerpush;
import com.tfzq.ty.model.generator.TyServiceorg;
import com.tfzq.ty.model.generator.TyStafflist;
import com.tfzq.ty.model.ty.TyOrgcustomerBean;
import com.tfzq.ty.provider.TyOrgcustomerProvider;
import com.tfzq.ty.provider.TyOrgcustomerpushProvider;
import com.tfzq.util.Constants;
import com.tfzq.util.HttpClient;

/**
 * @author pengtao
 */
@Service
public class TyOrgcustomerService extends BaseService<TyOrgcustomerProvider, TyOrgcustomer> {
	private static Logger logger = LoggerFactory.getLogger(TyOrgcustomerService.class);
	@Autowired
	private TyStafflistService tyStafflistService;
	@Autowired
	private TyServiceorgService tyServiceorgService;
	@Autowired
	private SysDicService sysDicService;
	@Autowired
	private TyOrgcustomerpushProvider tyOrgcustomerpushProvider;

	@Autowired
	public void setProvider(TyOrgcustomerProvider provider) {
		this.provider = provider;
	}

	public PageInfo<TyOrgcustomerBean> queryBeans(Map<String, Object> params) {
		return provider.queryBeans(params);
	}
	
	//删除客户同样要增加一条push记录
    public void delete(String id) {
    	super.delete(id);
        addOrgcustomerpush(id,WebUtil.getCurrentUser());
    }

    public TyOrgcustomer add(TyOrgcustomer record) {
    	TyOrgcustomer result = super.add(record);
    	addOrgcustomerpush(result.getId(),WebUtil.getCurrentUser());
    	return result;
    }
    
	public void update(TyOrgcustomer record) {
		//更新冗余字段
		if (StringUtils.isNotEmpty(record.getSalerId())) {
			TyStafflist temp = tyStafflistService.queryById(record.getSalerId());
			record.setServiceSaler(temp == null ? "" : temp.getStaffName());
		}
		if (StringUtils.isNotEmpty(record.getOrgId())) {
			TyServiceorg temp = tyServiceorgService.queryById(record.getOrgId());
			record.setOrgName(temp == null ? "" : temp.getOrgName());
		}

		super.update(record);
		addOrgcustomerpush(record.getId(),WebUtil.getCurrentUser());
	}
	
	public void sycnMarkAuto(TyOrgcustomer record){
		provider.sycnMarkAuto(record);
	}

	public void addOrgcustomerpush(String customerId,String createBy) {
		// 新增因修改所需要的同步记录
		String plats = PropertiesUtil.getString("push.plats");
//		if (StringUtils.isNotEmpty(pushPlat)&&StringUtils.contains(plats, pushPlat)) {
//			TyOrgcustomerpush push = new TyOrgcustomerpush();
//			push.setCustomerId(customerId);
//			push.setPlatFlag(pushPlat);
//			push.setDealFlag("0");
//			tyOrgcustomerpushProvider.add(push);
//			return;
//		}
		if (StringUtils.isNotEmpty(plats)) {
			String[] platArray = plats.split(",");
			for (String plat : platArray) {
				TyOrgcustomerpush push = new TyOrgcustomerpush();
				push.setCustomerId(customerId);
				push.setPlatFlag(plat);
				push.setDealFlag("0");
				push.setCreateBy(createBy);
				tyOrgcustomerpushProvider.add(push);
			}
		}
	}

	public void update31Cust(TyOrgcustomer cust) throws Exception {
		TyOrgcustomerBean custBean = new TyOrgcustomerBean();
		try {
			BeanUtils.copyProperties(custBean, cust);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			custBean = null;
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(custBean.getSalerId())) {
			TyStafflist temp = tyStafflistService.queryById(custBean.getSalerId());
			custBean.setServiceSaler(temp == null ? "" : temp.getStaffName());
			custBean.setSalerMobile(temp == null ? "" : temp.getTel());
		}
		if (StringUtils.isNotEmpty(custBean.getOrgId())) {
			TyServiceorg temp = tyServiceorgService.queryById(custBean.getOrgId());
			custBean.setOrgName(temp == null ? "" : temp.getOrgName());

			Map<String, String> custCatMap = sysDicService.queryDicByDicIndexKey("CUSTCAT");
			String custCatStr = custCatMap.get(temp.getCustCat().toString());
			custBean.setCustCat(custCatStr);
			if (temp != null && StringUtils.isNotEmpty(temp.getOrgLevel())) {
				if (temp.getOrgLevel().equals("1")) {
					custBean.setOrgLevel(Constants.CUST_LEVEL_1);
				} else if (temp.getOrgLevel().equals("2")) {
					custBean.setOrgLevel(Constants.CUST_LEVEL_2);
				} else if (temp.getOrgLevel().equals("3")) {
					custBean.setOrgLevel(Constants.CUST_LEVEL_3);
				} else {
					custBean.setOrgLevel(Constants.CUST_LEVEL_4);
				}
			}
		}
		boolean flag = true;
		while (flag) {
			flag = false;
			String appid = PropertiesUtil.getString("31meijia.appid");
			String secret = PropertiesUtil.getString("31meijia.appsecret");
			String tokenurl = PropertiesUtil.getString("31meijia.api.token");
			String access_key = appid + "_access_token";
			String access_token = (String) RedisUtil.get(access_key, false);
			if (!StringUtils.isNotBlank(access_token)) {
				Map<String, String> pa = new HashMap<String, String>();
				pa.put("appid", appid);
				pa.put("secret", secret);
				String result1 = HttpClient.get(tokenurl, pa);
				JSONObject jsonParams1 = JSONObject.parseObject(result1);
				access_token = jsonParams1.getString("access_token");
				RedisUtil.set(access_key, access_token);
				RedisUtil.expire(access_key, 7200);
			}
			String url = PropertiesUtil.getString("31meijia.api.member") + "save-associator";
			Map<String, String> params = new HashMap<String, String>();
			params.put("access_token", access_token);
			params.put("RealName", custBean.getCustName());
			params.put("Mobile", custBean.getCustMobile());
			params.put("Email", custBean.getCustEmail() == null ? "" : custBean.getCustEmail());
			params.put("PosStatusEx", custBean.getTitle());
			params.put("Company", custBean.getOrgName());
			params.put("LevelId", custBean.getOrgLevel());
			// JSONObject jsonParams = new JSONObject();
			// jsonParams.put("Extra907828220", custBean.getServiceSaler());
			// jsonParams.put("Extra907828221", custBean.getSalerMobile());
			params.put("ExtraFields[Extra907828220]", custBean.getServiceSaler());
			params.put("ExtraFields[Extra907828221]", custBean.getSalerMobile());
			params.put("ExtraFields[Extra1109120496]", custBean.getCustCat());

			logger.info("接口调用入参对象:" + custBean.toString());
			logger.info("接口调用入参map:" + params.toString());
			String retStr = HttpClient.post(url, params);
			logger.info("接口调用返回结果:" + retStr);
			JSONObject json = null;
			try {
				json = JSON.parseObject(retStr);
			} catch (Exception e) {
				// 返回了非json的Long型结果，调用成功
				return;
			}
			String errcode = json.getString("errcode");
			if (StringUtils.isNotBlank(errcode) && (!errcode.equals("0"))) {
				if (errcode.equals("40001")) { // token失效
					flag = true;
					RedisUtil.del(access_key);
				} else {
					throw new BusinessException("接口调用失败:" + json.getString("errmsg"));
				}
			}
		}
	}

	public static void main(String[] args) {
		/*
		 * List<String> a = new ArrayList<String>(); a.add("好好学习");
		 * a.add("好好学习1"); a.add("好好学习2"); a.add("好好学习"); for (Iterator iterator
		 * = a.iterator(); iterator.hasNext();) { String string = (String)
		 * iterator.next(); System.out.println(string); } System.out.println();
		 * removeDuplicate(a); for (Iterator iterator = a.iterator();
		 * iterator.hasNext();) { String string = (String) iterator.next();
		 * System.out.println(string); }
		 */
		String s = "1";
		String[] sArray = s.split(",");
		for (String string : sArray) {
			System.out.println(string);
		}
		String[] array = new String[] { "one", "two", "three", "four", "five" };
		List<String> list = Arrays.asList(array);
		System.out.println(list.toString());

	}

	private static void removeDuplicate(List list) {
		List tempList = new ArrayList();
		for (Object i : list) {
			if (!tempList.contains(i)) {
				tempList.add(i);
			}
		}
		list.clear();
		for (Object i : tempList) {
			list.add(i);
		}
	}

	public List<TyOrgcustomerBean> queryByCondition(Map<String, Object> params) {
		return provider.queryByCondition(params);
	}

	// public void updateByOrgId(Map<String, Object> pa) {
	// provider.updateByOrgId(pa);
	// }

	public List<DictItem> queryCustdict(Map<String, Object> params) {
		return provider.queryCustdict(params);
	}

	public PageInfo<TyOrgcustomerBean> queryLatestModifiedRecords(String lastestTime, String pageNum, String pageSize) {
		return provider.queryLatestModifiedRecords(lastestTime, pageNum, pageSize);
	}
	
	public List<TyOrgcustomerBean> queryCustActive(Map<String, Object> params) {
		return provider.queryCustActive(params);
	}

	public int queryMyActiveCustCount(String staffId) {
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("deleteFlag", "0");
		params1.put("staffId", staffId);
		params1.put("activeDatetime", Integer.parseInt(DateUtil.format(DateUtil.addDate(new Date(), Calendar.DATE, -100), DateUtil.DATE_PATTERN.YYYYMMDD)));
//
		// TODO Auto-generated method stub
		return provider.queryMyActiveCustCount(params1);
	}


	public PageInfo<TyOrgcustomerBean> queryCustomerWithMarksByProc(Map<String, Object> params) {
		return provider.queryCustomerWithMarksByProc(params);
	}
	
	public List<TyOrgcustomerBean> queryByConditionFromProcResult(Map<String, Object> params) {
		return provider.queryByConditionFromProcResult(params);
	}

	public void updateServiceSaler(String id) {
		 provider.updateServiceSaler(id);
	}

	public void updateBatchMark(String oldLabelName, String newLabelName) {
		// TODO Auto-generated method stub
		provider.updateBatchMark(oldLabelName,newLabelName);
		//清除缓存
		RedisUtil.delAll(getCachePrefix()+"*");
		

	}
	
}
