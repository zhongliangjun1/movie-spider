package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.swiftly.utils.concurrent.DefaultStep;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import com.dianping.swiftly.utils.concurrent.XFacade;
import com.dianping.swiftly.utils.concurrent.XStep;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-7
 * Time: PM3:34
 * To change this template use File | Settings | File Templates.
 *
 * http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=310115 获取页码数
 * http://www.gewara.com/shanghai/movie/searchCinema.xhtml?pageNo=0&countycode=310115 第一页
 *
 *
 */
public class BasicCinemaCrawler implements Crawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private int firstDistrictId;


    public BasicCinemaCrawler(int firstDistrictId) {
        this.firstDistrictId = firstDistrictId;
    }

    private int getPageNum(){
        GetPageNumProcessor getPageNumProcessor = new GetPageNumProcessor(ProcessName.GET_PAGE_NUM_PROCESS, this.firstDistrictId);
        return getPageNumProcessor.doWork(null);
    }

    private List<CinemaGewaraBasic> assign(int pageNum){
        List<String> processNameList = new LinkedList<String>();
        XDefaultContext context = new XDefaultContext();
        XStep first = new DefaultStep();

        for(int pageNo=0; pageNo<pageNum; pageNo++){
            String processName = String.format(ProcessName.GET_BASIC_CINEMA_ACCORD_PAGENO_PROCESS, pageNo);
            processNameList.add(processName);
            GetBasicCinemaAccordPageNoProcessor processor = new GetBasicCinemaAccordPageNoProcessor(processName, pageNo, this.firstDistrictId);
            first.addProcessor(processor);
        }

        try {
            XFacade.getInstance().invoke(context, first);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }

        List<CinemaGewaraBasic> result = new LinkedList<CinemaGewaraBasic>();
        for(String processName : processNameList){
            result.addAll((List<CinemaGewaraBasic>) context.getReuslt(processName));
        }
        return result;
    }

    @Override
    public List<CinemaGewaraBasic> parse() {

        int pageNum = getPageNum();
        List<CinemaGewaraBasic> result = assign(pageNum);

        return result;
    }
}
