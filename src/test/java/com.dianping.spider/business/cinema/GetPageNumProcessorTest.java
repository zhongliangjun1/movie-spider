package com.dianping.spider.business.cinema;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-8
 * Time: PM2:46
 * To change this template use File | Settings | File Templates.
 */
public class GetPageNumProcessorTest {

    public static void main(String[] args) {
        GetPageNumProcessor processor = new GetPageNumProcessor(ProcessName.GET_PAGE_NUM_PROCESS, 310115);
        processor.doWork(null);
    }

}
