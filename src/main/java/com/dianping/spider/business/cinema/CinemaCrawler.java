package com.dianping.spider.business.cinema;

import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;

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
public class CinemaCrawler extends AbstractCrawler {


    public CinemaCrawler(CrawlerInitType crawlerInitType, Object param) throws CrawlerInitFailureException {
        super(crawlerInitType, param);
    }

    @Override
    public Object parse() {
        return null;
    }
}
