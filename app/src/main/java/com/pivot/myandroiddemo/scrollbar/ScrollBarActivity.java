package com.pivot.myandroiddemo.scrollbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;

public class ScrollBarActivity extends BaseActivity {
    private ListView mListView;
    private ScrollBarLayout mScrollBarView;
    private boolean mListViewOnScrollBarCtroled = false;// listview是否是滚动条控制的滚动
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private boolean isFullScreen;//列表是否铺满一屏
    private final int mRowCount = 1;
    private Animation animation;
    private boolean isScrollDown;
    private int mFirstPosition;
    private int mFirstTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_bar);
        setToolbarTitle("listView滚动条");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        try {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListView = (ListView) findViewById(R.id.list_view);
        mScrollBarView = (ScrollBarLayout) findViewById(R.id.scroll_bar);
        mScrollBarView.setVisibility(View.GONE);

        mListView.setAdapter(new ListViewAdapter(this, getData(), animation));		
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getScrollY() != 0) {
                    return;
                }
                if (mScrollBarView != null) {
                    mListViewOnScrollBarCtroled = false;
                    mScrollBarView.scrollStateChanged(scrollState, isFullScreen);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mVisibleItemCount = mVisibleItemCount == 0 ? (visibleItemCount + 1) : Math.max(mVisibleItemCount, visibleItemCount);
                isFullScreen = (totalItemCount - mVisibleItemCount) > ScrollBarLayout.OFFSET_NUM;
                if (mScrollBarView != null && view.getChildCount() > 0) {
                    mTotalItemCount = totalItemCount;
                    if (!mListViewOnScrollBarCtroled) {
                        mScrollBarView.setScrollBarProgress(view, firstVisibleItem, visibleItemCount, totalItemCount, mRowCount);
                    }
                }
                View firstChild = view.getChildAt(0);
                if (firstChild == null) {
                    return;
                }
                int top = firstChild.getTop();
                /**
                 * firstVisibleItem > mFirstPosition表示向下滑动一整个Item
                 * mFirstTop > top表示在当前这个item中滑动
                 */
                isScrollDown = firstVisibleItem > mFirstPosition || mFirstTop > top;
                mFirstTop = top;
                mFirstPosition = firstVisibleItem;
            }
        });

        if (mScrollBarView != null) {
            mScrollBarView.setOnBarListener(new ScrollBarLayout.OnBarListener() {
                @Override
                public void onBarProgressChanged(double progress) {
                    int shouldShowPosition = (int) ((mTotalItemCount - mVisibleItemCount + 2 * mRowCount) * progress + 0.5);
                    mListView.setSelection(shouldShowPosition);
                }

                @Override
                public void onBarControl(boolean underCtrl) {
                    mListViewOnScrollBarCtroled = underCtrl;
                }
            });
        }
    }

    private String[] getData() {
        int counts = 100;
        // 测试数据
        String[] numbers = new String[counts];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = " NO. " + i;
        }
        return numbers;
    }
    
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext;
        private String[] texts;
        private Animation anim;

        public ListViewAdapter(Context context, String[] texts, Animation animation) {
            this.mContext = context;
            this.texts = texts;
            this.anim = animation;
        }

        public void notifyDataSetChanged(String[] texts) {
            this.texts = texts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return texts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder viewCache = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
                viewCache = new ItemViewHolder();
                viewCache.mTextView = (TextView) convertView.findViewById(R.id.text);
            } else {
                viewCache = (ItemViewHolder) convertView.getTag();
            }
            //清除当前显示区域中所有item的动画
            for (int i = 0; i < mListView.getChildCount(); i++) {
                View view = mListView.getChildAt(i);
                view.clearAnimation();
            }
            if (isScrollDown) {
                convertView.startAnimation(anim);
            }
            convertView.setTag(viewCache);
            viewCache.mTextView.setText(texts[position]);
            return convertView;
        }
    }

    private static class ItemViewHolder {
        public TextView mTextView;
    }
}

