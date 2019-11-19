package com.net263.contact.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/9/25
 * Time: 15:12
 * Package : com.net263.framework.tools.net
 * Company : 北京二六三企业通信有限公司
 */
@SuppressWarnings("all")
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL    请求地址(含参数)
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     */
    public static String sendGetRequest(String reqURL, String decodeCharset) {
        long responseLength = 0;       //响应长度
        String responseContent = null; //响应内容
        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
        HttpGet httpGet = new HttpGet(reqURL);           //创建org.apache.http.client.methods.HttpGet
        try {
            HttpResponse response = httpClient.execute(httpGet); //执行GET请求
            HttpEntity entity = response.getEntity();            //获取响应实体
            if (null != entity) {
                responseLength = entity.getContentLength();
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity); //Consume response content
            }
            System.out.println("请求地址: " + httpGet.getURI());
            System.out.println("响应状态: " + response.getStatusLine());
            System.out.println("响应长度: " + responseLength);
            System.out.println("响应内容: " + responseContent);
        } catch (ClientProtocolException e) {
            logger.debug("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
        } catch (ParseException e) {
            logger.debug(e.getMessage(), e);
        } catch (IOException e) {
            logger.debug("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); //关闭连接,释放资源
        }
        return responseContent;
    }


    /**
     * 发送HTTP_POST请求
     *
     * @param isEncoder 用于指明请求数据是否需要UTF-8编码,true为需要
     * @see 该方法为<code>sendPostRequest(String,String,boolean,String,String)</code>的简化方法
     * @see 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
     * @see 当<code>isEncoder=true</code>时,其会自动对<code>sendData</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,"UTF-8")</code>
     */
    public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder) {
        return sendPostRequest(reqURL, sendData, isEncoder, null, null);
    }


    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL        请求地址
     * @param sendData      请求参数,若有多个参数则应拼接成param11=value11&22=value22&33=value33的形式后,传入该参数中
     * @param isEncoder     请求数据是否需要encodeCharset编码,true为需要
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 当<code>isEncoder=true</code>时,其会自动对<code>sendData</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder, String encodeCharset, String decodeCharset) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(reqURL);
        //httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (String str : sendData.split("&")) {
                    formParams.add(new BasicNameValuePair(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1)));
                }
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8" : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(sendData));
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.debug("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }


    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(reqURL);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.debug("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * 使用httpClient发送请求
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static String sendPostBodyRequest(String url, String body) throws Exception {
        return sendPostBodyRequest(url, body, null);
    }


    /**
     * 使用httpClient发送请求
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static String sendPostBodyRequest(String url, String body, Map<String, String> heards) throws Exception {
        return sendPostBodyRequest(url, body, heards, null, null);
    }


    /**
     * 发送HTTP_POST请求,json格式数据
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static String sendPostBodyRequest(String url, String body, Map<String, String> heards, String encodeCharset, String decodeCharset) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost post = null;
        String resData = null;
        CloseableHttpResponse result = null;
        if (heards == null) {
            heards = new HashMap<String, String>();
        }
        heards.put("Content-Type", "application/json");
        try {
            post = new HttpPost(url);
            HttpEntity entity2 = new StringEntity(body, (encodeCharset == null ? Consts.UTF_8 : Charset.forName(encodeCharset)));
            post.setConfig(RequestConfig.custom().setConnectTimeout(300000).setSocketTimeout(300000).build());
            for (String heard : heards.keySet()) {
                post.setHeader(heard, heards.get(heard));
            }
            post.setEntity(entity2);
            result = httpclient.execute(post);
            if (HttpStatus.SC_OK == result.getStatusLine().getStatusCode()) {
                resData = EntityUtils.toString(result.getEntity(), decodeCharset == null ? "UTF-8" : decodeCharset);
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (post != null) {
                post.releaseConnection();
            }
            httpclient.close();
        }
        return resData;
    }


    /**
     * 发送HTTPS_POST请求
     *
     * @see 该方法为<code>sendPostSSLRequest(String,Map<String,String>,String,String)</code>方法的简化方法
     * @see 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,"UTF-8")</code>
     */
    public static String sendPostSSLRequest(String reqURL, Map<String, String> params) {
        return sendPostSSLRequest(reqURL, params, null, null);
    }


    /**
     * 发送HTTPS_POST请求
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @see 该方法会自动关闭连接,释放资源
     * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     */
    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset) {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{getX509TrustManager()}, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.debug("与[" + reqURL + "]通信过程中发生异常,堆栈信息为", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * https posp请求，可以绕过证书校验
     *
     * @param url
     * @param params
     * @return
     */
    public static final String sendPostHttpsRequest(String url, Map<String, String> params) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        //这个好像是HOST验证
        try {
            //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{getX509TrustManager()}, null);
            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            socketFactory.setHostnameVerifier(getX509HostnameVerifier());
            //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }


    /**
     * https post json请求，可以绕过证书校验
     *
     * @param url
     * @param body
     * @return
     */
    public static final String sendPostHttpsBodyRequest(String url, String body) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{getX509TrustManager()}, null);
            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            socketFactory.setHostnameVerifier(getX509HostnameVerifier());
            //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(body, Consts.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, Consts.UTF_8);
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }


    /**
     * 发送HTTP_Connect 请求
     *
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据,其数据类型为<code>java.util.Map<String, String></code>
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     * @see 若发送的<code>params</code>中含有中文,记得按照双方约定的字符集将中文<code>URLEncoder.encode(string,encodeCharset)</code>
     * @see 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     */
    public static String sendPostConnectRequest(String reqURL, Map<String, String> params) {
        StringBuilder sendData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sendData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (sendData.length() > 0) {
            sendData.setLength(sendData.length() - 1); //删除最后一个&符号
        }
        return sendPostConnectRequest(reqURL, sendData.toString());
    }


    /**
     * 使用httpURLConnection(java原生)发送post请求
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     * @see 若发送的<code>sendData</code>中含有中文,记得按照双方约定的字符集将中文<code>URLEncoder.encode(string,encodeCharset)</code>
     * @see 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     */
    public static String sendPostConnectRequest(String reqURL, String sendData) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        int httpStatusCode = 0;  //远程主机响应的HTTP状态码
        try {
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);        //指示应用程序要将数据写入URL连接,其值默认为false
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时
            //设置请求属性
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpURLConnection.setRequestProperty("Charset", "UTF-8");

            out = httpURLConnection.getOutputStream();
            out.write(sendData.toString().getBytes());

            //清空缓冲区,发送数据
            out.flush();

            //获取HTTP状态码
            httpStatusCode = httpURLConnection.getResponseCode();

            in = httpURLConnection.getInputStream();
            byte[] byteDatas = new byte[in.available()];
            in.read(byteDatas);
            return new String(byteDatas) + "`" + httpStatusCode;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return "Failed`" + httpStatusCode;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.debug("关闭输出流时发生异常,堆栈信息如下", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    logger.debug("关闭输入流时发生异常,堆栈信息如下", e);
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
    }


    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }


    private static X509HostnameVerifier getX509HostnameVerifier() {
        return new X509HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            public void verify(String arg0, SSLSocket arg1) throws IOException {
            }

            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
            }

            public void verify(String arg0, X509Certificate arg1) throws SSLException {
            }
        };
    }
}
