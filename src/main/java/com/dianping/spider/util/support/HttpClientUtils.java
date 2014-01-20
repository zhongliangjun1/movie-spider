package com.dianping.spider.util.support;

import com.dianping.spider.util.exception.IllegalParameterException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-17
 * Time: AM10:34
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientUtils {

    private static final Logger logger = Logger.getLogger(HttpClientUtils.class);

    private volatile static CloseableHttpClient httpclient; //= HttpClients.createDefault();
    private final static int MAX_TOTAL_CONNECTIONS = 800; // 最大连接数
    private final static int MAX_ROUTE_CONNECTIONS = 400; // 每个路由最大连接数

    private final static Map<String, HttpClientContext> httpClientContextMap = new ConcurrentHashMap<String, HttpClientContext>();

    public static CloseableHttpClient getHttpclientLazily(){
        if(httpclient==null){
            synchronized (HttpClientUtils.class){
                if(httpclient==null){
                    //HttpHost proxy = new HttpHost("192.168.8.87", 3128);
                    httpclient = HttpClients.custom().
                            setMaxConnPerRoute(MAX_ROUTE_CONNECTIONS).
                            setMaxConnTotal(MAX_TOTAL_CONNECTIONS).
                            //setProxy(proxy).
                            build();
                }
            }
        }
        return httpclient;
    }

    private static HttpClientContext getProbeHttpClientContext(String domain){

        if(StringUtils.isEmpty(domain))
            return null;

        HttpClientContext context = httpClientContextMap.get(domain);
        if(context==null){
            synchronized (HttpClientUtils.class){
                if(context==null){
                    context = HttpClientContext.create();
                    HttpGet httpGet = new HttpGet(domain);
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
                    httpGet.setConfig(requestConfig);
                    try {
                        getHttpclientLazily();
                        CloseableHttpResponse response = httpclient.execute(httpGet, context);
                        httpClientContextMap.put(domain, context);
                        response.close();
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }
            }
        }
        return context;
    }

    public static CloseableHttpResponse sendGet(String url){
        if(StringUtils.isEmpty(url))
            throw new IllegalParameterException();
        getHttpclientLazily();

        String domain = getDomain(url);
        HttpClientContext probeHttpClientContext = HttpClientUtils.getProbeHttpClientContext(domain);

        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(probeHttpClientContext.getCookieStore());

        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet, context);
            if(!checkUsability(response,url)){
                try {
                    //Thread.sleep(1000*60*8);
                    Thread.sleep(1000*45);
                    System.out.println("try again");
                } catch (InterruptedException e) {
                    logger.error(e);
                } finally {
                    response.close();
                }
                return sendGet(url);
            }
            return response;
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    private static boolean checkUsability(CloseableHttpResponse response, String url){
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode()==400 || statusLine.getStatusCode()==403){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = DateUtils.getTimesFromNow(sdf, 1).get(0);
            String msg = "forbid ip once at "+now+" "+statusLine.getReasonPhrase()+" | "+statusLine.getProtocolVersion();
            logger.error(msg+" | "+url);
            System.out.println(msg+" | "+url);
            return false;
        }else{
            return true;
        }
    }

    private static String getDomain(String url){
        String temp = "";
        if(url.indexOf("http://")==0){
            url = url.replaceAll("http://", "");
            temp = "http://";
        }else if(url.indexOf("https://")==0){
            url = url.replaceAll("https://", "");
            temp = "https://";
        }
        if(url.indexOf("/")<0){
            url = url + "/";
        }
        return temp + url.substring(0, url.indexOf("/"));
    }





    public static void main(String[] args) {

        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", "192.168.8.87");
        System.getProperties().setProperty("http.proxyPort", "3128");

        //getHttpclientLazily();
        String s = getDomain("http://www.gewara.com/cinema/");

        String ajaxURL = "http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=10&mid=&fyrq=2014-01-18";

        CloseableHttpResponse testResponse = sendGet(ajaxURL);

        HttpGet httpGet = new HttpGet(ajaxURL);
        HttpClientContext context = HttpClientContext.create();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet, context);

            try{
                HttpEntity entity = response.getEntity();
                ContentType contentType = ContentType.getOrDefault(entity);
                String charset = contentType.getCharset()!=null?contentType.getCharset().name():"UTF-8";
                byte[] bytes = EntityUtils.toByteArray(entity);
                String htmlStr = new String(bytes, charset);
            }finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
