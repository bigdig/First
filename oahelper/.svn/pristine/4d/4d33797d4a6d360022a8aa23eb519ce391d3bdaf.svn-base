package org.ibase4j.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by yw on 2017/2/16.
 */
public class HttpClientUtil {
    public static String send(String url, Map<String, String> map, String encoding) {
        String body = "";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //设置参数到请求对象中
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("请求地址：" + url);
        System.out.println("请求参数：" + nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            try {
                body = EntityUtils.toString(entity, encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //释放链接
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
    
    /** 
     * 发送get请求 
     * @param url       链接地址 
     * @param charset   字符编码，若为null则默认utf-8 
     * @return 
     */  
    public static String doGet(String url,String charset){  
        if(null == charset){  
            charset = "utf-8";  
        }  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        HttpGet httpGet= null;  
        String result = null;  
          
        try {  
            httpGet = new HttpGet(url);  
              
            HttpResponse response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return result;  
    }  

}




