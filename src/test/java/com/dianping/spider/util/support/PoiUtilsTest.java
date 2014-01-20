package com.dianping.spider.util.support;

import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class PoiUtilsTest {

    @Test
    public void getIdTest() {
        int id = PoiUtils.getDpShopId(1, "星美国际影城-浦东正大店", "陆家嘴西路168号正大广场8楼（近东方明珠）","电话：021-50472025");
        System.out.println(id);
    }
}
