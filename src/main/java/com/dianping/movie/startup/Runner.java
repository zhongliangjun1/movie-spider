package com.dianping.movie.startup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM6:10
 * To change this template use File | Settings | File Templates.
 */
public class Runner {

    private static String[] configLocations = {"classpath*:config/spring/local/appcontext-*.xml", "classpath*:config/spring/common/appcontext-*.xml"};

    private volatile static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocations);

    public static void main(String[] args) {
        System.out.println("begin");
    }

}
