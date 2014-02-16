package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.spider.util.crawler.Crawler;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-2-15
 * Time: PM9:44
 * To change this template use File | Settings | File Templates.
 */
public class MovieInfoCrawler implements Crawler {

    private MovieGewaraBasic movieGewaraBasic;

    public MovieInfoCrawler(MovieGewaraBasic movieGewaraBasic) {
        this.movieGewaraBasic = movieGewaraBasic;
    }

    @Override
    public Object parse() {
        return null;
    }


}
