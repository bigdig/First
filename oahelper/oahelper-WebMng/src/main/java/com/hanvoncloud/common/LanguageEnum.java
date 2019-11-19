package com.hanvoncloud.common;

public enum LanguageEnum
{
  CHNS("chns", "中文简体"),  CHNT("chnt", "中文繁体"),  EN("en", "英文"),  JPN("jpn", "日文"),  AUTO("auto", "自动");
  
  private String code;
  private String description;
  
  private LanguageEnum(String code, String description)
  {
    this.code = code;
    this.description = description;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
}
