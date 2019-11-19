package org.ibase4j.service.sys;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ibase4j.core.base.BaseService;
import org.ibase4j.core.support.SysEventService;
import org.ibase4j.core.support.dubbo.spring.annotation.DubboReference;
import org.ibase4j.core.util.DateUtil;
import org.ibase4j.core.util.ExceptionUtil;
import org.ibase4j.core.util.InstanceUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysEvent;
import org.ibase4j.provider.SysEventProvider;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class SysEventServiceImpl extends
		BaseService<SysEventProvider, SysEvent> implements SysEventService {
	@DubboReference
	public void setProvider(SysEventProvider provider) {
		this.provider = provider;
	}

	private ExecutorService executorService = Executors.newCachedThreadPool();

	public void saveEvent(final HttpServletRequest request,
			final HttpServletResponse response, final Exception ex,
			final Long startTime, final Long endTime) {
		final SysEvent record = new SysEvent();
		String uid = WebUtil.getCurrentUser();
		if (uid == null)
			uid = "visitor";
		record.setTitle("bizspace");
		record.setMethod(request.getMethod());
		record.setRequestUri(request.getServletPath());
		record.setClientHost(WebUtil.getHost(request));
		record.setUserAgent(request.getHeader("user-agent"));
		record.setParameters(JSON.toJSONString(filterInfo(request
				.getParameterMap())));
		record.setStatus(response.getStatus());
		record.setCreateBy(uid);
		record.setUpdateBy(uid);
		final String msg = (String) request.getAttribute("msg");

		executorService.submit(new Runnable() {
			public void run() {
				try { // 保存操作
					if (StringUtils.isNotBlank(msg)) {
						record.setRemark(msg);
					} else {
						record.setRemark(ExceptionUtil
								.getStackTraceAsString(ex));
					}
					Map<String, Object> params = InstanceUtil.newHashMap();
					params.put("permission_url", record.getRequestUri());

					add(record);
					// 内存信息
					if (logger.isDebugEnabled()) {
						String message = "开始时间: {}; 结束时间: {}; 耗时: {}s; URI: {}; ";
						// 最大内存: {}M; 已分配内存: {}M; 已分配内存中的剩余空间: {}M; 最大可用内存: {}M.
						// long total = Runtime.getRuntime().totalMemory() /
						// 1024 / 1024;
						// long max = Runtime.getRuntime().maxMemory() / 1024 /
						// 1024;
						// long free = Runtime.getRuntime().freeMemory() / 1024
						// / 1024;
						// , max, total, free, max - total + free
						logger.debug(message,
								DateUtil.format(startTime, "HH:mm:ss.SSS"),
								DateUtil.format(endTime, "HH:mm:ss.SSS"),
								(endTime - startTime) / 1000.00,
								record.getRequestUri());
					}
				} catch (Exception e) {
					logger.error("Save event log cause error :", e);
				}
			}
		});
	}

	/**
	 * 过滤日志中的敏感信息
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, String[]> filterInfo(Map<String, String[]> params) {
		if (params != null) {
			for (Entry<String, String[]> entry : params.entrySet()) {
				String key = entry.getKey();
				if (StringUtils.lowerCase(key).contains("password")
						|| StringUtils.lowerCase(key).contains("pwd")) {
					entry.setValue( new String[] { "******" });
				}
			}
		}
		return params;
	}
}