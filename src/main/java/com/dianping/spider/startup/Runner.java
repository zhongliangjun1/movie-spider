package com.dianping.spider.startup;

import com.dianping.combiz.spring.util.LionConfigUtils;
import com.dianping.mailremote.remote.MailService;
import com.dianping.spider.business.cinema.BasicCinemaCaptureTask;
import com.dianping.spider.business.cinema.DPShopUpdateTask;
import com.dianping.spider.business.cinema.DetailCinemaCaptureTask;
import com.dianping.spider.business.district.DistrictCaptureTask;
import com.dianping.spider.business.movie.MoviePicUpdateTask;
import com.dianping.spider.business.movie.PlayItemCaptureTask;
import com.dianping.spider.util.support.ApplicationContextUtils;
import com.dianping.spider.util.support.MailUtils;

import java.util.HashMap;
import java.util.Map;

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

        if(process && isEnableProcess("dish-server.runDistrictCaptureTask.switch")){
            process = runDistrictCaptureTask();
            System.out.println("finish runDistrictCaptureTask with result: "+process);
        }


        if(process && isEnableProcess("dish-server.runBasicCinemaCaptureTask.switch")){
            process = runBasicCinemaCaptureTask();
            System.out.println("finish runBasicCinemaCaptureTask with result: "+process);
        }


        if(process && isEnableProcess("dish-server.runDetailCinemaCaptureTask.switch")){
            process = runDetailCinemaCaptureTask();
            System.out.println("finish runDetailCinemaCaptureTask with result: "+process);
        }

        if(process && isEnableProcess("dish-server.runDPShopUpdateTask.switch")){
            process = runDPShopUpdateTask();
            System.out.println("finish runDPShopUpdateTask with result: "+process);
        }


        if(process && isEnableProcess("dish-server.runPlayItemCaptureTask.switch")){
            process = runPlayItemCaptureTask();
            System.out.println("finish runPlayItemCaptureTask with result: "+process);
        }

        if(process){
            process = runMoviePicUpdateTask();
            System.out.println("finish runMoviePicUpdateTask with result: "+process);
        }


        if(!process){
            String content = "%>_<% 任务失败了，你家里人知道吗？";
            Map<String,String> subPair = new HashMap<String, String>();
            subPair.put("result", content);
            MailUtils.sendMail(subPair);
        }
        System.out.println("finish");

        System.exit(0);
    }

    private static boolean isEnableProcess(String lionKey){
        boolean result = false;
        String isEnable = LionConfigUtils.getProperty(lionKey, "n");
        if("y".equals(isEnable)){
            result = true;
        }
        return result;
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

    public static boolean runMoviePicUpdateTask(){
        MoviePicUpdateTask task = new MoviePicUpdateTask();
        return task.run();
    }

    public static boolean runDPShopUpdateTask(){
        DPShopUpdateTask task = new DPShopUpdateTask();
        return task.run();
    }



}
