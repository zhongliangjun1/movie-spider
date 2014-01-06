package com.dianping.spider.util.crawler;

import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.exception.IllegalParameterException;
import com.dianping.spider.util.support.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM9:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class Crawler {

    private Document doc;

    public Crawler(CrawlerInitType crawlerInitType, Object param) throws CrawlerInitFailureException {

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
        }

    }


    public abstract Object parse();

    public Document getDoc() {
        return doc;
    }
}
