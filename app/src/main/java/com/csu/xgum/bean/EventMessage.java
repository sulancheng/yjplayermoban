package com.csu.xgum.bean;

/**
 * 作者：sucheng on 2019/9/12 0012 11:20
 */
public class EventMessage {
    private String type;
    private String lei;

    public EventMessage(String type) {
        this.type = type;
    }

    public EventMessage(String type, String lei) {
        this.type = type;
        this.lei = lei;
    }

    public String getLei() {
        return lei;
    }

    public void setLei(String lei) {
        this.lei = lei;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
