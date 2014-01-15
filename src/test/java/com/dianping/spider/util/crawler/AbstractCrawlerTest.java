package com.dianping.spider.util.crawler;

import com.dianping.spider.util.exception.CrawlerInitFailureException;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: AM11:59
 * To change this template use File | Settings | File Templates.
 */
public class AbstractCrawlerTest {

    public static void main(String[] args) throws CrawlerInitFailureException {
        AbstractCrawler crawler = new AbstractCrawler(CrawlerInitType.URL, "http://www.gewara.com/movie/searchCinema.xhtml?cinemaIdList=&cinemaids=&characteristic=&ctype=&countycode=310115&lineall=&lineId=&hotcinema=&pairseat=&popcorn=&park=&refund=&acthas=&cinemaname=&order=") {
            @Override
            public Object parse() {
                return null;
            }
        };
        Document document = crawler.getDoc();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                String url1 = "http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=1&mid=&fyrq=2014-01-14";
                String url2 = "http://www.dianping.com/shop/4125777";
                try {
                    AbstractCrawler ajaxCrawler = new AbstractCrawler(CrawlerInitType.AJAX_HTML, url1){
                        @Override
                        public Object parse() {
                            return null;
                        }
                    };
                } catch (CrawlerInitFailureException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        System.out.println("begin submit");
        for(int i=0; i<30; i++){
            executorService.submit(task);
        }

        System.out.println("end");
    }

}
