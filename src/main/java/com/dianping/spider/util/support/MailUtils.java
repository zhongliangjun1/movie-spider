package com.dianping.spider.util.support;

import com.dianping.mailremote.remote.MailService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-19
 * Time: PM5:34
 * To change this template use File | Settings | File Templates.
 */
public class MailUtils {

    public static void sendMail(Map<String,String> subPair){
        try{
            MailService mailService = ApplicationContextUtils.getBean("mailService");
            int typeCode = 1300;
            String to = "mango@dianping.com";
            mailService.send(typeCode, to, subPair);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
