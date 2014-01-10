package com.dianping.spider.util.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-10
 * Time: PM8:34
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContextUtils {

    private final static String[] configLocations = {"classpath*:config/spring/local/appcontext-*.xml", "classpath*:config/spring/common/appcontext-*.xml"};

    private final static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocations);

    public static void init(){}

    public static <T> T getBean(String bean){
        return (T) applicationContext.getBean(bean);
    }

}
