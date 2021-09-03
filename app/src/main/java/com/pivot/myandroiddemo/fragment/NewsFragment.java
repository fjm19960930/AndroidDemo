package com.pivot.myandroiddemo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.activity.NewsWebActivity;
import com.pivot.myandroiddemo.adpter.NewsListAdapter;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.pivot.myandroiddemo.biz.NewsMgr;
import com.pivot.myandroiddemo.entity.NewsEntity;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zcolin.gui.ZBanner;
import com.zcolin.gui.zrecyclerview.ZRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 每日新闻Fragment
 */
public class NewsFragment extends BaseFragment {
    private static List<String> TYPELIST = Arrays.asList("top", "tiyu", "yule", "guonei", "keji", "guoji");
    
    private ZBanner newsBanner;
    private ZRecyclerView newsListView;
    private List<View> tabListView;
    private List<BadgeView> badgeViewList;
    private int index = 0;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTabMode(tabListView, index);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getRootViewLayId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);

        TextView tabTop = getView(R.id.tab_news_top);tabTop.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//头条
        TextView tabSport = getView(R.id.tab_news_sport);tabSport.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//体育
        TextView tabEntertainment = getView(R.id.tab_news_entertainment);tabEntertainment.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//娱乐
        TextView tabInternal = getView(R.id.tab_news_internal);tabInternal.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//国内
        TextView tabTechnology = getView(R.id.tab_news_technology);tabTechnology.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//科技
        TextView tabInternational = getView(R.id.tab_news_international);tabInternational.setTextColor(getResources().getColorStateList(R.color.news_tab_text_color_selector));//国外
        tabListView = Arrays.asList(tabTop, tabSport, tabEntertainment, tabInternal, tabTechnology, tabInternational);
        badgeViewList = Arrays.asList(new BadgeView(tabTop.getContext()), new BadgeView(tabSport.getContext()), new BadgeView(tabEntertainment.getContext()),
                                        new BadgeView(tabInternal.getContext()), new BadgeView(tabTechnology.getContext()), new BadgeView(tabInternational.getContext()));
        tabTop.setOnClickListener(v -> {
            setTabMode(tabListView, 0);
            this.index = 0;
        });
        tabSport.setOnClickListener(v -> {
            setTabMode(tabListView, 1);
            this.index = 1;
        });
        tabEntertainment.setOnClickListener(v -> {
            setTabMode(tabListView, 2);
            this.index = 2;
        });
        tabInternal.setOnClickListener(v -> {
            setTabMode(tabListView, 3);
            this.index = 3;
        });
        tabTechnology.setOnClickListener(v -> {
            setTabMode(tabListView, 4);
            this.index = 4;
        });
        tabInternational.setOnClickListener(v -> {
            setTabMode(tabListView, 5);
            this.index = 5;
        });
        setBadgeCount(badgeViewList.get(3), tabInternal, 2);
        setBadgeCount(badgeViewList.get(1), tabSport, 5);

        newsListView = getView(R.id.news_list);
        newsListView.setIsLoadMoreEnabled(false);
        newsListView.setIsRefreshEnabled(false);
        
        newsBanner = getView(R.id.news_banner);
        requestNewsDate("top");
        newsBanner.setBannerStyle(ZBanner.CIRCLE_INDICATOR_TITLE)
                .setIndicatorGravity(ZBanner.CENTER)
                .setDelayTime(3000)
                .startAutoPlay();
        
        RefreshLayout refreshLayout =  getView(R.id.layout_refresh);
        refreshLayout.setRefreshHeader(new WaterDropHeader(mActivity));
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            requestNewsDate(TYPELIST.get(index));
            refreshLayout1.finishRefresh(2000, true);
        });
    }

    /**
     * 请求新闻数据
     */
    private void requestNewsDate(String type) {
        NewsMgr.requestNewsInfo(type, new NewsMgr.OnGetNewsFinishListener() {
            @Override
            public void onSuccess(NewsEntity news) {
                List<String> mainImgUrl = new ArrayList<>();
                if (news.data.get(0).thumbnail_pic_s03 != null) {
                    mainImgUrl = Arrays.asList(news.data.get(0).thumbnail_pic_s, news.data.get(0).thumbnail_pic_s02, news.data.get(0).thumbnail_pic_s03);
                } else if (news.data.get(0).thumbnail_pic_s03 == null) {
                    if (news.data.get(0).thumbnail_pic_s02 == null) {
                        mainImgUrl = Collections.singletonList(news.data.get(0).thumbnail_pic_s);
                    } else {
                        mainImgUrl = Arrays.asList(news.data.get(0).thumbnail_pic_s, news.data.get(0).thumbnail_pic_s02);
                    }
                }
                newsBanner.setImages(mainImgUrl).setBannerTitle(Collections.singletonList(news.data.get(0).title)).startAutoPlay();
                newsBanner.setOnBannerClickListener((view, position) -> {
                    Intent intent = new Intent(mActivity, NewsWebActivity.class);
                    intent.putExtra("newsUrl", news.data.get(0).url);
                    mActivity.startActivity(intent);
                });
                NewsListAdapter adapter = new NewsListAdapter(mActivity);
                adapter.setDatas(news.data.subList(1, news.data.size()));
                newsListView.setAdapter(adapter);
            }

            @Override
            public void onError(int code, String errorMsg) {
                System.out.println(errorMsg);
            }
        });
    }

    /**
     * tab项文字后的提示数字
     */
    private void setBadgeCount(BadgeView b, View v, int count) {
        b.setTargetView(v);
        b.setBackground(9, Color.WHITE);
        b.setHideOnNull(true);
        b.setBadgeCount(count);
        b.setTextColor(Color.parseColor("#33864c"));
    }
    
    /**
     * 切换tab状态
     */
    private void setTabMode(List<View> tabList, int index) {
        setBadgeCount(badgeViewList.get(index), tabListView.get(index), 0);
        for (int i = 0; i < tabList.size(); i++) {
            if (i == index) {
                tabList.get(i).setSelected(true);
            } else {
                tabList.get(i).setSelected(false);
            }
        }
        requestNewsDate(TYPELIST.get(index));
    }
}
