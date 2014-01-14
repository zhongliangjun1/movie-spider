package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraDetail;
import com.dianping.spider.util.exception.CrawlerInitFailureException;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-14
 * Time: PM4:35
 * To change this template use File | Settings | File Templates.
 */
public class DetailCinemaCrawlerTest {


    public static void main(String[] args) throws CrawlerInitFailureException {
        CinemaGewaraBasic cinemaGewaraBasic = new CinemaGewaraBasic();
        cinemaGewaraBasic.setId(16);
        DetailCinemaCrawler crawler = new DetailCinemaCrawler(cinemaGewaraBasic);
        CinemaGewaraDetail cinemaGewaraDetail = crawler.parse();
        System.out.println(cinemaGewaraDetail.getIntroduction());
    }

}
