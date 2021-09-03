package com.pivot.myandroiddemo.test.indexside;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;
import com.zcolin.gui.zrecyclerview.ZRecyclerView;

import java.util.ArrayList;
import java.util.List;

@ActivityParam(isShowToolBar = true, isShowReturn = true)
public class IndexSideActivity extends BaseActivity {

    private IndexSideBar mIndexSideBar;
    private TextView mIndexBlockDialog;
    private ZRecyclerView listTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_side);
        setToolbarTitle("IndexSideDemo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        List<IndexSideListItemEntity> data = new ArrayList<>();
        data.add(new IndexSideListItemEntity("A1"));data.add(new IndexSideListItemEntity("A2"));data.add(new IndexSideListItemEntity("B1"));data.add(new IndexSideListItemEntity("C1"));
        data.add(new IndexSideListItemEntity("D1"));data.add(new IndexSideListItemEntity("D3"));data.add(new IndexSideListItemEntity("E5"));data.add(new IndexSideListItemEntity("G1"));
        data.add(new IndexSideListItemEntity("G2"));data.add(new IndexSideListItemEntity("G3"));data.add(new IndexSideListItemEntity("H5"));data.add(new IndexSideListItemEntity("H2"));
        data.add(new IndexSideListItemEntity("J1"));data.add(new IndexSideListItemEntity("M1"));data.add(new IndexSideListItemEntity("N1"));data.add(new IndexSideListItemEntity("O1"));
        data.add(new IndexSideListItemEntity("Q1"));data.add(new IndexSideListItemEntity("Q2"));data.add(new IndexSideListItemEntity("Q3"));data.add(new IndexSideListItemEntity("Q7"));
        data.add(new IndexSideListItemEntity("S1"));data.add(new IndexSideListItemEntity("S2"));data.add(new IndexSideListItemEntity("T2"));data.add(new IndexSideListItemEntity("T6"));
        data.add(new IndexSideListItemEntity("U1"));data.add(new IndexSideListItemEntity("U2"));data.add(new IndexSideListItemEntity("U4"));data.add(new IndexSideListItemEntity("V1"));
        data.add(new IndexSideListItemEntity("W1"));data.add(new IndexSideListItemEntity("W2"));data.add(new IndexSideListItemEntity("X3"));data.add(new IndexSideListItemEntity("X7"));
        data.add(new IndexSideListItemEntity("Z1"));data.add(new IndexSideListItemEntity("ZZ1"));data.add(new IndexSideListItemEntity("Z2"));data.add(new IndexSideListItemEntity("Z9"));

        listTest = getView(R.id.list_test);
        IndexSideListAdapter adapter = new IndexSideListAdapter();
        adapter.setDatas(data);
        listTest.setAdapter(adapter);
        listTest.setIsRefreshEnabled(false);
        listTest.setIsShowNoMore(false);
        listTest.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<IndexSideListItemEntity>() {
            @Override
            public void onItemClick(View covertView, int position, IndexSideListItemEntity data) {
                ToastUtil.toastLong(data.text);
            }
        });

        mIndexSideBar = getView(R.id.index_slide_bar);
        mIndexBlockDialog = getView(R.id.index_slide_dialog);
        mIndexSideBar.setIndicatorTv(mIndexBlockDialog);
        mIndexSideBar.setListener(new IndexSideBar.itemClickListener() {
            @Override
            public void onChoosed(int index, String text) {
                System.out.println(text + index);
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).text.contains(text)) {
//                        listTest.getRecyclerView().smoothScrollToPosition(i);
                        smoothMoveToPosition(listTest.getRecyclerView(), i);
                        break;
                    }
                }
            }
        });
    }

    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置和最后一个可见位置之间
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
        }
    }
}
