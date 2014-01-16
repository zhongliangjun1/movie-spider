package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.spider.util.support.ApplicationContextUtils;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-16
 * Time: PM2:28
 * To change this template use File | Settings | File Templates.
 */
public class PlayItemCrawlerTest {

    static {
        ApplicationContextUtils.init();
    }

    public static void main(String[] args) {
        CinemaGewaraBasic cinemaGewaraBasic = new CinemaGewaraBasic();
        cinemaGewaraBasic.setId(1);
        PlayItemCrawler crawler = new PlayItemCrawler(cinemaGewaraBasic);
        Map<String, Object> result = crawler.parse();
        System.out.println(result);
    }

}
