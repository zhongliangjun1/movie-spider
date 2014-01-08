package com.dianping.spider.business.district;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-7
 * Time: PM3:12
 * To change this template use File | Settings | File Templates.
 */
public class District implements Serializable {

    private static final long serialVersionUID = -4111565934249592224L;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
