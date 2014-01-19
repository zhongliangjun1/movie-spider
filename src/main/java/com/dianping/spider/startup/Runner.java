package com.dianping.spider.startup;

import com.dianping.combiz.spring.util.LionConfigUtils;
import com.dianping.mailremote.remote.MailService;
import com.dianping.spider.business.cinema.BasicCinemaCaptureTask;
import com.dianping.spider.business.cinema.DetailCinemaCaptureTask;
import com.dianping.spider.business.district.DistrictCaptureTask;
import com.dianping.spider.business.movie.PlayItemCaptureTask;
import com.dianping.spider.util.support.ApplicationContextUtils;
import com.dianping.spider.util.support.MailUtils;

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

        if(process && isEnableProcess("dish-server.runDistrictCaptureTask.switch"))
            process = runDistrictCaptureTask();

        if(process && isEnableProcess("dish-server.runBasicCinemaCaptureTask.switch"))
            process = runBasicCinemaCaptureTask();

        if(process && isEnableProcess("dish-server.runDetailCinemaCaptureTask.switch"))
            process = runDetailCinemaCaptureTask();

        if(process && isEnableProcess("dish-server.runPlayItemCaptureTask.switch"))
            process = runPlayItemCaptureTask();

        if(!process){
            String content = "%>_<% 任务失败了，你家里人知道吗？";
            MailUtils.sendMail(content);
        }
        System.out.println("finish");
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



}
