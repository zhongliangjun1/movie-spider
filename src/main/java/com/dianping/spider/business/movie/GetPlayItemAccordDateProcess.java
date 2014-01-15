package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: PM2:12
 * To change this template use File | Settings | File Templates.
 *
 * http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=1&mid=&fyrq=2014-01-16
 */
public class GetPlayItemAccordDateProcess extends TemplateProcessor {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=%s&mid=&fyrq=%s";
    private String processName;
    private String url;
    private Crawler crawler;
    private CinemaGewaraBasic cinemaGewaraBasic;

    public GetPlayItemAccordDateProcess(String processName, CinemaGewaraBasic cinemaGewaraBasic, String time) {
        this.processName = processName;
        this.cinemaGewaraBasic = cinemaGewaraBasic;
        this.url = String.format(URL_TEMPLATE, cinemaGewaraBasic.getId(), time);
    }

    private void initCrawler() throws CrawlerInitFailureException {
        this.crawler = new AbstractCrawler(CrawlerInitType.AJAX_HTML, url){
            @Override
            public Object parse() {
                return null;
            }
        };
    }

    @Override
    protected void nameConfig() {
        super.name = this.processName;
    }

    @Override
    protected Object doWork(XDefaultContext context) {
        try {
            initCrawler();
        } catch (CrawlerInitFailureException e) {
            this.logger.error(e);
            return null;
        }
        return null;
    }


}
