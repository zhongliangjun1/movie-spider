package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraDetail;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.support.ApplicationContextUtils;
import org.apache.log4j.Logger;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-14
 * Time: PM4:52
 * To change this template use File | Settings | File Templates.
 */
public class DetailCinemaCaptureTask implements Task {

    private final Logger logger = Logger.getLogger(this.getClass());

    private MovieService movieService = ApplicationContextUtils.getBean("movieService");


    @Override
    public boolean run() {
        long num = movieService.getCinemaGewaraBasicsCount();

        if(num<=0)
            return false;

        double countAll = num;
        int pageSize = 10;
        int pageNum = (int) Math.ceil(countAll/pageSize);
        List<CinemaGewaraDetail> result = new LinkedList<CinemaGewaraDetail>();

        for(int pageNo=1; pageNo<=pageNum; pageNo++){
            Page<CinemaGewaraBasic> page = movieService.paginateCinemaGewaraBasics(pageNo, pageSize);

            if(page!=null && page.getRecords()!=null && page.getRecords().size()>0){
                List<CinemaGewaraBasic> cinemaGewaraBasics = page.getRecords();
                for(CinemaGewaraBasic cinemaGewaraBasic : cinemaGewaraBasics){
                    try {
                        DetailCinemaCrawler detailCinemaCrawler = new DetailCinemaCrawler(cinemaGewaraBasic);
                        result.add(detailCinemaCrawler.parse());
                    } catch (CrawlerInitFailureException e) {
                        logger.error(e);
                        return false;
                    }
                }
            }
        }
        movieService.batchUpsertCinemaGewaraDetails(result);
        return true;
    }


}
