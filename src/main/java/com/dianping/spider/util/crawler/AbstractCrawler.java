package com.dianping.spider.util.crawler;

import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.exception.IllegalParameterException;
import com.dianping.spider.util.support.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

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
        }

    }


    public Document getDoc() {
        return doc;
    }


    public static void main(String[] args) throws CrawlerInitFailureException {
        AbstractCrawler crawler = new AbstractCrawler(CrawlerInitType.URL, "http://www.gewara.com/movie/searchCinema.xhtml?cinemaIdList=&cinemaids=&characteristic=&ctype=&countycode=310115&lineall=&lineId=&hotcinema=&pairseat=&popcorn=&park=&refund=&acthas=&cinemaname=&order=") {
            @Override
            public Object parse() {
                return null;
            }
        };
        Document document = crawler.getDoc();
        System.out.println("end");
    }

}
