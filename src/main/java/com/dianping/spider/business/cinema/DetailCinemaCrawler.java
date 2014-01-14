package com.dianping.spider.business.cinema;

import com.dianping.dishremote.remote.dto.movie.CinemaGewaraBasic;
import com.dianping.dishremote.remote.dto.movie.CinemaGewaraDetail;
import com.dianping.dishremote.remote.dto.movie.Service;
import com.dianping.dishremote.remote.dto.movie.Traffic;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-14
 * Time: AM9:44
 * To change this template use File | Settings | File Templates.
 */
public class DetailCinemaCrawler extends AbstractCrawler {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String URL_TEMPLATE = "http://www.gewara.com/cinema/%s";
    private CinemaGewaraBasic cinemaGewaraBasic;
    private String url;


    public DetailCinemaCrawler(CinemaGewaraBasic cinemaGewaraBasic) throws CrawlerInitFailureException {
        super(CrawlerInitType.URL, String.format(URL_TEMPLATE, cinemaGewaraBasic.getId()));
        this.cinemaGewaraBasic = cinemaGewaraBasic;
        this.url = String.format(URL_TEMPLATE, cinemaGewaraBasic.getId());
    }

    @Override
    public CinemaGewaraDetail parse() {
        try{

            CinemaGewaraDetail cinemaGewaraDetail = new CinemaGewaraDetail();
            BeanUtils.copyProperties(cinemaGewaraBasic, cinemaGewaraDetail);

            String mark_integer = doc.getElementById("mark_integer").text();
            String mark_decimal = doc.getElementById("mark_decimal").text();
            BigDecimal grade = new BigDecimal(mark_integer+mark_decimal);
            cinemaGewaraDetail.setGrade(grade);

            try{
                cinemaGewaraDetail.setPhone(doc.select("div.detail_head_text > dl.clear").get(1).text());
            }catch (NullPointerException e){}

            try{
                cinemaGewaraDetail.setSpecialDesc(doc.getElementsByClass("ffst").first().text());
            }catch (NullPointerException e){}


            Elements ui_hide_Elements = doc.getElementsByClass("ui_hide");
            if(ui_hide_Elements!=null){
                Iterator<Element> iterator1 = ui_hide_Elements.iterator();
                while (iterator1.hasNext()){
                    Element element = iterator1.next();
                    String title = element.getElementsByTag("dt").first().text();
                    String body = element.getElementsByTag("dd").first().text();
                    if("优惠公告：".equals(title)){
                        cinemaGewaraDetail.setFavorableNotice(body);
                    }else if("影院介绍：".equals(title)){
                        cinemaGewaraDetail.setIntroduction(body);
                    }
                }
            }

            Elements traffic_content_Elements = doc.select("div#traffic_content > dl.ui_media");
            if(traffic_content_Elements!=null){
                Traffic traffic = new Traffic();
                cinemaGewaraDetail.setTraffic(traffic);
                Iterator<Element> iterator2 = traffic_content_Elements.iterator();
                while (iterator2.hasNext()){
                    Element element = iterator2.next();
                    String title = element.getElementsByClass("t1").first().text();
                    String body = element.getElementsByClass("ui_text").first().text();
                    if("地铁".equals(title)){
                        traffic.setMetro(body);
                    }else if("驾车".equals(title)){
                        traffic.setCar(body);
                    }else if("公交".equals(title)){
                        traffic.setBus(body);
                    }
                }
            }

            Elements cinemaSer_content_Elements = doc.select("div#cinemaSer_content > dl.ui_media");
            if(cinemaSer_content_Elements!=null){
                Service service = new Service();
                cinemaGewaraDetail.setService(service);
                Iterator<Element> iterator3 = cinemaSer_content_Elements.iterator();
                while (iterator3.hasNext()){
                    Element element = iterator3.next();
                    String title = element.getElementsByClass("ui_pic").first().text();
                    String body = element.getElementsByClass("ui_text").first().text();
                    if("卖品".equals(title)){
                        service.setSell(body);
                    }else if("IMAX".equals(title)){
                        service.setImax(body);
                    }else if("3D眼镜".equals(title)){
                        service.setGlass(body);
                    }else if("儿童票".equals(title)){
                        service.setChildrenTicket(body);
                    }else if("退票服务".equals(title)){
                        service.setReturnTicket(body);
                    }else if("休息区".equals(title)){
                        service.setRestArea(body);
                    }else if("情侣座".equals(title)){
                        service.setLoverSeat(body);
                    }
                }
            }


            return cinemaGewaraDetail;
        }catch (NullPointerException e){
            logger.error("dom changed : "+this.url, e);
            return null;
        }
    }



}
