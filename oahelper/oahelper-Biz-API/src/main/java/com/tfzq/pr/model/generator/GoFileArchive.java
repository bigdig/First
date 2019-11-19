package com.tfzq.pr.model.generator;
import org.ibase4j.core.base.BaseModel;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
 *   @author yuzhitao 
 */
@SuppressWarnings("serial")
public class GoFileArchive extends BaseModel{
    private String fileName;
    private String fileDescription;
    private String fileCatalog;
    private String filePath;
    private Integer fileWeight;
    
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return this.fileName;
    }
    public void setFileDescription(String fileDescription){
        this.fileDescription = fileDescription;
    }
    public String getFileDescription(){
        return this.fileDescription;
    }
    public void setFileCatalog(String fileCatalog){
        this.fileCatalog = fileCatalog;
    }
    public String getFileCatalog(){
        return this.fileCatalog;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public String getFilePath(){
        return this.filePath;
    }
    public void setFileWeight(Integer fileWeight){
        this.fileWeight = fileWeight;
    }
    public Integer getFileWeight(){
        return this.fileWeight;
    }
    
    @JSONField( format = "yyyy-MM-dd HH:mm:ss" )
	public Date getCreateTime() {
		return super.getCreateTime();
	}
}