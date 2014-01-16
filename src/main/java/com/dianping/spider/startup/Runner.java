package com.dianping.spider.startup;

import com.dianping.spider.business.cinema.BasicCinemaCaptureTask;
import com.dianping.spider.business.cinema.DetailCinemaCaptureTask;
import com.dianping.spider.business.district.DistrictCaptureTask;
import com.dianping.spider.business.movie.PlayItemCaptureTask;
import com.dianping.spider.util.support.ApplicationContextUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM6:10
 * To change this template use File | Settings | File Templates.
 */
public class Runner {

    static {
        ApplicationContextUtils.init();
    }

    public static void main(String[] args) {
        System.out.println("begin");

        boolean process = true;

//        if(process)
//            process = runDistrictCaptureTask();
//
//        if(process)
//            process = runBasicCinemaCaptureTask();
//
//        if(process)
//            process = runDetailCinemaCaptureTask();

        if(process)
            process = runPlayItemCaptureTask();

        System.out.println("finish");
    }


    public static boolean runDistrictCaptureTask(){
        DistrictCaptureTask task = new DistrictCaptureTask();
        return task.run();
    }

    public static boolean runBasicCinemaCaptureTask(){
        BasicCinemaCaptureTask task = new BasicCinemaCaptureTask();
        return task.run();
    }

    public static boolean runDetailCinemaCaptureTask(){
        DetailCinemaCaptureTask task = new DetailCinemaCaptureTask();
        return task.run();
    }

    public static boolean runPlayItemCaptureTask(){
        PlayItemCaptureTask task = new PlayItemCaptureTask();
        return task.run();
    }

}
