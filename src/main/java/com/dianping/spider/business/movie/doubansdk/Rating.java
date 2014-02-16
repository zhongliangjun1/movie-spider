package com.dianping.spider.business.movie.doubansdk;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-2-16
 * Time: PM4:48
 * To change this template use File | Settings | File Templates.
 */
public class Rating implements Serializable {
    private static final long serialVersionUID = -5232310160177997172L;

    private int max;
    private float average;
    private int stars;
    private int min;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
