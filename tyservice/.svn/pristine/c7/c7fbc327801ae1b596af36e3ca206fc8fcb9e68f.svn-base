package com.tfzq.ty.model.generator;
import java.util.Date;

import org.ibase4j.core.base.BaseModel;

import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author pengtao 
 */
@SuppressWarnings("serial")
public class TyProjecttrack extends BaseModel{
    private String projectId;
    private String projectTrack;

	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectTrack() {
		return projectTrack;
	}
	public void setProjectTrack(String projectTrack) {
		this.projectTrack = projectTrack;
	}
	@JSONField( format = "yyyy-MM-dd" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}

}