package com.xxl.job.executor.service.jobhandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tfzq.pr.entity.InfAlbum;
import com.tfzq.pr.entity.InfChannelcontent;
import com.tfzq.pr.entity.InfContent;
import com.tfzq.pr.service.InfAlbumService;
import com.tfzq.pr.service.InfChannelcontentService;
import com.tfzq.pr.service.InfContentService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="tfzqNewsJobHandler")
@Component
public class TfzqNewsJobHandler extends IJobHandler {
	@Autowired
	private InfAlbumService infAlbumService;
	@Autowired
	private InfContentService infContentService;
	@Autowired
	private InfChannelcontentService infChannelcontentService;
	
    @Value("${tfzqnews.subjectId}")
	private String subjectId;
    @Value("${tfzqnews.channelNumber}")
    private String channelNumber;
    @Value("${tfzqnews.channelId}")
    private String channelId;
    @Value("${tfzqnews.channelName}")
    private String channelName;
    @Value("${tfzqnews.companyId}")
    private String companyId;
    @Value("${tfzqnews.createBy}")
    private String createBy;//"2017122015343107100011"
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		String page="1";
		if(StringUtils.isNotEmpty(param)){
			page = param;
		}
		String host = "http://www.tfzq.com";
    	String url = "http://www.tfzq.com/about/news.html?page="+page;
    	Document doc = Jsoup.connect(url).get();
    	Elements divs = doc.select(".m_news_li");
    	for (Element element : divs) {
    		String imgUrl = element.select(".news_img img").attr("src");
    		String href = element.select("a").attr("href");
    		String title= element.select(".news-item-title").text();
    		String summary = element.select(".news-item-degist").text();
    		
    		Map<String,Object> params = new HashMap<String,Object>();
    		params.put("title", title);
    		List<InfContent> cs = infContentService.findByCondition(params);
    		if(cs.size()==0){
    			InfContent content = new InfContent();
    			content.setThumbnail(host+imgUrl);
    			content.setRemarks(host +href);
    			content.setTitle(title);
    			content.setSummary(summary);
    			content.setSubjectId(subjectId);
    			content.setStatus("5");
    			content.setEditmodelId("2");
    			content.setCompanyId(companyId);
    			content.setDeleteFlag("0");
    			content.setCreateBy(createBy);
    			content.setUpdateBy(createBy);
    			InfContent result = infContentService.insert(content);
    			
    			InfAlbum album = new InfAlbum();
    			album.setContentId(result.getId());
    			album.setSubjectId(subjectId);
    			album.setCreateBy(createBy);
    			album.setUpdateBy(createBy);
    			infAlbumService.insert(album);
    			
    			InfChannelcontent channel = new InfChannelcontent();
    			channel.setContentId(result.getId());
    			channel.setChannelId(channelId);
    			channel.setChannelNumber(Integer.parseInt(channelNumber));
    			channel.setChannelName(channelName);
    			channel.setCompanyId(companyId);
    			channel.setPublishCount(1);
    			channel.setUseStatus("0");
    			channel.setDeleteFlag("0");
    			channel.setCreateBy(createBy);
    			channel.setUpdateBy(createBy);
    			infChannelcontentService.insert(channel);
    		}
    	}

		XxlJobLogger.log("tfzq news:"+divs.size());

		return SUCCESS;
	}

}
