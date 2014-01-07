package com.dianping.spider.business.cinema;

import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-7
 * Time: PM3:34
 * To change this template use File | Settings | File Templates.
 *
 * http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=310115  第一页
 * http://www.gewara.com/shanghai/movie/searchCinema.xhtml?pageNo=1&countycode=310115 第二页
 *
 *
 */
public class CinemaCrawler implements Crawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String FIRST_PAGE_URL_TEMPLATE = "http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=%s";
    private static final String OTHER_PAHE_URL_TEMPLATE = "http://www.gewara.com/shanghai/movie/searchCinema.xhtml?pageNo=%s&countycode=%s";
    private int firstDistrictId;


    public CinemaCrawler(int firstDistrictId) {
        this.firstDistrictId = firstDistrictId;
    }

    private int getPageNum(){

        return 0;
    }

    @Override
    public Object parse() {


        return null;
    }
}
