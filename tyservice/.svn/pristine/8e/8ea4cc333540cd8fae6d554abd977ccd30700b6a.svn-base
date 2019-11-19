package com.tfzq.generate;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author pengtao
 *
 */
public class Field {

	private String code;
	private String column;
	private String name;
	private String dbType;
	private String javaType;
	private String jdbcType;
	private int length;
	private int precision;
	private boolean dictFlag;
	private boolean entityFlag;
	private boolean fileFlag;
	private boolean skipInputFlag;
	private boolean skipViewFlag;
	private boolean searchFlag;
	private String ref;
	private String refObj;
	private String idField;
	private String nameField;
	private int size;

	public Field(String column, String dbType, String name, String control,String search) {
		super();
		this.skipInputFlag = false;
		this.skipViewFlag = false;

		this.column = column;
		this.code = Utils.calBeanObjName(Utils.calBeanName(this.column));
		this.name = name;
		this.dbType = StringUtils.removeEnd(StringUtils.removeStart(dbType, "\""), "\"");
		if (this.dbType.startsWith("integer")) {
			this.dbType = "number";
		} else if (this.dbType.startsWith("number")) {
			String lenAndPre = StringUtils.substringBetween(this.dbType, "(", ")");
			if(lenAndPre.indexOf(",")>-1){
				this.length = Integer.parseInt(StringUtils.substringBefore(lenAndPre, ","));
				this.precision = Integer.parseInt(StringUtils.substringAfter(lenAndPre, ","));				
			}else{
				this.length = Integer.parseInt(lenAndPre);
			}
			this.dbType = "number";
		} else if (this.dbType.startsWith("varchar2") || this.dbType.startsWith("varchar")) {
			this.length = Integer.parseInt(StringUtils.substringBetween(this.dbType, "(", ")"));
			this.dbType = StringUtils.substringBefore(this.dbType, "(");
		}
		this.javaType = calJavaType();
		this.jdbcType = calJdbcType();
		if(StringUtils.startsWith(control, "dict")){
			this.dictFlag = true;
			String left =StringUtils.substringAfter(control, "=");
			this.ref = StringUtils.upperCase(left);
			this.refObj = StringUtils.lowerCase(left)+"Map";
			this.idField = this.code;
			this.nameField = this.code + "Text";
		}
		if(StringUtils.startsWith(control, "entity")){
			this.entityFlag = true;
			String left =StringUtils.substringAfter(control, "=");
			String tableName = StringUtils.substringBefore(left, ":");
			String idAndNam = StringUtils.substringAfter(left, ":");
			this.ref = Utils.calBeanName(tableName);
			this.refObj = Utils.calBeanObjName(this.ref);
			this.idField = Utils.calBeanObjName(Utils.calBeanName(StringUtils.substringBefore(idAndNam, ",")));
			this.nameField = Utils.calBeanObjName(Utils.calBeanName(StringUtils.substringAfter(idAndNam, ",")));
		}
		if(StringUtils.startsWith(control, "file=")){
			this.fileFlag = true;
			String left =StringUtils.substringAfter(control, "=");
			this.ref = Utils.calBeanObjName(Utils.calBeanName(StringUtils.substringBefore(left, ",")));
			String size_str = StringUtils.substringAfter(left, ",");
			this.size = StringUtils.isEmpty(size_str)?1:Integer.parseInt(size_str);
		}
		if(StringUtils.equals(control, "skip")){
			this.skipInputFlag = true;
			this.skipViewFlag = true;
		}
		if(StringUtils.startsWith(control, "skipinput")){
			this.skipInputFlag = true;
		}
		if(StringUtils.startsWith(control, "skipview")){
			this.skipViewFlag = true;
		}
		if(StringUtils.equals(search, "y")){
			this.searchFlag=true;
		}
	}

	public boolean isSkipViewFlag() {
		return skipViewFlag;
	}


