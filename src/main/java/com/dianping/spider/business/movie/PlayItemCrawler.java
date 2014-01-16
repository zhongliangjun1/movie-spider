package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaPlayItemListGewara;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.MoviePlayItemListGewara;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.support.DateUtils;
import com.dianping.swiftly.utils.concurrent.DefaultStep;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import com.dianping.swiftly.utils.concurrent.XFacade;
import com.dianping.swiftly.utils.concurrent.XStep;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: PM1:23
 * To change this template use File | Settings | File Templates.
 */
public class PlayItemCrawler implements Crawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private CinemaGewaraBasic cinemaGewaraBasic;

    public final static String MOVIE_GEWARA_BASIC_LIST = "MOVIE_GEWARA_BASIC_LIST";
    public final static String CINEMA_PLAY_ITEM_LIST_GEWARA = "CINEMA_PLAY_ITEM_LIST_GEWARA";

    public PlayItemCrawler(CinemaGewaraBasic cinemaGewaraBasic) {
        this.cinemaGewaraBasic = cinemaGewaraBasic;
    }

    private Map<String, Object> assign(int numOfDays){
        if(numOfDays<=0)
            return null;

        Map<String, String> timeToProcessMap = new HashMap<String, String>();
        List<String> processNameList = new LinkedList<String>();
        XDefaultContext context = new XDefaultContext();
        XStep first = new DefaultStep();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> times = DateUtils.getTimesFromNow(dateFormat, numOfDays);

        for(String time : times){
            String processName = String.format(ProcessName.GET_CINEMA_PLAY_ITEM_ACCORD_DATE_PROCESS, time);
            timeToProcessMap.put(time, processName);
            processNameList.add(processName);
            GetPlayItemAccordDateProcessor processor = new GetPlayItemAccordDateProcessor(processName, this.cinemaGewaraBasic, time);
            first.addProcessor(processor);
        }

        try {
            XFacade.getInstance().invoke(context, first);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }



        Map<String, Object> result = new HashMap<String, Object>();

        List<MovieGewaraBasic> movieListAll = new ArrayList<MovieGewaraBasic>();
        result.put(MOVIE_GEWARA_BASIC_LIST, movieListAll);

        CinemaPlayItemListGewara cinemaPlayItemListGewara = new CinemaPlayItemListGewara();
        Map<String, List<MoviePlayItemListGewara>> playItemMap = new HashMap<String, List<MoviePlayItemListGewara>>();
        cinemaPlayItemListGewara.setCinemaId(this.cinemaGewaraBasic.getId());
        cinemaPlayItemListGewara.setPlayItemMap(playItemMap);
        result.put(CINEMA_PLAY_ITEM_LIST_GEWARA, cinemaPlayItemListGewara);

        for(String time : times){
            String processName = timeToProcessMap.get(time);
            Map<String, Object> record = (Map<String, Object>) context.getReuslt(processName);
            if(record!=null){
                List<MovieGewaraBasic> movieList = (List<MovieGewaraBasic>) record.get(ProcessName.MOVIE_GEWARA_BASIC_LIST_RESULT_KEY);
                if(CollectionUtils.isNotEmpty(movieList)){
                    movieListAll.addAll(movieList);
                }

                List<MoviePlayItemListGewara> moviePlayItemListGewaraList = (List<MoviePlayItemListGewara>) record.get(ProcessName.MOVIE_PLAY_ITEM_LIST_GEWARA_LIST_RESULT_KEY);
                if(CollectionUtils.isNotEmpty(moviePlayItemListGewaraList)){
                    playItemMap.put(time, moviePlayItemListGewaraList);
                }
            }
        }
        return result;

    }

    @Override
    public Map<String, Object> parse() {
        int numOfDays = 7;
        return assign(numOfDays);
    }

}
