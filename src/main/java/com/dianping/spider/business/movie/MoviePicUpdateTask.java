package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.support.ApplicationContextUtils;
import com.dianping.spider.util.support.PicUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午3:35
 * To change this template use File | Settings | File Templates.
 */
public class MoviePicUpdateTask implements Task {

    private final Logger logger = Logger.getLogger(this.getClass());

    private MovieService movieService = ApplicationContextUtils.getBean("movieService");

    @Override
    public boolean run() {
        int page = 1;
        while (true) {
            Page<MovieGewaraBasic> result = movieService.paginateMovieGewaraBasics(page, 10);
            if (result == null) {
                break;
            }
            List<MovieGewaraBasic> movies = result.getRecords();
            if (CollectionUtils.isEmpty(movies)) {
                break;
            }
            List<MovieGewaraBasic> moviesToUpdate = selectMoviesToUpdate(movies);
            movieService.batchUpsertMovieGewaraBasics(moviesToUpdate);
            page++;
        }
        return true;
    }

    private List<MovieGewaraBasic> selectMoviesToUpdate(List<MovieGewaraBasic> movies) {
        List<MovieGewaraBasic> moviesToUpdate = new ArrayList<MovieGewaraBasic>();
        for (MovieGewaraBasic movie : movies) {
            if (StringUtils.isEmpty(movie.getPosterImageUrlOfDP()) &&
                    !StringUtils.isEmpty(movie.getPosterImageUrl())) {

                updatePicUrl(movie);
                moviesToUpdate.add(movie);
            }
        }
        return moviesToUpdate;
    }

    private void updatePicUrl(MovieGewaraBasic movie) {
        String picUrl = movie.getPosterImageUrl();
        String tempFilePath = PicUtils.getTempFilePath(picUrl);
        PicUtils.download(picUrl, tempFilePath);
        String dpUrl = PicUtils.uploadPic(picUrl);
        if (StringUtils.isEmpty(dpUrl)) {
            logger.error("pic upload failed! pic: " + picUrl);
        }
        PicUtils.delete(tempFilePath);
        movie.setPosterImageUrlOfDP(dpUrl);
    }



}
