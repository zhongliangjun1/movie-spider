package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.MoviePlayItemListGewara;
import com.dianping.dishremote.remote.dto.movie.PlayItemGewara;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.support.DateUtils;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: PM2:12
 * To change this template use File | Settings | File Templates.
 *
 * http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=1&mid=&fyrq=2014-01-16
 */
public class GetPlayItemAccordDateProcess extends TemplateProcessor {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/cinema/ajax/getCinemaPlayItem.xhtml?cid=%s&mid=&fyrq=%s";
    private String processName;
    private String url;
    private Crawler crawler;
    private CinemaGewaraBasic cinemaGewaraBasic;

    public GetPlayItemAccordDateProcess(String processName, CinemaGewaraBasic cinemaGewaraBasic, String time) {
        this.processName = processName;
        this.cinemaGewaraBasic = cinemaGewaraBasic;
        this.url = String.format(URL_TEMPLATE, cinemaGewaraBasic.getId(), time);
    }

    private void initCrawler() throws CrawlerInitFailureException {
        this.crawler = new AbstractCrawler(CrawlerInitType.AJAX_HTML, url){
            @Override
            public Map<String, Object> parse() {
                try{
                    Elements choseMovieInfo_Elements = doc.getElementsByClass("choseMovieInfo");
                    if(choseMovieInfo_Elements==null)
                        return null;

                    for(Element choseMovieInfo : choseMovieInfo_Elements){
                        MovieGewaraBasic movie = new MovieGewaraBasic();
                        List<PlayItemGewara> playItemList = new ArrayList<PlayItemGewara>();
                        MoviePlayItemListGewara moviePlayItemList = new MoviePlayItemListGewara();
                        moviePlayItemList.setPlayItemList(playItemList);

                        movie.setPosterImageUrl(choseMovieInfo.getElementsByTag("img").first().attr("src"));

                        String movieHref = choseMovieInfo.select("div.ui_text a").first().attr("href");
                        Integer movieId = getMovieIdFromString(movieHref);
                        if(movieId==null)
                            continue;
                        movie.setId(movieId);

                        movie.setName(choseMovieInfo.select("div.ui_text b").first().text());

                        BigDecimal grade = new BigDecimal(choseMovieInfo.select("span.grade sub").first().text()+choseMovieInfo.select("span.grade sup").first().text());
                        movie.setGrade(grade);

                        Elements p_Elements = choseMovieInfo.select("div.ui_text p");
                        if(p_Elements!=null){
                            for(Element p : p_Elements){
                                String title = p.getElementsByTag("em").first().text();
                                String body = p.text();
                                if("看点：".equals(title)){
                                    movie.setLightSpot(body.replaceAll(title, ""));
                                }else if("首映：".equals(title)){
                                    String firstShowDateStr = body.replaceAll(title, "");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    movie.setFirstShowDate(DateUtils.strToDate(dateFormat, firstShowDateStr));
                                }else if("语言：".equals(title)){
                                    String anotherTitle = p.getElementsByTag("em").last().text();
                                    body = body.replaceAll(title, "");
                                    movie.setLanguage(body.substring(0, body.indexOf("片长：")));
                                    movie.setDuration(body.substring(body.indexOf("片长：")+3));
                                }else if("导演/主演：".equals(title)){
                                    body = body.replaceAll(title, "");
                                    movie.setDirector(body.substring(0, body.indexOf("/")));
                                    movie.setMainPerformer(body.substring(body.indexOf("/")+1));
                                }
                            }
                        }




                        System.out.println();
                    }

                    return null;
                }catch (NullPointerException e){
                    super.logger.error("dom changed : "+url, e);
                    return null;
                }
            }
        };
    }

    private Integer getMovieIdFromString(String str){
        Integer id = null;
        int begin = str.lastIndexOf("/")+1;
        try{
            id = Integer.parseInt(str.substring(begin));
        }catch (Exception e){
            logger.error(e);
        }
        return id;
    }

    @Override
    protected void nameConfig() {
        super.name = this.processName;
    }

    @Override
    protected Map<String, Object> doWork(XDefaultContext context) {
        try {
            initCrawler();
        } catch (CrawlerInitFailureException e) {
            this.logger.error(e);
            return null;
        }
        return (Map<String, Object>) this.crawler.parse();
    }

    public static void main(String[] args) {
        CinemaGewaraBasic cinemaGewaraBasic = new CinemaGewaraBasic();
        cinemaGewaraBasic.setId(1);
        GetPlayItemAccordDateProcess process = new GetPlayItemAccordDateProcess(
                ProcessName.GET_CINEMA_PLAY_ITEM_ACCORD_DATE_PROCESS, cinemaGewaraBasic, "2014-01-18");
        process.doWork(null);
    }


}
