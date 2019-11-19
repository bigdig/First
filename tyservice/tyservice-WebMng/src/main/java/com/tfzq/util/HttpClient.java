package com.tfzq.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClient {
    // private static final Logger log = Logger.getLogger(HttpClient.class);

    public static String post(String url, Map<String, String> files, Map<String, String> params) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            if (files != null && !files.isEmpty()) {
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    FileBody bin = new FileBody(new File(entry.getValue()));
                    builder.addPart(entry.getKey(), bin);
                }
            }

            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    StringBody comment = new StringBody(entry.getValue(), ContentType.TEXT_PLAIN);
                    builder.addPart(entry.getKey(), comment);
                }
            }

            HttpEntity reqEntity = builder.build();
            httppost.setEntity(reqEntity);

            // log.debug("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // log.debug(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    // log.debug("Response content length: " +
                    // resEntity.getContentLength());
                }
                String retStr = EntityUtils.toString(resEntity);
                // log.debug(retStr);
                EntityUtils.consume(resEntity);// ?
                return retStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 
     * @param url 请求服务器地址
     * @param params 参数列表
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String get(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httppost = new HttpGet();
            RequestConfig requestConfig = RequestConfig.custom().
            		setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);
            StringBuilder sb = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    } else {
                        sb.append("?");
                    }
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
            }
            sb.insert(0, url);
            httppost.setURI(new URI(sb.toString()));

//            log.debug("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
//                log.debug(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
//                    log.debug("Response content length: " + resEntity.getContentLength());
                }
                String retStr = EntityUtils.toString(resEntity);
//                log.debug(retStr);
                EntityUtils.consume(resEntity);// ?
                return retStr;
            } finally {
                response.close();
            }
        } catch(Exception e){
            e.printStackTrace();
            return null;
        } finally {
            httpclient.close();
        }
    }
    
    /**
     * 
     * @param url 请求服务器地址
     * @param params 参数列表
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().
            		setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httppost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
            }

//            log.debug("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
//                log.debug(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
//                    log.debug("Response content length: " + resEntity.getContentLength());
                }
                String retStr = EntityUtils.toString(resEntity);
//                log.debug(retStr);
                EntityUtils.consume(resEntity);// ?
                return retStr;
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }


    public static String postJson(String url, String jsonStr) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().
            		setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            httppost.setEntity(new StringEntity(jsonStr, ContentType.APPLICATION_JSON));

            // log.debug("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // log.debug(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    // log.debug("Response content length: " +
                    // resEntity.getContentLength());
                }
                String retStr = EntityUtils.toString(resEntity);
                System.out.println(retStr);
                EntityUtils.consume(resEntity);// ?
                return retStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    public static void main(String[] args) throws Exception{
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("openid", "1117");// openid
        jsonParams.put("channelid", "2227");// 频道id
        jsonParams.put("pointReceiveDesc", "测试连通");// 积分获取详情

        //String url =  SystemConfig.getString("integralServiceUrl") + "ajax/insertPoint";
        String url="http://app27.s.weshaketv.com:9158/jifenservice/ajax/insertPoint";
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("jsonStr", jsonParams.toJSONString());
        params.put("callback", "1");

        HttpClient.post(url, params);

    }

}
