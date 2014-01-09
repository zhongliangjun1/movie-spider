package com.dianping.spider.business.district;

import com.dianping.dishremote.remote.dto.movie.DistrictGewara;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-9
 * Time: PM2:46
 * To change this template use File | Settings | File Templates.
 */
public class FirstDistrictsRepo {

    public static List<DistrictGewara> getFirstDistricts(){

        List<DistrictGewara> list = new LinkedList<DistrictGewara>();

        DistrictGewara firstDistrict = new DistrictGewara();
        firstDistrict.setId(310115);
        firstDistrict.setName("浦东新区");
        firstDistrict.setCityIdOfDP(1);
        firstDistrict.setCitySpell("shanghai");

        list.add(firstDistrict);

        return list;
    }


}
