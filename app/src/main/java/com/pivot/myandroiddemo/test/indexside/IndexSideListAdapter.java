package com.pivot.myandroiddemo.test.indexside;

import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;

/**
 * Created by ASUS on 2019/5/31.
 */

public class IndexSideListAdapter extends BaseRecyclerAdapter<IndexSideListItemEntity> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.test_item_layout;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, IndexSideListItemEntity data) {
        TextView tvItem = getView(holder, R.id.tv_test_item);
        tvItem.setText(data.text);
    }
}
