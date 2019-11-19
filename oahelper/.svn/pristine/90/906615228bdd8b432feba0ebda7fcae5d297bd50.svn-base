package com.tfzq.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSONObject;
import com.tfzq.pr.entity.PrNews;
import com.tfzq.pr.entity.PrEmotion;
import com.tfzq.pr.entity.PrMediaspread;
import com.tfzq.pr.entity.PrMonitoring;
import com.tfzq.pr.entity.PrOpiniontrend;
import com.tfzq.pr.entity.PrVitality;
import com.tfzq.pr.entity.PrWeektrend;
import com.tfzq.pr.entity.PrYlcount;
import com.tfzq.pr.entity.PrYqcount;


public class YqUtils {
	//获取token
	public static String getToken(String username,String secret)throws ClientProtocolException, IOException, URISyntaxException {
		Map<String, String> user = new HashMap<>();
		if(StringUtils.isNotEmpty(username)){
			user.put("username", username);
		}
		if(StringUtils.isNotEmpty(secret)){
			user.put("secret", secret);
		}
		String result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/gettoken.htm", user);
		JSONObject jsonParams1 = JSONObject.parseObject(result1);
		String access_token = ((JSONObject)jsonParams1.getJSONArray("data").get(0)).getString("token");
		//System.out.println(jsonParams1.toJSONString());
		return access_token;
	}
	//实时舆情，敏感通知，热点舆情
	private static List<PrNews> getNews(String token,String type,String data_time,String cat_id,int pagesize,int pagenum,String company_id)throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		Map<String, String> params = new HashMap<>();
		List<PrNews> newsList = new ArrayList<>();
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		JSONObject newsObj = new JSONObject();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(type)){
			params.put("type", type);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(cat_id)){
			params.put("cat_id", cat_id);
		}
		if(pagesize<=0){
			pagesize = 100;
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("pagesize", String.valueOf(pagesize));
		params.put("pagenum", String.valueOf(pagenum));
		//do{
			result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
			jsonParams1 = JSONObject.parseObject(result1);
			System.out.println(result1);
			if(jsonParams1.getJSONArray("data").size()!=0){
				for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
					PrNews news = new PrNews();
					newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
					news.setAbstractstr(newsObj.getString("abstractStr"));
					news.setCatIcon(newsObj.getString("cat_icon"));
					news.setCatId(newsObj.getString("cat_id"));
					news.setCatName(newsObj.getString("cat_name"));
					news.setCompanyCode(newsObj.getString("company_code"));
					news.setCompanyId(newsObj.getString("company_id"));
					news.setCompanyName(newsObj.getString("company_name"));
					news.setId(newsObj.getString("id"));
					news.setNewsId(newsObj.getString("news_id"));
					news.setNewsMedia(newsObj.getString("news_media"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					news.setNewsTime(sdf.parse(newsObj.getString("news_time")));
					news.setTitle(newsObj.getString("title"));
					news.setTransmit(newsObj.getString("transmit"));
					newsList.add(news);
				}
			}
		//}while(jsonParams1.getJSONArray("data").size()!=0);
		return newsList;
	}
	//实时舆情
	public static List<PrNews> realYq(String token,String data_time,String cat_id,int pagesize,int pagenum,String company_id)throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		return getNews(token,"8",data_time,cat_id,pagesize,pagenum,company_id);
	}
	//敏感通知
	public static List<PrNews> noticeYq(String token,String data_time,String cat_id,int pagesize,int pagenum,String company_id)throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		return getNews(token,"2",data_time,cat_id,pagesize,pagenum,company_id);
	}
	//热点舆情
	public static List<PrNews> hotYq(String token,String data_time,String cat_id,int pagesize,int pagenum,String company_id)throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		return getNews(token,"3",data_time,cat_id,pagesize,pagenum,company_id);
	}
	//一周走势
	public static List<PrWeektrend> Weektrend(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "4");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		System.out.println(result1);
		List<PrWeektrend> trend = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		int size = jsonParams1.getJSONArray("data").size();
		for(int i = 0;i<size;i++){
			PrWeektrend newsTrend = new PrWeektrend();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			newsTrend.setAmount(newsObj.getString("amount"));
			newsTrend.setYmd(newsObj.getString("ymd"));
			newsTrend.setCompanyId(company_id);
			newsTrend.setBusiDate(((JSONObject)jsonParams1.getJSONArray("data").get(size-1)).getString("ymd"));
			trend.add(newsTrend);
		}
		return trend;
	}
	//舆情走势
	public static List<PrOpiniontrend> yqTrend(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "41");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		System.out.println(jsonParams1.toString());
		List<PrOpiniontrend> yqTrendList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		int size = jsonParams1.getJSONArray("data").size();
		for(int i = 0;i<size;i++){
			PrOpiniontrend yqtrend = new PrOpiniontrend();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			yqtrend.setDataAmount(newsObj.getString("data_amount"));
			yqtrend.setCatNote(newsObj.getString("cat_note"));
			yqtrend.setYmd(jsonParams1.getString("ymd"));
			yqtrend.setCompanyId(company_id);
			int length = (jsonParams1.getString("ymd")).split(",").length;
			yqtrend.setBusiDate(((jsonParams1.getString("ymd")).split(","))[length-1]);
			yqTrendList.add(yqtrend);
		}
		return yqTrendList;
	}
	//舆情统计
	public static List<PrYqcount> yqCount(String token,String data_time,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "5");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		System.out.println(result1);
		List<PrYqcount> yqList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
			PrYqcount count = new PrYqcount();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			count.setAmount(newsObj.getString("amount"));
			count.setCatName(newsObj.getString("cat_name"));
			count.setCatId(newsObj.getString("cat_id"));
			count.setAmount30(newsObj.getString("amount_30"));
			count.setAmount7(newsObj.getString("amount_7"));
			count.setCompanyId(company_id);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(new Date()); 
			count.setBusiDate(str);
			yqList.add(count);
		}
		return yqList;
	}
	//媒体分布
	public static List<PrMediaspread> mediaSpread(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "17");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		System.out.println(jsonParams1.toString());
		List<PrMediaspread> msList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
			PrMediaspread meSpread = new PrMediaspread();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			meSpread.setAmount(newsObj.getString("amount"));
			meSpread.setCatId(newsObj.getString("cat_id"));
			meSpread.setCatNote(newsObj.getString("cat_note"));
			meSpread.setPercent(newsObj.getString("percent"));
			meSpread.setCompanyID(company_id);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(new Date()); 
			meSpread.setBusiDate(str);
			msList.add(meSpread);
		}			
		return msList;
	}
	//情感属性
	public static List<PrEmotion> getEmotion(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "12");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		List<PrEmotion> emList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
			PrEmotion emotion = new PrEmotion();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			emotion.setAmount(newsObj.getString("amount"));
			emotion.setCatNote(newsObj.getString("cat_note"));
			emotion.setCatId(newsObj.getString("cat_id"));
			emotion.setCompanyId(newsObj.getString("company_id"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(new Date()); 
			emotion.setBusiDate(str);
			emList.add(emotion);
		}
		return emList;
	}
	//舆论统计表
	public static List<PrYlcount> getOpinion(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "16");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		jsonParams1 = JSONObject.parseObject(result1);
		System.out.println(jsonParams1.toString());
		
		List<PrYlcount> ylList = new ArrayList<>();
		for(int i = 0;i<((JSONObject)jsonParams1.getJSONArray("data").get(0)).getJSONArray("trends").size();i++){
			PrYlcount prYlcount = new PrYlcount();
			JSONObject newsObj = new JSONObject();
			newsObj = (JSONObject)((JSONObject)jsonParams1.getJSONArray("data").get(0)).getJSONArray("trends").get(i);			
			prYlcount.setCatAmount1(newsObj.getString("cat_amount_1"));
			prYlcount.setCatAmount16(newsObj.getString("cat_amount_16"));
			prYlcount.setCatAmount45(newsObj.getString("cat_amount_45"));
			prYlcount.setCatAmount46(newsObj.getString("cat_amount_46"));
			prYlcount.setKeyWords(newsObj.getString("key_words"));
			prYlcount.setNum(jsonParams1.getString("num"));
			prYlcount.setResponseIp(jsonParams1.getString("response_ip"));
			prYlcount.setStatus(jsonParams1.getString("status"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(new Date()); 
			prYlcount.setBusiDate(str);
			ylList.add(prYlcount);
		}
		return ylList;
	}
	//媒体活跃度
	public static List<PrVitality> getVitality(String token,String data_time,String Interval,String company_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		if(StringUtils.isNotEmpty(data_time)){
			params.put("data_time", data_time);
		}
		if(StringUtils.isNotEmpty(Interval)){
			params.put("Interval", Interval);
		}
		if(StringUtils.isNotEmpty(company_id)){
			params.put("company_id", company_id);
		}
		params.put("type", "19");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		System.out.println(result1);
		jsonParams1 = JSONObject.parseObject(result1);
		List<PrVitality> viList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
			PrVitality vitality = new PrVitality();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			vitality.setAmount(newsObj.getString("amount"));
			vitality.setNewsMedia(newsObj.getString("news_media"));
			vitality.setCompanyId(company_id);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(new Date()); 
			vitality.setBusiDate(str);
			viList.add(vitality);
		}
		return viList;
	}
	//监测方案
	public static List<PrMonitoring> Monitoring(String token) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Map<String, String> params = new HashMap<>();
		if(StringUtils.isNotEmpty(token)){
			params.put("token", token);
		}
		params.put("type", "7");
		String result1 = new String();
		JSONObject jsonParams1 = new JSONObject();
		result1 = HttpClient.post("http://api.capitalink.cn/ddjtapi/general.htm", params);
		System.out.println(result1);
		jsonParams1 = JSONObject.parseObject(result1);
		List<PrMonitoring> moList = new ArrayList<>();
		JSONObject newsObj = new JSONObject();
		for(int i = 0;i<jsonParams1.getJSONArray("data").size();i++){
			PrMonitoring prMonitoring = new PrMonitoring();
			newsObj = (JSONObject)jsonParams1.getJSONArray("data").get(i);
			prMonitoring.setId(newsObj.getString("company_id"));
			prMonitoring.setDataText(newsObj.getString("name"));
			prMonitoring.setDataType("3");
			moList.add(prMonitoring);
		}
		return moList;
	}
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		// TODO 自动生成的方法存根
		String token = getToken("ddjt","ddjt123");
		List<PrVitality> list = new ArrayList<>();
		List<PrMonitoring> list1 = Monitoring(token);
		for(int i=0;i<list1.size();i++){
			list = getVitality(token,null,null,list1.get(i).getId());
			for(int j = 0;j<list.size();j++){
				System.out.println(list.get(j));
			}
		}		
	}

}
