package com.dianping.spider.util.crawler;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM9:47
 * To change this template use File | Settings | File Templates.
 */
public enum CrawlerInitType {

    URL("url"),
    HTML("html"),
    FILE("file"),
    AJAX_HTML("ajax_html");

    public String value;

    private CrawlerInitType(String value){
        this.value = value;
    }

}
