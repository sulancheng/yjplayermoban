package com.zbar.lib;

/**
 * Created by Administrator
 * on 2019/3/28 0028.
 */
public class EventBusMessage {
    public EventBusMessage(String content, int code) {
        this.content = content;
        this.code = code;
    }

    private String content;
    private int code;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
