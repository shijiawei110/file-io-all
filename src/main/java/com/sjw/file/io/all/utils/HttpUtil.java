/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.sjw.file.io.all.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author shijw
 * @version HttpUtil.java, v 0.1 2018-12-12 11:40
 * @description http连接池
 */
public class HttpUtil {

    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 默认超时时间
     */
    private static final Integer DEFAULT_TIME_OUT = 10000;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connManager;

    /**
     * 初始化连接池管理器,配置SSL
     */
    static {
        try {
            // 创建ssl安全访问连接
            // 获取创建ssl上下文对象
            SSLContext sslContext = getSSLContext(true, null, null);

            // 注册
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();
            // ssl注册到连接池
            connManager = new PoolingHttpClientConnectionManager(registry);
            // 连接池最大连接数
            connManager.setMaxTotal(2000);
            // 每个路由最大连接数
            connManager.setDefaultMaxPerRoute(2000);
        } catch (Exception e) {
            LOGGER.error("初始化httpclient连接池管理器异常,e = {}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取客户端连接对象
     *
     * @param timeOut 超时时间
     * @return
     */
    private static CloseableHttpClient getHttpClient(Integer timeOut) {
        if (timeOut == null) {
            timeOut = DEFAULT_TIME_OUT;
        }
        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut).
                        setConnectTimeout(timeOut).
                        setSocketTimeout(timeOut).
                        build();
        // 配置超时回调机制
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                //重试1次
                if (executionCount >= 1) {
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return true;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    return false;
                }
                // ssl握手异常
                if (exception instanceof SSLException) {
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
//                .setRetryHandler(retryHandler)
                .build();
        return httpClient;
    }

    /**
     * 获取SSL上下文对象,用来构建SSL Socket连接
     *
     * @param isDeceive 是否绕过SSL
     * @param creFile   整数文件,isDeceive为true 可传null
     * @param crePwd    整数密码,isDeceive为true 可传null, 空字符为没有密码
     * @return SSL上下文对象
     * @throws Exception
     */
    private static SSLContext getSSLContext(boolean isDeceive, File creFile, String crePwd) throws Exception {
        SSLContext sslContext = null;
        if (isDeceive) {
            sslContext = SSLContext.getInstance("SSLv3");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }
            };
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
        } else {
            if (null != creFile && creFile.length() > 0) {
                if (null != crePwd) {
                    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keyStore.load(new FileInputStream(creFile), crePwd.toCharArray());
                    sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
                } else {
                    throw new SSLHandshakeException("httpclient SSL配置异常:整数密码为空");
                }
            }
        }
        return sslContext;

    }

    /**
     * post请求,支持SSL
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param timeout 超时时间ms
     * @return 响应信息
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params, Integer timeout) throws Exception {
        // 创建post请求
        HttpPost httpPost = new HttpPost(url);
        // 添加请求参数信息
        // 封装form
        Map<String, Object> form = Maps.newHashMapWithExpectedSize(8);
        if (null != params) {
            for (String s : params.keySet()) {
                form.put(s, params.get(s) == null ? null : params.get(s));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(form), ENCODING));
        }
        return getResult(httpPost, url, timeout, form);
    }

    /**
     * post请求,支持SSL
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应信息
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, null);
    }

    public static String postJson(String url, String body, Integer timeout) throws Exception {
        // 创建post请求
        HttpPost httpPost = new HttpPost(url);
        // 添加请求参数信息
        // 封装body
        httpPost.setEntity(new StringEntity(body));
        return getResult(httpPost, url, timeout, body);
    }

    public static String postJson(String url, String body) throws Exception {
        // 创建post请求
        HttpPost httpPost = new HttpPost(url);
        // 添加请求参数信息
        // 封装body
        httpPost.setEntity(new StringEntity(body,ENCODING));
        return getResult(httpPost, url, null, body);
    }

    public static String postJsonWithHeader(String url, String body) throws Exception {
        // 创建post请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type","application/json");
        // 添加请求参数信息
        // 封装body
        httpPost.setEntity(new StringEntity(body,ENCODING));
        return getResult(httpPost, url, null, body);
    }

    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet, url, null, null);
    }

    public static String getWithHeader(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        return getResult(httpGet, url, null, null);
    }

    private static String getResult(HttpRequestBase httpRequest, String url, Integer timeOut, Object body) {
        // 响应结果
        StringBuilder sb = null;
        CloseableHttpResponse response = null;
        // 获取连接客户端
        CloseableHttpClient httpClient = getHttpClient(timeOut);
        // 发起请求
        long start = System.currentTimeMillis();
//        LOGGER.info("开始执行POST请求,url={},params={}", url, JSON.toJSONString(form));
        try {
            response = httpClient.execute(httpRequest);
//            LOGGER.info("POST请求结束，url={},耗时{}毫秒", url, System.currentTimeMillis() - start);
            int respCode = response.getStatusLine().getStatusCode();
            // 正确响应
            if (HttpStatus.SC_OK == respCode) {
                // 获得响应实体
                HttpEntity entity = response.getEntity();
                sb = new StringBuilder();
                sb.append(EntityUtils.toString(entity, ENCODING));
                return sb.toString();
            }
        } catch (Exception e) {
            LOGGER.error("执行http请求异常,url={},params={},耗时{}秒,e={}", url, JSON.toJSONString(body),
                    (System.currentTimeMillis() - start) / 1000, ExceptionUtils.getStackTrace(e));
            //手动抛出异常
            throw new RuntimeException("httpUtil请求异常", e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.info("http util关闭响应连接出错，e = {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return null;
    }

    /**
     * Map转换成NameValuePair List集合
     *
     * @param params map
     * @return NameValuePair List集合
     */
    private static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        List<NameValuePair> paramList = new LinkedList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return paramList;
    }
}

