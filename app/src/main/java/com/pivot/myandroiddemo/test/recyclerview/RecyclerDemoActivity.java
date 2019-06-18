package com.pivot.myandroiddemo.test.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;

import java.util.ArrayList;

/**
 * RecyclerViewDemo
 */
public class RecyclerDemoActivity extends BaseActivity {
    private ArrayList<String> mDatas = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_demo);
        setToolbarTitle("RecyclerViewDemo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));


        for (int i = 0; i < 300; i++){
            mDatas.add("第 " + i +" 个item");
        }
        mRecyclerView = findViewById(R.id.recycler_view);
        
        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        
        //网格布局
        //如果是横向滚动，后面的数值表示的是几行，如果是竖向滚动，后面的数值表示的是几列
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(gridLayoutManager);

        //瀑布流布局
//        StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(staggeredManager);
        
        //初始化分隔线、添加分隔线
        DividerItemDecoration mDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);
        
        RecyclerDemoAdapter adapter = new RecyclerDemoAdapter(mActivity, mDatas);
        mRecyclerView.setAdapter(adapter);
    }
}
