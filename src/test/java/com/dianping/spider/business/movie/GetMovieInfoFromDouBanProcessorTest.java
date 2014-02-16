package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-2-16
 * Time: PM4:06
 * To change this template use File | Settings | File Templates.
 */
public class GetMovieInfoFromDouBanProcessorTest {

    public static void main(String[] args) {
        String content = "2014-02-14(中国大陆)";
        content = content.substring(0, content.indexOf("("));

        int movieId = 139830172;
        String processName = String.format(ProcessName.GET_MOVIE_INFO_FROM_DOUBAN_PROCESS, movieId);
        MovieGewaraBasic movieGewaraBasic = new MovieGewaraBasic();
        movieGewaraBasic.setId(movieId);
        movieGewaraBasic.setName("冰雪奇缘");
//        movieGewaraBasic.setId(130463222);
//        movieGewaraBasic.setName("北京爱情故事");
        GetMovieInfoFromDouBanProcessor processor = new GetMovieInfoFromDouBanProcessor(processName, movieGewaraBasic);
        processor.doWork(null);
    }


}
