package com.pivot.myandroiddemo.test.tablelayout;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.zcolin.gui.zrecyclerview.ZRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TabDemoFragment extends BaseFragment {

    public static TabDemoFragment newInstance(String t) {
        Bundle args = new Bundle();
        args.putString("title", t);
        TabDemoFragment fragment = new TabDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        return R.layout.fragment_tab_demo;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);
        
        ZRecyclerView recyclerView = getView(R.id.tab_demo_list);
        recyclerView.setIsRefreshEnabled(false);
        recyclerView.setIsShowNoMore(false);

        List<TabDemoListItemEntity> datas = new ArrayList<>();
        for(int i = 0; i < 22; i++) {
            datas.add(new TabDemoListItemEntity(getArguments().getString("title"), "错误：（1884）警告：以非位置格式指定的多个替换; 你的意思是添加formatted =“false”属性吗？"));
        }
        TabDemoListAdapter adapter = new TabDemoListAdapter();
        adapter.setDatas(datas);
        recyclerView.setAdapter(adapter);
    }
}
