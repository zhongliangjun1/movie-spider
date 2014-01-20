package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraDetail;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.support.ApplicationContextUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-14
 * Time: PM4:35
 * To change this template use File | Settings | File Templates.
 */
public class DetailCinemaCrawlerTest {

    static {
        ApplicationContextUtils.init();
    }

    public static void main(String[] args) throws CrawlerInitFailureException {
        CinemaGewaraBasic cinemaGewaraBasic = new CinemaGewaraBasic();
        cinemaGewaraBasic.setId(32113697);
        DetailCinemaCrawler crawler = new DetailCinemaCrawler(cinemaGewaraBasic);
        CinemaGewaraDetail cinemaGewaraDetail = crawler.parse();
        System.out.println(cinemaGewaraDetail.getIntroduction());

        MovieService movieService = ApplicationContextUtils.getBean("movieService");
        boolean result = movieService.upsertCinemaGewaraDetail(cinemaGewaraDetail);
        System.out.println(result);
    }

}
