package com.dianping.spider.util.crawler;

import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.exception.IllegalParameterException;
import com.dianping.spider.util.support.HttpClientUtils;
import com.dianping.spider.util.support.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM9:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCrawler implements Crawler {

    protected final Logger logger = Logger.getLogger(this.getClass());

    protected Document doc;

    public AbstractCrawler(CrawlerInitType crawlerInitType, Object param) throws CrawlerInitFailureException {

        if(crawlerInitType==null || param==null)
            throw new IllegalParameterException();

        switch (crawlerInitType) {

            case URL:
                if(!(param instanceof String))
                    throw new IllegalParameterException();
                String url = (String) param;
                if(StringUtils.isEmpty(url))
                    throw new IllegalParameterException();
                try {
                    this.doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    throw new CrawlerInitFailureException("can not init Crawler with this url:"+url, e);
                }
                break;

            case HTML:
                if(!(param instanceof String))
                    throw new IllegalParameterException();
                String html = (String) param;
                if(StringUtils.isEmpty(html))
                    throw new IllegalParameterException();
                this.doc = Jsoup.parse(html);
                break;

            case FILE:
                if(!(param instanceof File))
                    throw new IllegalParameterException();
                File file = (File) param;
                try {
                    this.doc = Jsoup.parse(file, "UTF-8");
                } catch (IOException e) {
                    throw new CrawlerInitFailureException("can not init Crawler with this file", e);
                }
                break;

            case AJAX_HTML:
                if(!(param instanceof String))
                    throw new IllegalParameterException();
                String ajaxURL = (String) param;
                if(StringUtils.isEmpty(ajaxURL))
                    throw new IllegalParameterException();
                try {
                    CloseableHttpResponse response = HttpClientUtils.sendGet(ajaxURL);
                    try{
                        HttpEntity entity = response.getEntity();
                        ContentType contentType = ContentType.getOrDefault(entity);
                        String charset = contentType.getCharset()!=null?contentType.getCharset().name():"UTF-8";
                        byte[] bytes = EntityUtils.toByteArray(entity);
                        String htmlStr = new String(bytes, charset);
                        this.doc = Jsoup.parse(htmlStr);
                    }finally {
                        response.close();
                    }
                } catch (Exception e){
                    throw new CrawlerInitFailureException("can not init Crawler with this url:"+ajaxURL, e);
                }
                break;
        }

    }


    public Document getDoc() {
        return doc;
    }




}
