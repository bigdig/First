package com.tfzq.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CsvWriter extends BufferedOutputStream  {  
	  
    public CsvWriter(final OutputStream out) {  
        super(out);  
    }  
  
    public void writeLine(final List<String> csvLine) throws IOException {  
        StringBuffer sb = new StringBuffer();  
          
        for (int i = 0; i < csvLine.size(); i++) {  
            String line = csvLine.get(i);  
            if (line == null) {  
                line = "";  
            }  
            sb.append("\"").append(line.replaceAll("\"", "\"\"")).append("\",");  
        }  
        super.write(sb.deleteCharAt(sb.length() - 1).toString().getBytes("gbk"));  
        super.write("\n".getBytes("gbk"));
    }  
  
}   
