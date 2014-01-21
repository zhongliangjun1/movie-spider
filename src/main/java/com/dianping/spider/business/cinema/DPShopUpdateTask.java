package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraDetail;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.support.ApplicationContextUtils;
import com.dianping.spider.util.support.PoiUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
public class DPShopUpdateTask implements Task {

    private final Logger logger = Logger.getLogger(this.getClass());

    private MovieService movieService = ApplicationContextUtils.getBean("movieService");

    @Override
    public boolean run() {
        int page = 1;
        while (true) {
            Page<CinemaGewaraDetail> result = movieService.paginateCinemaGewaraDetails(page, 5);
            if (result == null) {
                break;
            }
            List<CinemaGewaraDetail> cinemas = result.getRecords();
            if (CollectionUtils.isEmpty(cinemas)) {
                break;
            }
            updateCinemas(cinemas);
            movieService.batchUpsertCinemaGewaraDetails(cinemas);
            page++;
        }
        return true;
    }

    private void updateCinemas(List<CinemaGewaraDetail> cinemas) {
        for (int i=0; i<cinemas.size(); ++i) {
            CinemaGewaraDetail cinema = cinemas.get(i);
            int dpId = PoiUtils.getDpShopId(cinema.getDistrictGewara().getCityIdOfDP(),
                    cinema.getName(), cinema.getAddress(), cinema.getPhone());
            if (dpId == 0) {
                continue;
            }
            cinema.setShopIdOfDP(dpId);
        }
    }
}
