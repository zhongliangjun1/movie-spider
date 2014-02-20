package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.MovieDouban;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.MovieStagePhotoDouban;
import com.dianping.spider.business.movie.doubansdk.MovieSearchResultEntity;
import com.dianping.spider.business.movie.doubansdk.MovieSubjectResultEntity;
import com.dianping.spider.business.movie.doubansdk.Subject;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.spider.util.support.DateUtils;
import com.dianping.spider.util.support.HttpClientUtils;
import com.dianping.spider.util.support.StringUtils;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import com.google.gson.Gson;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-2-15
 * Time: PM9:37
 * To change this template use File | Settings | File Templates.
 */
public class GetMovieInfoFromDouBanProcessor extends TemplateProcessor {

    private String processName;
    private MovieGewaraBasic movieGewaraBasic;

    private MovieDouban movieDouban;

    public GetMovieInfoFromDouBanProcessor(String processName, MovieGewaraBasic movieGewaraBasic) {
        this.processName = processName;
        this.movieGewaraBasic = movieGewaraBasic;
        this.movieDouban = new MovieDouban();
        this.movieDouban.setIdOfGewara(movieGewaraBasic.getId());
    }



    // search result : /v2/movie/search?q={text}
    private boolean searchMovie(){
        boolean result = false;
        String movieName = movieGewaraBasic.getName();
        String url = "http://api.douban.com/v2/movie/search?q="+movieName;
        String resultOfJSONString = HttpClientUtils.sendGetWithResultOfJSONString(url);

        if(StringUtils.isEmpty(resultOfJSONString))
            return result;

        try {
            Gson gson = new Gson();
            MovieSearchResultEntity movieSearchResult = gson.fromJson(resultOfJSONString, MovieSearchResultEntity.class);
            int total = movieSearchResult.getTotal();
            if(total<=0){
                return result;
            } else {
                for(int i=0; i<total; i++){
                    Subject subject = movieSearchResult.getSubjects().get(i);
                    if(movieName.equals(subject.getTitle()) && "movie".equals(subject.getSubtype())){
                        movieDouban.setId(subject.getId());
                        movieDouban.setTitle(subject.getTitle());
                        movieDouban.setOriginal_title(subject.getOriginal_title());
                        movieDouban.setAverage(subject.getRating().getAverage());
                        movieDouban.setStars(subject.getRating().getStars());
                        movieDouban.setPosterImageLargeUrl(subject.getImages().getLarge());
                        if (StringUtils.isNotEmpty(subject.getYear())){
                            movieDouban.setYear(Integer.parseInt(subject.getYear()));
                        }
                        movieDouban.setAlt(subject.getAlt());
                        result = true;
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    // get info : /v2/movie/subject/:id
    private boolean getInfoByAPI(){
        boolean result = false;
        int subjectId = movieDouban.getId();
        String url = "http://api.douban.com/v2/movie/subject/"+subjectId;
        String resultOfJSONString = HttpClientUtils.sendGetWithResultOfJSONString(url);

        if(StringUtils.isEmpty(resultOfJSONString))
            return result;

        try{
            Gson gson = new Gson();
            MovieSubjectResultEntity movieSubjectResult = gson.fromJson(resultOfJSONString, MovieSubjectResultEntity.class);
            movieDouban.setSummary(movieSubjectResult.getSummary());
            movieDouban.setAka(movieSubjectResult.getAka());
            movieDouban.setMobile_url(movieSubjectResult.getMobile_url());
            movieDouban.setWish_count(movieSubjectResult.getWish_count());
            movieDouban.setCollect_count(movieSubjectResult.getCollect_count());
            movieDouban.setComments_count(movieSubjectResult.getComments_count());
            movieDouban.setRatings_count(movieSubjectResult.getRatings_count());
            movieDouban.setReviews_count(movieSubjectResult.getReviews_count());
            movieDouban.setGenres(movieSubjectResult.getGenres());
            movieDouban.setCountries(movieSubjectResult.getCountries());
            movieDouban.setDouban_site(movieSubjectResult.getDouban_site());
            movieDouban.setSchedule_url(movieSubjectResult.getSchedule_url());
            result = true;
        } catch (Exception e){
            e.printStackTrace();
            return result;
        }
        return result;
    }

    // http://movie.douban.com/subject/24736566/
    private boolean getInfoByCrawlerFromMoviePage(){
        boolean result = false;
        int subjectId = movieDouban.getId();
        String url = "http://movie.douban.com/subject/"+subjectId;
        try {
            Crawler crawler = new AbstractCrawler(CrawlerInitType.URL, url) {
                @Override
                public MovieDouban parse() {
                    try{
                        Elements plNodes = doc.select("div#info span.pl");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for(Element e : plNodes){
                            String title = e.text();
                            if("上映日期:".equals(title)){
                                Element nextSibling = e.nextElementSibling();
                                String content = nextSibling.text();
                                if(content.indexOf("(中国大陆)")>0){
                                    content = content.replaceAll("(中国大陆)", "");
                                    Date date = DateUtils.strToDate(dateFormat, content);
                                    movieDouban.setMainland_pubdate(date);
                                    movieDouban.setPubdates(date);
                                    nextSibling = nextSibling.nextElementSibling();
                                    if("v:initialReleaseDate".equals(nextSibling.attr("property"))){
                                        content = nextSibling.text();
                                        content = content.substring(0, content.indexOf("("));
                                        date = DateUtils.strToDate(dateFormat, content);
                                        movieDouban.setForeign_pubdate(date);
                                    }
                                } else {
                                    content = content.substring(0, content.indexOf("("));
                                    Date date = DateUtils.strToDate(dateFormat, content);
                                    movieDouban.setForeign_pubdate(date);
                                    movieDouban.setPubdates(date);
                                }
                            } else if("IMDb链接:".equals(title)){
                                Element nextSibling = e.nextElementSibling();
                                movieDouban.setImdb(nextSibling.text());
                                movieDouban.setUrlOfIMDb(nextSibling.attr("href"));
                            } else if("官方网站:".equals(title)){
                                Element nextSibling = e.nextElementSibling();
                                movieDouban.setWebsite(nextSibling.attr("href"));
                            }
                        }
                    } catch (NullPointerException e){
                        System.out.println("dom changed");
                        e.printStackTrace();
                    }
                    return movieDouban;
                }
            };
            crawler.parse();
            result = true;
        } catch (CrawlerInitFailureException e) {
            e.printStackTrace();
        }

        return result;
    }

    // step1: http://movie.douban.com/subject/4202982/photos
    // step2: http://movie.douban.com/photos/photo/2157920052
    private boolean getStagePhotosByCrawler(){
        boolean result = false;
        try {

            // step1
            String urlOfStep1 = "http://movie.douban.com/subject/"+movieDouban.getId()+"/photos";
            Crawler crawlerStep1 = new AbstractCrawler(CrawlerInitType.URL, urlOfStep1) {
                @Override
                public List<Long> parse() {
                    List<Long> photoIds = new ArrayList<Long>();
                    try {
                        Elements coverNodes = doc.select("div.cover");
                        for(Element element : coverNodes){
                            Element liNode = element.parent();
                            long photoId = Long.parseLong(liNode.attr("data-id"));
                            photoIds.add(photoId);
                        }
                    } catch (NullPointerException e){
                        System.out.println("dom changed");
                        e.printStackTrace();
                    }
                    return photoIds;
                }
            };
            List<Long> photoIds = (List<Long>) crawlerStep1.parse();

            // step2
            List<MovieStagePhotoDouban> stagePhotos = new ArrayList<MovieStagePhotoDouban>();
            for(long photoId : photoIds){
                String url = "http://movie.douban.com/photos/photo/"+photoId;
                Crawler crawler = new AbstractCrawler(CrawlerInitType.URL, url) {
                    @Override
                    public MovieStagePhotoDouban parse() {
                        Elements mainphotoNodes = doc.select("a.mainphoto img");
                        if(mainphotoNodes.size()>0){
                            MovieStagePhotoDouban movieStagePhotoDouban = new MovieStagePhotoDouban();
                            movieStagePhotoDouban.setPhotoUrl(mainphotoNodes.get(0).attr("src"));

                            Elements descriNodes = doc.select("div.photo_descri div");
                            if(descriNodes.size()>0){
                                movieStagePhotoDouban.setDescription(descriNodes.get(0).text());
                            }

                            return movieStagePhotoDouban;
                        } else {
                            return null;
                        }
                    }
                };
                MovieStagePhotoDouban movieStagePhotoDouban = (MovieStagePhotoDouban) crawler.parse();
                if(movieStagePhotoDouban!=null){
                    movieStagePhotoDouban.setId(photoId);
                    stagePhotos.add(movieStagePhotoDouban);
                }
            }

            movieDouban.setStagePhotos(stagePhotos);

        } catch (CrawlerInitFailureException e) {
            e.printStackTrace();
        }

        return result;
    }



    @Override
    protected MovieDouban doWork(XDefaultContext context) {

        if( searchMovie() ){
            if( getInfoByAPI() ){
                if( getInfoByCrawlerFromMoviePage() ){
                    getStagePhotosByCrawler();
                    return this.movieDouban;
                }
            }
        }
        return null;
    }

    @Override
    protected void nameConfig() {
        super.name = this.processName;
    }


}
