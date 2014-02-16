package com.dianping.spider.business.movie.doubansdk;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-2-16
 * Time: PM4:51
 * To change this template use File | Settings | File Templates.
 */
public class Images implements Serializable {

    private static final long serialVersionUID = -5624524227459494225L;

    private String small;
    private String large;
    private String medium;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
