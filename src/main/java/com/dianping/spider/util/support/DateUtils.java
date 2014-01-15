package com.dianping.spider.util.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-15
 * Time: PM1:36
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {


    public static List<String> getTimesFromNow(DateFormat dateFormat,int numOfDays){
        if (dateFormat==null || numOfDays<=0)
            return null;

        List<String> list = new LinkedList<String>();
        Calendar cal = Calendar.getInstance();
        while(numOfDays>0){
            String time = dateFormat.format(cal.getTime());
            list.add(time);
            if(numOfDays>0){
                cal.add(Calendar.DAY_OF_MONTH, 1);
                numOfDays--;
            }
        }

        return list;
    }

    public static Date strToDate(DateFormat dateFormat, String str){
        if(dateFormat==null || StringUtils.isEmpty(str))
            return null;

        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }


    // 跨年、跨月Test:  cal.set(Calendar.MONTH, 11);cal.set(Calendar.DAY_OF_MONTH, 29);
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = getTimesFromNow(sdf, 7);
        System.out.println(list.size());
    }


}
