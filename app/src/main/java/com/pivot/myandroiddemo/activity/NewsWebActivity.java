package com.pivot.myandroiddemo.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

/**
 * 单条新闻的web页面
 */
@ActivityParam(isShowToolBar = true, isShowReturn = true)
public class NewsWebActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        WebView newsWeb = findViewById(R.id.web_news);
        newsWeb.loadUrl(getIntent().getStringExtra("newsUrl"));
    }
}
