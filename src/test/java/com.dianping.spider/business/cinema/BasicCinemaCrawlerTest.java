package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.DistrictGewara;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-8
 * Time: PM11:07
 * To change this template use File | Settings | File Templates.
 */
public class BasicCinemaCrawlerTest {

    private static String[] configLocations = {"classpath*:config/spring/local/appcontext-*.xml", "classpath*:config/spring/common/appcontext-*.xml"};

    private volatile static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocations);


    public static void main(String[] args) {
        DistrictGewara firstDistrict = new DistrictGewara();
        firstDistrict.setId(310115);
        firstDistrict.setName("浦东新区");
        firstDistrict.setCityIdOfDP(1);
        firstDistrict.setCitySpell("shanghai");
        BasicCinemaCrawler crawler = new BasicCinemaCrawler(firstDistrict);
        List<CinemaGewaraBasic> result = crawler.parse();
        System.out.println(result.size());
    }

}
