package com.pivot.myandroiddemo.entity;

import com.zcolin.frame.http.ZReply;

/**
 * 新闻响应实体
 */
public class NewsReply implements ZReply{
    @Override
    public boolean isSuccess() {
        return error_code == 0;
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
    public NewsEntity result;
}
