package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaPlayItemListGewara;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.support.ApplicationContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-16
 * Time: PM2:42
 * To change this template use File | Settings | File Templates.
 */
public class PlayItemCaptureTask implements Task {

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

        List<MovieGewaraBasic> movieListAll = new LinkedList<MovieGewaraBasic>();
        List<CinemaPlayItemListGewara> cinemaPlayItemListGewaras = new LinkedList<CinemaPlayItemListGewara>();

        for(int pageNo=1; pageNo<=pageNum; pageNo++){
            Page<CinemaGewaraBasic> page = movieService.paginateCinemaGewaraBasics(pageNo, pageSize);

            if(page!=null && page.getRecords()!=null && page.getRecords().size()>0){
                List<CinemaGewaraBasic> cinemaGewaraBasics = page.getRecords();
                for(CinemaGewaraBasic cinemaGewaraBasic : cinemaGewaraBasics){

                    PlayItemCrawler crawler = new PlayItemCrawler(cinemaGewaraBasic);
                    Map<String, Object> result = crawler.parse();

                    List<MovieGewaraBasic> movieList = (List<MovieGewaraBasic>) result.get(PlayItemCrawler.MOVIE_GEWARA_BASIC_LIST);
                    if(CollectionUtils.isNotEmpty(movieList)){
                        movieListAll.addAll(movieList);
                    }

                    CinemaPlayItemListGewara cinemaPlayItemListGewara = (CinemaPlayItemListGewara) result.get(PlayItemCrawler.CINEMA_PLAY_ITEM_LIST_GEWARA);

                    if(cinemaPlayItemListGewara!=null){
                        cinemaPlayItemListGewara.setCaptureDate(new Date());
                        cinemaPlayItemListGewaras.add(cinemaPlayItemListGewara);
                    }
                }
            }
        }

        movieListAll = clearDuplication(movieListAll);
        if(CollectionUtils.isNotEmpty(movieListAll)){
            movieService.batchUpsertMovieGewaraBasics(movieListAll);
        }

        if(CollectionUtils.isNotEmpty(cinemaPlayItemListGewaras)){
            if( movieService.removeAllCinemaPlayItemListGewaras() ){
                movieService.batchUpsertCinemaPlayItemListGewaras(cinemaPlayItemListGewaras);
                movieService.addCinemaPlayItemListGewaraToRepo(cinemaPlayItemListGewaras);
            }
        }

        return true;
    }

    private List<MovieGewaraBasic> clearDuplication(List<MovieGewaraBasic> movieListAll){
        Map<Integer, MovieGewaraBasic> map = new HashMap<Integer, MovieGewaraBasic>();
        List<MovieGewaraBasic> result = new ArrayList<MovieGewaraBasic>();
        for(MovieGewaraBasic movie : movieListAll){
            map.put(movie.getId(), movie);
        }
        Set<Integer> keySet = map.keySet();
        for(Integer key : keySet){
            result.add(map.get(key));
        }
        return result;
    }


}
