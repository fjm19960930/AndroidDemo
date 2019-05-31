package com.pivot.myandroiddemo.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.activity.NewsWebActivity;
import com.pivot.myandroiddemo.entity.NewsEntity;
import com.zcolin.frame.imageloader.ImageLoaderUtils;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;

/**
 * 新闻列表适配器
 */

public class NewsListAdapter extends BaseRecyclerAdapter<NewsEntity.NewsItemEntity> {
    private Context context;

    public NewsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.news_item_layout;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, NewsEntity.NewsItemEntity data) {
        View newsItemLayout = getView(holder, R.id.layout_news_item);
        TextView newsTitle = getView(holder, R.id.tv_news_title);
        TextView newsAuthor = getView(holder, R.id.tv_news_author);
        TextView newsDate = getView(holder, R.id.tv_news_date);
        ImageView newsImage = getView(holder, R.id.iv_news_item);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_item_enter);
        
        newsTitle.setText(data.title);
        newsAuthor.setText(data.author_name);
        newsDate.setText(data.date);
        ImageLoaderUtils.displayImage(context, data.thumbnail_pic_s, newsImage);
        newsItemLayout.startAnimation(animation);
        newsItemLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsWebActivity.class);
            intent.putExtra("newsUrl", data.url);
            context.startActivity(intent);
        });
    }
}
