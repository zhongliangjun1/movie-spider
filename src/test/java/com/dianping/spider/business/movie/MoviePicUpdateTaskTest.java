package com.dianping.spider.business.movie;

import com.dianping.spider.util.support.ApplicationContextUtils;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午8:44
 * To change this template use File | Settings | File Templates.
 */
public class MoviePicUpdateTaskTest {

    MoviePicUpdateTask moviePicUpdateTask = new MoviePicUpdateTask();

    @Test
    public void runTest() {
        ApplicationContextUtils.init();
        moviePicUpdateTask.run();
    }
}
