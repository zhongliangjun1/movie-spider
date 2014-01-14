package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-14
 * Time: AM9:44
 * To change this template use File | Settings | File Templates.
 */
public class DetailCinemaCrawler extends AbstractCrawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/cinema/%s";
    private CinemaGewaraBasic cinemaGewaraBasic;
    private String url;


    public DetailCinemaCrawler(CinemaGewaraBasic cinemaGewaraBasic) throws CrawlerInitFailureException {
        super(CrawlerInitType.URL, String.format(URL_TEMPLATE, cinemaGewaraBasic.getId()));
        this.cinemaGewaraBasic = cinemaGewaraBasic;
        this.url = String.format(URL_TEMPLATE, cinemaGewaraBasic.getId());
    }

    @Override
    public Object parse() {
        try{


            return null;
        }catch (NullPointerException e){
            logger.error("dom changed : "+this.url, e);
            return null;
        }
    }


}
