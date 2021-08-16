package com.pivot.myandroiddemo.biz;

import com.pivot.myandroiddemo.entity.NewsEntity;
import com.pivot.myandroiddemo.entity.NewsReply;
import com.zcolin.frame.http.ZHttp;
import com.zcolin.frame.http.response.ZResponse;

import okhttp3.Response;

/**
 * 新闻业务管理类
 */

public class NewsMgr {
    private static final String NEWS_URL = "https://v.juhe.cn/toutiao/index?type=%s&key=eb835aee3b2c44168b6aabbc549b9eba";

    /**
     * 请求新闻数据
     */
    public static void requestNewsInfo(String newsType, OnGetNewsFinishListener listener) {
        String newsUrl = String.format(NEWS_URL, newsType);
        ZHttp.get(newsUrl, new ZResponse<NewsReply>(NewsReply.class) {
            @Override
            public void onSuccess(Response response, NewsReply resObj) {
                if (listener != null) {
                    if (resObj.result != null) {
                        listener.onSuccess(resObj.result);
                    } else {
                        listener.onError(-1, "没有获取到新闻信息");
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                listener.onError(code, error);
            }
        });
    }
    
    public interface OnGetNewsFinishListener {
        /**
         * 获取新闻内容成功 回调
         */
        void onSuccess(NewsEntity news);

        /**
         * 获取新闻内容失败 回调
         */
        void onError(int code, String errorMsg);
    }
}
