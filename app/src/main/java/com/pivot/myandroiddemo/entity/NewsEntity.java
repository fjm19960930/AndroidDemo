package com.pivot.myandroiddemo.entity;

import java.util.List;

/**
 * 新闻内容实体
 */
public class NewsEntity {
    public List<NewsItemEntity> data;
    public class NewsItemEntity {
        public String title;//标题
        public String date;//日期
        public String category;//新闻类型
        public String author_name;//作者
        public String url;//新闻网址
        public String thumbnail_pic_s;//第一张图片 至少一张
        public String thumbnail_pic_s02;//第二张图片
        public String thumbnail_pic_s03;//第三张图片
    }
}
