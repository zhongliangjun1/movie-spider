package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.DistrictGewara;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-8
 * Time: PM2:46
 * To change this template use File | Settings | File Templates.
 */
public class GetPageNumProcessorTest {

    public static void main(String[] args) {
        DistrictGewara firstDistrict = new DistrictGewara();
        firstDistrict.setId(310115);
        firstDistrict.setCityIdOfDP(1);
        firstDistrict.setCitySpell("shanghai");
        GetPageNumProcessor processor = new GetPageNumProcessor(ProcessName.GET_PAGE_NUM_PROCESS, firstDistrict);
        processor.doWork(null);
    }

}
