package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.MovieService;
import com.dianping.dishremote.remote.dto.Page;
import com.dianping.dishremote.remote.dto.movie.*;
import com.dianping.mailremote.remote.MailService;
import com.dianping.spider.startup.Task;
import com.dianping.spider.util.support.ApplicationContextUtils;
import com.dianping.spider.util.support.MailUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
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
        boolean isSendMail = false;

        movieService.removeAllCinemaPlayItemListGewaras();

        for(int pageNo=1; pageNo<=pageNum; pageNo++){
            Page<CinemaGewaraBasic> page = movieService.paginateCinemaGewaraBasics(pageNo, pageSize);

            if(page!=null && page.getRecords()!=null && page.getRecords().size()>0){
                List<CinemaGewaraBasic> cinemaGewaraBasics = page.getRecords();
                for(CinemaGewaraBasic cinemaGewaraBasic : cinemaGewaraBasics){

                    PlayItemCrawler crawler = new PlayItemCrawler(cinemaGewaraBasic);
                    Map<String, Object> result = crawler.parse();
                    if(result==null)
                        continue;

                    List<MovieGewaraBasic> movieList = (List<MovieGewaraBasic>) result.get(PlayItemCrawler.MOVIE_GEWARA_BASIC_LIST);
                    if(CollectionUtils.isNotEmpty(movieList)){
                        movieService.batchUpsertMovieGewaraBasics(movieList);
                    }

                    CinemaPlayItemListGewara cinemaPlayItemListGewara = (CinemaPlayItemListGewara) result.get(PlayItemCrawler.CINEMA_PLAY_ITEM_LIST_GEWARA);

                    if(cinemaPlayItemListGewara!=null){
                        cinemaPlayItemListGewara.setCaptureDate(new Date());
                        upsertCinemaPlayItemListGewaraWithRetry(cinemaPlayItemListGewara, 5);
                        addCinemaPlayItemListGewaraToRepoWithRetry(cinemaPlayItemListGewara, 5);
                        System.out.println("get CinemaPlayItemListGewara of : "+cinemaGewaraBasic.getName()+cinemaGewaraBasic.getId());
                        if(!isSendMail){
                            sendMail(cinemaPlayItemListGewara);
                            isSendMail = true;
                        }
                    }else {
                        System.out.println("fail to get CinemaPlayItemListGewara of : "+cinemaGewaraBasic.getName()+cinemaGewaraBasic.getId());
                    }

                }
            }
        }

        return true;
    }

    private boolean upsertCinemaPlayItemListGewaraWithRetry(CinemaPlayItemListGewara cinemaPlayItemListGewara, int retryCount){
        try{
            return movieService.upsertCinemaPlayItemListGewara(cinemaPlayItemListGewara);
        }catch (Exception e){
            System.out.println("upsertCinemaPlayItemListGewara failure and retry now");
            logger.error(e);
            if(retryCount>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                return upsertCinemaPlayItemListGewaraWithRetry(cinemaPlayItemListGewara, retryCount-1);
            }else {
                System.out.println("once upsertCinemaPlayItemListGewara failure");
                return false;
            }
        }
    }

    private boolean addCinemaPlayItemListGewaraToRepoWithRetry(CinemaPlayItemListGewara cinemaPlayItemListGewara, int retryCount){
        try{
            return movieService.addCinemaPlayItemListGewaraToRepo(cinemaPlayItemListGewara);
        }catch (Exception e){
            System.out.println("addCinemaPlayItemListGewaraToRepo failure and retry now");
            logger.error(e);
            if(retryCount>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                return addCinemaPlayItemListGewaraToRepoWithRetry(cinemaPlayItemListGewara, retryCount-1);
            }else {
                System.out.println("once addCinemaPlayItemListGewaraToRepo failure");
                return false;
            }
        }
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

    private void sendMail(CinemaPlayItemListGewara playItemList){
        try{

            Map<String,String> subPair = new HashMap<String, String>();
            subPair.put("result", "get it");
            subPair.put("cinemaId", ""+playItemList.getCinemaId());
            subPair.put("shopId", ""+playItemList.getShopIdOfDP());

            for(String time : playItemList.getPlayItemMap().keySet()){
                List<MoviePlayItemListGewara> list = playItemList.getPlayItemMap().get(time);
                for(MoviePlayItemListGewara moviePlayItemListGewara: list){
                    subPair.put("movieId", ""+moviePlayItemListGewara.getMovieId());
                    for(PlayItemGewara playItem:moviePlayItemListGewara.getPlayItemList()){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        subPair.put("beginTime", ""+sdf.format(playItem.getBeginTime()));
                        subPair.put("showType", ""+playItem.getShowType());
                        subPair.put("language", ""+playItem.getLanguage());
                        subPair.put("originalPrice", ""+playItem.getOriginalPrice());
                        subPair.put("screeningRoom", ""+playItem.getScreeningRoom());
                        break;
                    }
                    break;
                }
                break;
            }
            MailUtils.sendMail(subPair);
        }catch (Exception e){
            logger.error(e);
        }
    }





    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(1);list.add(2);list.add(3);list.add(4);
        int size = list.size();
        int begin = 0;
        int end = 0;
        int length = 3;
        while(begin==0 || end<size){
            if(end+length<size){
                end = end+length;
            }else{
                end = size;
            }
            List<CinemaPlayItemListGewara> subList = list.subList(begin, end);
            begin = begin + length;
            System.out.println(subList.size());
        }
    }


}
