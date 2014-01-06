package com.dianping.spider.util.exception;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM9:29
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerInitFailureException extends Exception {

    private static final long serialVersionUID = 8797662660084021357L;

    public CrawlerInitFailureException(){}

    public CrawlerInitFailureException(String message){
        super(message);
    }

    public CrawlerInitFailureException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CrawlerInitFailureException(Throwable throwable) {
        super(throwable);
    }
}
