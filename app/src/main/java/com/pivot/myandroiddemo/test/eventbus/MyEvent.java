package com.pivot.myandroiddemo.test.eventbus;

/**
 * Created by ASUS on 2019/6/28.
 */
public class MyEvent {
    private String mMsg;

    public MyEvent(String mMsg) {
        this.mMsg = mMsg;
    }

    public String getmMsg() {
        return mMsg;
    }
}
