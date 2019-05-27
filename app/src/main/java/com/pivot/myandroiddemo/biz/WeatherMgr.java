package com.pivot.myandroiddemo.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pivot.myandroiddemo.entity.WeatherFutureEntity;
import com.pivot.myandroiddemo.entity.WeatherReply;
import com.zcolin.frame.http.ZHttp;
import com.zcolin.frame.http.response.ZResponse;

import java.io.IOException;

import okhttp3.Response;

/**
 * 天气业务管理类
 */
public class WeatherMgr {
    private static final String WEATHER_URL = "http://apis.juhe.cn/simpleWeather/query?city=%s&key=cfe8e8bb2a5a51cecdd87c21f3467714";

    /**
     * 请求天气数据
     */
    public static void requestWeatherInfo(String cityName, OnGetWeatherFinishListener listener) {
        String weatherUrl = String.format(WEATHER_URL, cityName);
        ZHttp.get(weatherUrl, new ZResponse<WeatherReply>(WeatherReply.class) {
            @Override
            public void onSuccess(Response response, WeatherReply resObj) {
                if (listener != null) {
                    if (resObj.result != null) {
                        listener.onSuccess(resObj.result);
                    } else {
                        listener.onError(-1, "没有获取到天气信息");
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                listener.onError(code, error);
            }
        });
    }
    
    public static Bitmap getBitmap(Context context, String fileName) {
        try {
            return BitmapFactory.decodeStream(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public interface OnGetWeatherFinishListener {
        /**
         * 获取天气信息成功 回调
         */
        void onSuccess(WeatherFutureEntity weather);

        /**
         * 获取天气信息失败 回调
         */
        void onError(int code, String errorMsg);
    }
    
    /**
     * 获取天气类型码
     *
     * @param weatherStr 天气类型
     */
    public static int getWeatherCode(String weatherStr) {
        int code = 0;
        switch (weatherStr) {
            case "00": //晴
                code = 0;
                break;
            case "01": //多云
                code = 1;
                break;
            case "02": //阴
                code = 4;
                break;
            case "03":
            case "04": //阵雨 雷阵雨
                code = 9;
                break;
            case "05": //冰雹
                code = 5;
                break;
            case "06": //雨夹雪
                code = 10;
                break;
            case "07": //小雨
                code = 6;
                break;
            case "08": //中雨
                code = 7;
                break;
            case "09":
            case "10":
            case "11":
            case "12": //大雨及其以上
                code = 8;
                break;
            case "13": //阵雪
                code = 11;
                break;
            case "14": //小雪
                code = 11;
                break;
            case "15": //中雪
                code = 12;
                break;
            case "16": //大雪
                code = 13;
                break;
            case "17": //暴雪
                code = 14;
                break;
            case "18":
            case "29": //雾 浮尘
                code = 18;
                break;
            case "19": //冻雨
                code = 5;
                break;
            case "20":
            case "30":
            case "31": //沙尘暴 扬沙
                code = 16;
                break;
            case "21":
            case "22": //中雨以下
                code = 7;
                break;
            case "23":
            case "24":
            case "25": //大雨以上
                code = 8;
                break;
            case "26":
            case "27":
            case "28": //中雪以上
                code = 13;
                break;
            case "53": //霾
                code = 17;
                break;
        }
        return code;
    }
}
