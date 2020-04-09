package com.csu.xgum.bean;

/**
 * Created by Administrator
 * on 2019/4/2 0002.
 */
public class MyTrowable extends Throwable {
    public MyTrowable(String message, int errocode) {
        super(message);
        this.errocode = errocode;
    }

    private int errocode;

    public int getErrocode() {
        return errocode;
    }

    public void setErrocode(int errocode) {
        this.errocode = errocode;
    }
}
