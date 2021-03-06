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
import com.dianping.spider.util.support.StringUtils;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
public class GetPlayItemAccordDateProcessor extends TemplateProcessor {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/cinema/ajax/getOpiItemPage.xhtml?cid=%s&mid=&fyrq=%s";
    private String processName;
    private String url;
    private Crawler crawler;
    private String time;

    public GetPlayItemAccordDateProcessor(String processName, CinemaGewaraBasic cinemaGewaraBasic, String time) {
        this.processName = processName;
        this.url = String.format(URL_TEMPLATE, cinemaGewaraBasic.getId(), time);
        this.time = time;
    }

    private void initCrawler() throws CrawlerInitFailureException {
        this.crawler = new AbstractCrawler(CrawlerInitType.AJAX_HTML, url){
            @Override
            public Map<String, Object> parse() {
                try{

                    Elements playItemContent_Elements = doc.getElementsByClass("playItemContent");
                    if(playItemContent_Elements==null)
                        return null;

                    List<MovieGewaraBasic> movieList = new ArrayList<MovieGewaraBasic>();
                    List<MoviePlayItemListGewara> moviePlayItemListGewaraList = new ArrayList<MoviePlayItemListGewara>();

                    for(Element playItemContent : playItemContent_Elements ){

                        // movie
                        MovieGewaraBasic movie = new MovieGewaraBasic();

                        Element choseMovieInfo = playItemContent.getElementsByClass("choseMovieInfo").first();

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
                                    movie.setLanguage(body.substring(0, body.indexOf(anotherTitle)));
                                    movie.setDuration(body.substring(body.indexOf(anotherTitle) + 3));
                                }else if("导演/主演：".equals(title)){
                                    body = body.replaceAll(title, "");
                                    movie.setDirector(body.substring(0, body.indexOf("/")));
                                    movie.setMainPerformer(body.substring(body.indexOf("/") + 1));
                                }
                            }
                        }
                        movieList.add(movie);

                        // playItemList
                        Elements playItemList_Elements = playItemContent.getElementsByClass("chooseOpi_body").first().select("li");
                        if(playItemList_Elements==null)
                            continue;

                        MoviePlayItemListGewara moviePlayItemList = new MoviePlayItemListGewara();
                        moviePlayItemList.setMovieId(movie.getId());
                        List<PlayItemGewara> playItemList = new ArrayList<PlayItemGewara>();
                        moviePlayItemList.setPlayItemList(playItemList);

                        for(Element playItem_Element : playItemList_Elements){
                            PlayItemGewara playItemGewara = new PlayItemGewara();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");

                            String beginTime = playItem_Element.select("span.opitime b").first().text();
                            playItemGewara.setBeginTime(DateUtils.strToDate(dateFormat, time+"-"+beginTime));

                            String endTime = playItem_Element.select("span.opitime em").first().text().replaceAll("预计", "").replaceAll("散场", "");
                            playItemGewara.setEndTime(DateUtils.strToDate(dateFormat, time+"-"+endTime));

                            playItemGewara.setLanguage(playItem_Element.select("span.opiEdition em").first().text());

                            String showType = playItem_Element.select("span.opiEdition em").last().text().replaceAll("\\u00a0", "");
                            if(StringUtils.isEmpty(showType)){
                                showType = playItem_Element.select("span.opiEdition em").last().select("span").first().attr("class").replaceAll("ui_type", "").toUpperCase();
                            }
                            playItemGewara.setShowType(showType);

                            playItemGewara.setScreeningRoom(playItem_Element.select("span.opiRoom").first().text());

                            Element opiPrice = playItem_Element.select("span.opiPrice em").first();
                            if(opiPrice!=null){
                                playItemGewara.setOriginalPrice(new BigDecimal(opiPrice.text()));
                            }

                            playItemList.add(playItemGewara);
                        }
                        moviePlayItemListGewaraList.add(moviePlayItemList);

                    }

                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put(ProcessName.MOVIE_GEWARA_BASIC_LIST_RESULT_KEY, movieList);
                    result.put(ProcessName.MOVIE_PLAY_ITEM_LIST_GEWARA_LIST_RESULT_KEY, moviePlayItemListGewaraList);
                    return result;

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





}
