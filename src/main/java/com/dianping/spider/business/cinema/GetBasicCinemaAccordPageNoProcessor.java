package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.DistrictGewara;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.Crawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import com.dianping.swiftly.utils.concurrent.TemplateProcessor;
import com.dianping.swiftly.utils.concurrent.XDefaultContext;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-8
 * Time: PM10:05
 * To change this template use File | Settings | File Templates.
 */
public class GetBasicCinemaAccordPageNoProcessor extends TemplateProcessor {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/%s/movie/searchCinema.xhtml?pageNo=%s&countycode=%s";
    private String processName;
    private DistrictGewara firstDistrict;
    private String url;
    private Crawler crawler;

    public GetBasicCinemaAccordPageNoProcessor(String processName,int pageNo, DistrictGewara firstDistrict) {
        this.processName = processName;
        this.firstDistrict = firstDistrict;
        this.url = String.format(URL_TEMPLATE, firstDistrict.getCitySpell(), pageNo, firstDistrict.getId());
    }

    private void initCrawler() throws CrawlerInitFailureException {
        this.crawler = new AbstractCrawler(CrawlerInitType.URL, url){
            @Override
            public List<CinemaGewaraBasic> parse() {
                try{

                    List<CinemaGewaraBasic> cinemaList = new LinkedList<CinemaGewaraBasic>();

                    Element cinemaListArea = doc.getElementById("cinemaListArea");
                    Elements effectLis = cinemaListArea.getElementsByClass("effectLi");
                    for(Element effectLi : effectLis){
                        CinemaGewaraBasic cinema = new CinemaGewaraBasic();
                        cinema.setDistrictGewara(firstDistrict);

                        Element ui_pic = effectLi.getElementsByClass("ui_pic").first();
                        Element ui_pic_img = ui_pic.getElementsByTag("img").first();
                        String ui_pic_img_style = ui_pic_img.attr("style");
                        cinema.setLogoUrl(getPicUrlFromString(ui_pic_img_style));

                        Element ui_text = effectLi.getElementsByClass("ui_text").first();
                        Element cinemaName = ui_text.getElementsByTag("a").first();
                        Integer cinemaId = getCinemaIdFromString(cinemaName.attr("href"));
                        if(cinemaId==null)
                            continue;
                        cinema.setId(cinemaId);
                        cinema.setName(cinemaName.text());

                        Element mt10 = ui_text.getElementsByClass("mt10").first();
                        String text = mt10.text().replaceAll("\\s*", "");
                        cinema.setAddress(getAddressFromString(text));

                        cinemaList.add(cinema);
                    }

                    return cinemaList;
                }catch (NullPointerException e){
                    super.logger.error("dom changed : "+url, e);
                    return null;
                }
            }
        };
    }

    /**
     * style="border:1px solid #f4f4f4;background:url(http://img5.gewara.com/sw120h60/images/cinema/201112/s4ee044bd_1346b016dbb__7cd5.jpg) center center no-repeat #fff;vertical-align:middle;"
     * @param str
     * @return
     */
    private static String getPicUrlFromString(String str){
        String regEx="url\\(.+\\)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        List<String> list = new LinkedList<String>();
        while (m.find()){
            list.add(str.substring(m.start() + 4, m.end() - 1));
        }

        if(list.size()<=0){
            return null;
        }else{
            return list.get(list.size()-1);
        }
    }

    /**
     * [浦东新区]陆家嘴西路168号正大广场8楼（近东方明珠）[交通]
     * @param str
     * @return
     */
    private static String getAddressFromString(String str){
        int begin = str.indexOf("]")+1;
        int end = str.lastIndexOf("[");
        return str.substring(begin, end);
    }

    /**
     * /cinema/10
     * @param str
     * @return
     */
    private Integer getCinemaIdFromString(String str){
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
    protected List<CinemaGewaraBasic> doWork(XDefaultContext context) {
        try {
            initCrawler();
        } catch (CrawlerInitFailureException e) {
            this.logger.error(e);
            return null;
        }
        return (List<CinemaGewaraBasic>) crawler.parse();
    }


    public static void main(String[] args) {
        String style="border:1px solid #f4f4f4;background:url(http://img5.gewara.com/sw120h60/images/cinema/201112/s4ee044bd_1346b016dbb__7cd5.jpg) center center no-repeat #fff;vertical-align:middle;";
        String address = "[浦东新区]陆家嘴西路168号正大广场8楼（近东方明珠）[交通]";
        String cinemaIdLink = "/cinema/10";
        //System.out.println(getLastNumberFromString(cinemaIdLink));
    }

}
