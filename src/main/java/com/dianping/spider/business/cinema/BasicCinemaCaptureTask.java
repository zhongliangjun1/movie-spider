package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.DistrictGewara;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.support.ApplicationContextUtils;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-10
 * Time: PM9:35
 * To change this template use File | Settings | File Templates.
 */
public class BasicCinemaCaptureTask implements Task {

    private final Logger logger = Logger.getLogger(this.getClass());

    private MovieService movieService = ApplicationContextUtils.getBean("movieService");


    @Override
    public boolean run() {
        long num = movieService.getDistrictGewarasCount();
        if(num<=0)
            return false;

        double countAll = num;
        int pageSize = 10;
        int pageNum = (int) Math.ceil(countAll/pageSize);
        List<CinemaGewaraBasic> result = new LinkedList<CinemaGewaraBasic>();
        for(int pageNo=1; pageNo<=pageNum; pageNo++){
            Page<DistrictGewara> page = movieService.paginateDistrictGewaras(pageNo, pageSize);

            if(page!=null && page.getRecords()!=null && page.getRecords().size()>0){
                List<DistrictGewara> districtGewaras = page.getRecords();
                for(DistrictGewara districtGewara : districtGewaras){
                    BasicCinemaCrawler basicCinemaCrawler = new BasicCinemaCrawler(districtGewara);
                    //result.addAll(basicCinemaCrawler.parse());
                    movieService.batchUpsertCinemaGewaraBasics(basicCinemaCrawler.parse());
                }
            }
            
        }
        //movieService.batchUpsertCinemaGewaraBasics(result);
        return true;
    }





}
