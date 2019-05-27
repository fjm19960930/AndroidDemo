/*
 * *********************************************************
 *   author   fanjiaming
 *   company  telchina
 *   email    fanjiaming@telchina.net
 *   date     18-12-25 下午4:08
 * ********************************************************
 */

package com.pivot.myandroiddemo.adpter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.biz.WeatherMgr;
import com.pivot.myandroiddemo.entity.WeatherFutureEntity;
import com.zcolin.frame.util.CalendarUtil;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;


/**
 * 天气预报列表适配器
 */
public class WeatherListAdapter extends BaseRecyclerAdapter<WeatherFutureEntity.FutureEntity> {
    private String[] icons = new String[]{"th_weather/weather_0.png", "th_weather/weather_1.png", "th_weather/weather_2.png", 
            "th_weather/weather_3.png", "th_weather/weather_4.png", "th_weather/weather_5.png", "th_weather/weather_6.png", 
            "th_weather/weather_7.png", "th_weather/weather_8.png", "th_weather/weather_9.png", "th_weather/weather_10.png", 
            "th_weather/weather_11.png", "th_weather/weather_12.png", "th_weather/weather_13.png", "th_weather/weather_14.png", 
            "th_weather/weather_15.png", "th_weather/weather_16.png", "th_weather/weather_17.png", "th_weather/weather_18.png", 
            "th_weather/weather_19.png", "th_weather/weather_20.png"};
    private Context context;
    public WeatherListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.main_my_setting_weather_item_view;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, WeatherFutureEntity.FutureEntity data) {
        TextView date = getView(holder, R.id.tv_forecast_date);
        ImageView icon = getView(holder, R.id.iv_forecast_icon);
        TextView temp = getView(holder, R.id.tv_forecast_temp);
        
        String year = String.valueOf(CalendarUtil.getCurrentYear());
        date.setText(data.date.replace(year + "-", ""));
        temp.setText(data.temperature);
        icon.setImageBitmap(WeatherMgr.getBitmap(context,icons[WeatherMgr.getWeatherCode(data.wid.day)]));
    }
}
