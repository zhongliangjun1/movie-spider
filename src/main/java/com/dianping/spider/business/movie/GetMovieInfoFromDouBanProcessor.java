package com.dianping.spider.business.movie;

import com.dianping.dishremote.remote.dto.movie.MovieDouban;
import com.dianping.dishremote.remote.dto.movie.MovieGewaraBasic;
import com.dianping.spider.business.movie.doubansdk.MovieSearchResultEntity;
import com.dianping.spider.business.movie.doubansdk.Subject;
import com.dianping.spider.util.support.HttpClientUtils;
import com.dianping.spider.util.support.StringUtils;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import com.google.gson.Gson;

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
                    if(movieName.equals(subject.getTitle())){
                        movieDouban.setId(subject.getId());
                        movieDouban.setTitle(subject.getTitle());
                        movieDouban.setOriginal_title(subject.getOriginal_title());
                        movieDouban.setAverage(subject.getRating().getAverage());
                        movieDouban.setStars(subject.getRating().getStars());
                        movieDouban.setPosterImageLargeUrl(subject.getImages().getLarge());
                        movieDouban.setYear(subject.getYear());
                        movieDouban.setAlt(subject.getAlt());
                        result = true;
                        return result;
                    }
                }
            }
            System.out.print(resultOfJSONString);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        return result;
    }



    @Override
    protected Object doWork(XDefaultContext context) {
        searchMovie();
        return null;
    }

    @Override
    protected void nameConfig() {
        super.name = this.processName;
    }


}