	public void setSkipViewFlag(boolean skipViewFlag) {
		this.skipViewFlag = skipViewFlag;
	}


	public boolean isSkipInputFlag() {
		return skipInputFlag;
	}

	public void setSkipInputFlag(boolean skipInputFlag) {
		this.skipInputFlag = skipInputFlag;
	}

	public boolean isFileFlag() {
		return fileFlag;
	}

	public void setFileFlag(boolean fileFlag) {
		this.fileFlag = fileFlag;
	}

	public String getRefObj() {
		return refObj;
	}

	public void setRefObj(String refObj) {
		this.refObj = refObj;
	}

	public boolean isEntityFlag() {
		return entityFlag;
	}

	public void setEntityFlag(boolean entityFlag) {
		this.entityFlag = entityFlag;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public boolean isDictFlag() {
		return dictFlag;
	}

	public void setDictFlag(boolean dictFlag) {
		this.dictFlag = dictFlag;
	}

	public Field() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String jType) {
		this.javaType = jType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(boolean searchFlag) {
		this.searchFlag = searchFlag;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String calJavaType() {
		if (StringUtils.equalsIgnoreCase("char", dbType) || StringUtils.equalsIgnoreCase("varchar", dbType) || StringUtils.equalsIgnoreCase("varchar2", dbType)
				|| StringUtils.equalsIgnoreCase("clob", dbType)) {
			return "String";
		}
		if (StringUtils.equalsIgnoreCase("integer", dbType) || (StringUtils.equalsIgnoreCase("number", dbType) && precision == 0)) {
			/*
			 * if(length>9){ return "BigInteger"; }
			 */
			return "Integer";
		}
		if (StringUtils.equalsIgnoreCase("number", dbType) && precision > 0) {
			return "Double";
		}
		if (StringUtils.equalsIgnoreCase("timestamp", dbType) || StringUtils.equalsIgnoreCase("date", dbType)) {
			return "Date";
		}
		if (StringUtils.equalsIgnoreCase("float", dbType)) {
			return "Float";
		}
		if (StringUtils.equalsIgnoreCase("long", dbType)) {
			return "Long";
		}
		return null;
	}

	public String calJdbcType() {
		if (StringUtils.equalsIgnoreCase("char", dbType)) {
			return "CHAR";
		}
		if (StringUtils.equalsIgnoreCase("varchar", dbType) || StringUtils.equalsIgnoreCase("varchar2", dbType)) {
			return "VARCHAR";
		}
		if (StringUtils.equalsIgnoreCase("clob", dbType)) {
			return "CLOB";
		}
		if (StringUtils.equalsIgnoreCase("integer", dbType) || (StringUtils.equalsIgnoreCase("number", dbType) && precision == 0)) {
			return "INTEGER";
		}
		if (StringUtils.equalsIgnoreCase("number", dbType) && precision > 0) {
			return "DOUBLE";
		}
		if (StringUtils.equalsIgnoreCase("timestamp", dbType)) {
			return "TIMESTAMP";
		}
		if (StringUtils.equalsIgnoreCase("date", dbType)) {
			return "DATE";
		}
		if (StringUtils.equalsIgnoreCase("long", dbType)) {
			return "LONG";
		}
		if (StringUtils.equalsIgnoreCase("binary_double", dbType)) {
			return "DOUBLE";
		}
		if (StringUtils.equalsIgnoreCase("binary_float", dbType)) {
			return "Float";
		}
		return null;
	}

	@Override
	public String toString() {
		return "Field [code=" + code + ", column=" + column + ", name=" + name + ", dbType=" + dbType + ", javaType=" + javaType + ", jdbcType=" + jdbcType
				+ ", length=" + length + ", precision=" + precision + ", dictFlag=" + dictFlag + ", entityFlag=" + entityFlag + ", ref=" + ref + ", refObj="
				+ refObj + ", idField=" + idField + ", nameField=" + nameField + "]";
	}

}
