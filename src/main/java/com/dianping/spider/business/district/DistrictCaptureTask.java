package com.dianping.spider.business.district;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.movie.DistrictGewara;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.support.ApplicationContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-10
 * Time: PM8:55
 * To change this template use File | Settings | File Templates.
 */
public class DistrictCaptureTask implements Task {

    private final Logger logger = Logger.getLogger(this.getClass());

    private MovieService movieService = ApplicationContextUtils.getBean("movieService");

    @Override
    public boolean run() {
        List<DistrictGewara> list = FirstDistrictsRepo.getFirstDistricts();
        if(CollectionUtils.isEmpty(list))
            return false;
        List<DistrictGewara> result = new LinkedList<DistrictGewara>();
        for(DistrictGewara firstDistrict : list){
            try {
                DistrictCrawler districtCrawler = new DistrictCrawler(firstDistrict);
                result.addAll(districtCrawler.parse());
            } catch (CrawlerInitFailureException e) {
                logger.error(e);
                return false;
            }
        }
        movieService.batchUpsertDistrictGewaras(result);
        return true;
    }


}
