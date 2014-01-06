package com.dianping.spider.util.exception;

/**
 * Created with IntelliJ IDEA.
 * Author: liangjun.zhong
 * Date: 14-1-6
 * Time: PM9:35
 * To change this template use File | Settings | File Templates.
 */
public class IllegalParameterException extends RuntimeException {

    private static final long serialVersionUID = -1462719232179547243L;

    public IllegalParameterException() {
    }

    public IllegalParameterException(String s) {
        super(s);
    }

    public IllegalParameterException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IllegalParameterException(Throwable throwable) {
        super(throwable);
    }
}
