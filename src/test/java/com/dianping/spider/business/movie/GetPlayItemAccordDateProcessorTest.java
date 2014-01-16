package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-16
 * Time: PM1:53
 * To change this template use File | Settings | File Templates.
 */
public class GetPlayItemAccordDateProcessorTest {

    public static void main(String[] args) {
        CinemaGewaraBasic cinemaGewaraBasic = new CinemaGewaraBasic();
        cinemaGewaraBasic.setId(1);
        GetPlayItemAccordDateProcessor process = new GetPlayItemAccordDateProcessor(
                ProcessName.GET_CINEMA_PLAY_ITEM_ACCORD_DATE_PROCESS, cinemaGewaraBasic, "2014-01-18");
        process.doWork(null);
    }

}
