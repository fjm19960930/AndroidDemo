/*
 * *********************************************************
 *   author   fanjiaming
 *   company  telchina
 *   email    fanjiaming@telchina.net
 *   date     18-12-26 下午3:23
 * ********************************************************
 */

package com.pivot.myandroiddemo.entity;

import com.zcolin.frame.http.ZReply;

/**
 * 天气信息响应实体
 */

public class WeatherReply implements ZReply {
    @Override
    public boolean isSuccess() {
        return reason.equals("查询成功!") || reason.equals("查询成功");
    }

    @Override
    public int getReplyCode() {
        return error_code;
    }

    @Override
    public String getErrorMessage() {
        return error_code + reason;
    }

    public String reason;
    public int error_code;
    public WeatherFutureEntity result;
}
