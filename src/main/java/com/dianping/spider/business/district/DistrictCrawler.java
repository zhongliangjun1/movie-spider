package com.dianping.spider.business.district;

import com.dianping.dishremote.remote.dto.movie.DistrictGewara;
import com.dianping.spider.util.crawler.AbstractCrawler;
import com.dianping.spider.util.crawler.CrawlerInitType;
import com.dianping.spider.util.exception.CrawlerInitFailureException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM10:34
 * To change this template use File | Settings | File Templates.
 *
 *
 * http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=310115
 *
 * <dl class="clear dlTy twoDlTy " id="district_content">
     <dd>
     <a class="selected">浦东新区</a>
     <a href="javascript:search('countycode', '310101')">黄浦区</a>
     <a href="javascript:search('countycode', '310104')">徐汇区</a>
     <a href="javascript:search('countycode', '310112')">闵行区</a>
     </dd>
   </dl>
 */
public class DistrictCrawler extends AbstractCrawler {

    private static final String URL_TEMPLATE = "http://www.gewara.com/shanghai/movie/searchCinema.xhtml?countycode=%s";
    private int firstDistrictId;
    private String url;

    public DistrictCrawler(int firstDistrictId) throws CrawlerInitFailureException {
        super(CrawlerInitType.URL, String.format(URL_TEMPLATE, firstDistrictId));
        this.firstDistrictId = firstDistrictId;
        this.url = String.format(URL_TEMPLATE, firstDistrictId);
    }

    @Override
    public List<DistrictGewara> parse() {

        try{
            Element district_content = doc.getElementById("district_content");
            Elements a_Elements = district_content.getElementsByTag("a");
            Iterator<Element> iterator = a_Elements.iterator();
            List<DistrictGewara> districtList = new LinkedList<DistrictGewara>();
            while (iterator.hasNext()){
                Element a = iterator.next();
                DistrictGewara district = new DistrictGewara();
                if(a.hasClass("selected")){
                    district.setId(firstDistrictId);
                    district.setName(a.text());
                }else {
                    String href = a.attr("href");
                    district.setId(getLastNumberFromString(href));
                    district.setName(a.text());
                }
                districtList.add(district);
            }
            return districtList;

        }catch (NullPointerException e){
            logger.error("dom changed : "+this.url, e);
            return null;
        }

    }

    private Integer getLastNumberFromString(String str){
        String regEx="\'\\d+\'";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        List<Integer> list = new LinkedList<Integer>();
        while (m.find()){
            list.add(Integer.parseInt(str.substring(m.start() + 1, m.end() - 1)));
            //System.out.println(Integer.parseInt(str.substring(m.start()+1, m.end()-1)));
        }
        if(list.size()<=0){
            return null;
        }else{
            return list.get(list.size()-1);
        }
    }

    public static void main(String[] args) throws CrawlerInitFailureException {
        DistrictCrawler crawler = new DistrictCrawler(310115);
        //List<DistrictGewara> districtList = crawler.parse();
        System.out.println(crawler.getLastNumberFromString("ppp'12'uuu'123'kkk"));
    }
}
