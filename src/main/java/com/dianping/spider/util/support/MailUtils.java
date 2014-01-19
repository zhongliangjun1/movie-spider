package com.dianping.spider.util.support;

import com.dianping.mailremote.remote.MailService;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-19
 * Time: PM5:34
 * To change this template use File | Settings | File Templates.
 */
public class MailUtils {

    public static void sendMail(String content){
        try{
            MailService mailService = ApplicationContextUtils.getBean("mailService");
            int typeCode = 880;
            String to = "mango@dianping.com";
            String subject = "电影排片表爬虫job";
            mailService.send(typeCode, to, subject, content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
