package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.support.DateUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: PM1:23
 * To change this template use File | Settings | File Templates.
 */
public class PlayItemCrawler implements Crawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private CinemaGewaraBasic cinemaGewaraBasic;

    public PlayItemCrawler(CinemaGewaraBasic cinemaGewaraBasic) {
        this.cinemaGewaraBasic = cinemaGewaraBasic;
    }

    private void assign(int numOfDays){
        if(numOfDays<=0)
            return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> times = DateUtils.getTimesFromNow(dateFormat, numOfDays);

    }

    @Override
    public Object parse() {
        return null;
    }

}
