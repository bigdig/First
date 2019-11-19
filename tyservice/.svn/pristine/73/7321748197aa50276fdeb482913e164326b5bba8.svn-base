package com.tfzq.generate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @author pengtao
 *
 */
public class TableDesign {
	private String tableName;//pb_user
	private String tableDesc;//用户
	private String beanName;//PbUser
	private String beanObj;//pbUser
	private List<Field> fields = new ArrayList<Field>();
	private List<Field> transFields = new ArrayList<Field>();
	private List<Field> filesFields = new ArrayList<Field>();
	private List<Field> searchFields = new ArrayList<Field>();
	private boolean hasDateField=false;
	private boolean hasDictField=false;
	private boolean hasEntityField=false;
	private boolean hasFileField=false;
	private boolean hasDelFlag=false;
	private boolean hasSearchDictField=false;

	public static String rootDir = "D:\\codegene\\src\\";
	public String codeDir = "";

	public String getCodeDir() {
		return codeDir;
	}

	public void setCodeDir(String codeDir) {
		this.codeDir = codeDir;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanObj() {
		return beanObj;
	}

	public void setBeanObj(String beanObj) {
		this.beanObj = beanObj;
	}

	public boolean isHasDateField() {
		return hasDateField;
	}

	public void setHasDateField(boolean hasDateField) {
		this.hasDateField = hasDateField;
	}

	public List<Field> getTransFields() {
		return transFields;
	}

	public void setTransFields(List<Field> transFields) {
		this.transFields = transFields;
	}

	public boolean isHasDictField() {
		return hasDictField;
	}

	public void setHasDictField(boolean hasDictField) {
		this.hasDictField = hasDictField;
	}

	public boolean isHasEntityField() {
		return hasEntityField;
	}

	public void setHasEntityField(boolean hasEntityField) {
		this.hasEntityField = hasEntityField;
	}

	public boolean isHasDelFlag() {
		return hasDelFlag;
	}

	public void setHasDelFlag(boolean hasDelFlag) {
		this.hasDelFlag = hasDelFlag;
	}

	public List<Field> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(List<Field> searchFields) {
		this.searchFields = searchFields;
	}

	public boolean isHasSearchDictField() {
		return hasSearchDictField;
	}

	public void setHasSearchDictField(boolean hasSearchDictField) {
		this.hasSearchDictField = hasSearchDictField;
	}

	public boolean isHasFileField() {
		return hasFileField;
	}

	public void setHasFileField(boolean hasFileField) {
		this.hasFileField = hasFileField;
	}

	public List<Field> getFilesFields() {
		return filesFields;
	}

	public void setFilesFields(List<Field> filesFields) {
		this.filesFields = filesFields;
	}

	public void addField(Field field) {
		this.fields.add(field);
		if(field.getJdbcType().equals("DATE") || field.getJdbcType().equals("TIMESTAMP")){
			this.hasDateField = true;
		}
		if(field.isDictFlag() ){
			this.transFields.add(field);
			this.hasDictField = true;
		}
		if(field.isEntityFlag() ){
			this.transFields.add(field);
			this.hasEntityField = true;
		}
		if(field.isFileFlag() ){
			this.filesFields.add(field);
			this.hasFileField = true;
		}
		if(field.isSearchFlag() ){
			this.searchFields.add(field);
			if(field.isDictFlag() && StringUtils.isNoneEmpty(field.getRef())){
				this.hasSearchDictField = true;
			}
		}
		if(field.getCode().equals("deleteFlag")){
			this.hasDelFlag = true;
		}

	}
}
