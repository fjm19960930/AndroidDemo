package com.pivot.myandroiddemo.test.tablelayout;

import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;

/**
 * Created by ASUS on 2019/6/14.
 */

public class TabDemoListAdapter extends BaseRecyclerAdapter<TabDemoListItemEntity>{

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.tab_demo_list_item_layout;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, TabDemoListItemEntity data) {
        TextView title = getView(holder, R.id.tv_tab_list_item_title);
        TextView desc = getView(holder, R.id.tv_tab_demo_item_desc);
        title.setText(data.title);
        desc.setText(data.desc);
    }
}
