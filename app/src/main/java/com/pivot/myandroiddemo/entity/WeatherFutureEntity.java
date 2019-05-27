package com.pivot.myandroiddemo.entity;

import java.util.List;

/**
 * 天气预报信息实体类
 */
public class WeatherFutureEntity {
    public List<FutureEntity> future;
    public class FutureEntity {
        public String date;
        public String temperature;
        public WidEntity wid;
    }
    public class WidEntity {
        public String day;
    }
}
