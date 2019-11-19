package org.ibase4j.model.sys;

import java.util.List;

import org.ibase4j.model.generator.SysDept;

@SuppressWarnings("serial")
public class SysDeptBean extends SysDept {
	private String channelTypeText;
	private List<SysDeptBean> children;

	public String getChannelTypeText() {
		return channelTypeText;
	}

	public void setChannelTypeText(String channelTypeText) {
		this.channelTypeText = channelTypeText;
	}

	public List<SysDeptBean> getChildren() {
		return children;
	}

	public void setChildren(List<SysDeptBean> children) {
		this.children = children;
	}

}
