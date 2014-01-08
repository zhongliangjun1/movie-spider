package com.dianping.spider.business.cinema;

import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-7
 * Time: PM8:13
 * To change this template use File | Settings | File Templates.
 *
 * 页码数
 * <div id="page">
     <a href="/shanghai/movie/searchCinema.xhtml?countycode=310115" class="on"><span>1</span></a>
     <a href="/shanghai/movie/searchCinema.xhtml?pageNo=1&amp;countycode=310115"><span>2</span></a>
     <a href="/shanghai/movie/searchCinema.xhtml?pageNo=2&amp;countycode=310115"><span>3</span></a>
     <a href="/shanghai/movie/searchCinema.xhtml?pageNo=1&amp;countycode=310115" class="next"><span>下一页</span></a>
   </div>
 */
public class GetPageNumProcessor extends TemplateProcessor {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=%s";
    private String processName;
    private String url;
    private Crawler crawler;

    public GetPageNumProcessor(String processName, int firstDistrictId) {
        this.processName = processName;
        this.url = String.format(URL_TEMPLATE, firstDistrictId);
    }

    private void initCrawler() throws CrawlerInitFailureException {
        this.crawler = new AbstractCrawler(CrawlerInitType.URL, url){
            @Override
            public Integer parse() {
                try{
                    int pageNum = 1;
                    Element page_Element = doc.getElementById("page");
                    if(page_Element!=null){
                        Elements a_Elements = page_Element.getElementsByTag("a");
                        pageNum = a_Elements.size()-1;
                    }
                    return pageNum;
                }catch (NullPointerException e){
                    super.logger.error("dom changed : "+url, e);
                    return 0;
                }
            }
        };
    }



    @Override
    protected void nameConfig() {
        super.name = this.processName;
    }

    @Override
    protected Integer doWork(XDefaultContext context) {
        try {
            initCrawler();
        } catch (CrawlerInitFailureException e) {
            this.logger.error(e);
            return 0;
        }
        return (Integer) crawler.parse();
    }



}
