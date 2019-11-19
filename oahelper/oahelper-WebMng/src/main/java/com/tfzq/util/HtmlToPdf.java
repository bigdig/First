package com.tfzq.util;

import java.io.File;

import org.ibase4j.core.util.PropertiesUtil;

public class HtmlToPdf {  
    private static final String toPdfTool = PropertiesUtil.getString("pdf_tool_path");// Constants.Pdf_Tool_Path;  
      
    /** 
     * html转pdf 
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径 
     * @param destPath pdf保存路径 
     * @return 转换成功返回true 
     */  
    public static boolean convert(String srcPath1, String destPath){ 
    	boolean result = true;  
    	try{ 
    		String srcPath=new String(srcPath1.getBytes("ISO-8859-1"),"UTF-8");
            File file = new File(destPath);  
            File parent = file.getParentFile();  
            //如果pdf保存路径不存在，则创建路径  
            if(!parent.exists()){  
                parent.mkdirs();  
            }  
              
            StringBuilder cmd = new StringBuilder();  
            cmd.append(toPdfTool);  
            cmd.append(" ");  
            cmd.append(srcPath);  
            cmd.append(" ");  
            cmd.append(destPath);  
    		Runtime.getRuntime().exec(cmd.toString());                     
        }catch(Exception e){  
            result = false;  
            e.printStackTrace();  
        }            
        return result;       
    }
    public static void main(String[] args) throws Exception {
    	convert("www.baidu.com",  "d:\\1\\1.dpf");
    }
}  
